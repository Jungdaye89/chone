package com.chone.server.commons.lock;

import com.chone.server.commons.exception.LockTimeoutException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class InMemoryLockTemplate implements DistributedLockTemplate {

  private final ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, Long> lockTimeMap = new ConcurrentHashMap<>();

  @Scheduled(fixedRate = 60000)
  public void cleanupExpiredLocks() {
    long now = System.currentTimeMillis();
    lockTimeMap.forEach(
        (key, creationTime) -> {
          if (now - creationTime > TimeUnit.HOURS.toMillis(1)) {
            ReentrantLock lock = lockMap.get(key);
            if (lock != null && !lock.isLocked()) {
              lockMap.remove(key);
              lockTimeMap.remove(key);
            }
          }
        });
  }

  @Override
  public <T> T executeWithLock(
      String lockKey, long timeout, TimeUnit timeUnit, Supplier<T> callback)
      throws LockTimeoutException {

    ReentrantLock lock =
        lockMap.computeIfAbsent(
            lockKey,
            k -> {
              lockTimeMap.put(k, System.currentTimeMillis());
              return new ReentrantLock();
            });

    boolean acquired = false;

    try {
      acquired = lock.tryLock(timeout, timeUnit);
      if (!acquired) {
        throw new LockTimeoutException("락 획득 시간 초과: " + lockKey);
      }
      return callback.get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("락 획득 중 인터럽트 발생", e);
    } catch (Exception e) {
      if (!(e instanceof LockTimeoutException)) {
        log.error("콜백 실행 중 발생한 예외: {}", e);
        throw e;
      }
      throw e;
    } finally {
      if (acquired) {
        lock.unlock();
      }
    }
  }
}

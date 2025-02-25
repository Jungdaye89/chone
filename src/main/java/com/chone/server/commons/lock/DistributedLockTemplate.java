package com.chone.server.commons.lock;

import com.chone.server.commons.exception.LockTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface DistributedLockTemplate {
  /**
   * @param lockKey 락을 획득할 고유 키
   * @param timeout 락 획득 시도 제한 시간
   * @param timeUnit 시간 단위
   * @param callback 락을 획득한 후 실행할 콜백 함수
   * @return 콜백 함수의 실행 결과
   * @throws LockTimeoutException 락 획득 시간이 초과된 경우
   */
  <T> T executeWithLock(String lockKey, long timeout, TimeUnit timeUnit, Supplier<T> callback)
      throws LockTimeoutException;

  default void executeWithLock(String lockKey, long timeout, TimeUnit timeUnit, Runnable callback)
      throws LockTimeoutException {
    executeWithLock(
        lockKey,
        timeout,
        timeUnit,
        () -> {
          callback.run();
          return null;
        });
  }
}

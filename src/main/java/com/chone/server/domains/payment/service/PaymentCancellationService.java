package com.chone.server.domains.payment.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.lock.DistributedLockTemplate;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderCancelReason;
import com.chone.server.domains.order.service.OrderService;
import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.domain.PaymentStatus;
import com.chone.server.domains.payment.domain.PgPaymentLog;
import com.chone.server.domains.payment.dto.request.CancelPaymentRequest;
import com.chone.server.domains.payment.dto.response.CancelPaymentResponse;
import com.chone.server.domains.payment.exception.PaymentExceptionCode;
import com.chone.server.domains.payment.infrastructure.pg.PgApiService;
import com.chone.server.domains.payment.repository.PaymentRepository;
import com.chone.server.domains.payment.repository.PgPaymentLogRepository;
import com.chone.server.domains.user.domain.User;
import jakarta.persistence.LockTimeoutException;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentCancellationService {
  private static final String LOCK_KEY_PREFIX = "payment:cancel:";
  private static final String PG_STATUS_KEY = "status";
  private static final String PG_MESSAGE_KEY = "message";

  private final PaymentRepository paymentRepository;
  private final PgPaymentLogRepository pgPaymentLogRepository;

  private final PaymentDomainService domainService;
  private final OrderService orderService;
  private final PgApiService pgApiService;
  private final DistributedLockTemplate lockTemplate;

  @Transactional
  public CancelPaymentResponse cancelPayment(
      CustomUserDetails principal, UUID paymentId, @Valid CancelPaymentRequest requestDto) {
    Payment payment = paymentRepository.findById(paymentId);
    User currentUser = principal.getUser();

    domainService.validateCancelPaymentRequest(currentUser, payment);

    try {
      return executeCancellationWithLock(payment, requestDto);
    } catch (LockTimeoutException e) {
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_CANCELLATION_IN_PROGRESS);
    } catch (ApiBusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error("결제 취소 중 오류 발생: {}", e.getMessage(), e);
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_CANCELLATION_ERROR);
    }
  }

  private CancelPaymentResponse executeCancellationWithLock(
      Payment payment, CancelPaymentRequest requestDto) {
    return lockTemplate.executeWithLock(
        LOCK_KEY_PREFIX + payment.getId(),
        10,
        TimeUnit.SECONDS,
        () -> processCancellationTransaction(payment, requestDto));
  }

  private CancelPaymentResponse processCancellationTransaction(
      Payment payment, CancelPaymentRequest requestDto) {
    try {
      Order cancelOrder = cancelOrder(payment);

      Map<String, String> pgResponse = pgApiService.requestCancel();
      String status = pgResponse.get(PG_STATUS_KEY);
      String message = pgResponse.get(PG_MESSAGE_KEY);
      log.info(message);

      if ("success".equals(status)) {
        updatePaymentCancellation(payment, requestDto.reason());
        log.info("결제 취소 완료: paymentId={}, 취소 사유={}", payment.getId(), requestDto.reason());
        return CancelPaymentResponse.from(true, payment);
      } else {
        throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_CANCELLATION_FAILED);
      }
    } catch (ApiBusinessException e) {
      throw e;
    } catch (Exception e) {
      logCancellationFailure(e);
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_CANCELLATION_FAILED);
    }
  }

  private Order cancelOrder(Payment payment) {
    // todo listener
    Order order = orderService.findByOrderId(payment.getOrder().getId());
    OrderCancelReason cancelReason = OrderCancelReason.CANCEL_PAYMENT;
    order.cancel(cancelReason);
    return orderService.saveOrder(order);
  }

  private void updatePaymentCancellation(Payment payment, String reason) {
    payment.updateStatus(PaymentStatus.CANCELED);
    payment.updateCancelReason(reason);
    paymentRepository.save(payment);

    PgPaymentLog pgLog = pgPaymentLogRepository.findByPayment(payment);
    pgLog.cancel();
    pgPaymentLogRepository.save(pgLog);
  }

  private void logCancellationFailure(Exception e) {
    String errorMessage = e.getMessage();
    String errorType = e.getClass().getSimpleName();

    log.error(
        "결제 취소 중 오류 발생: errorType-{} : message-{} : exception-{},", errorType, errorMessage, e);
  }
}

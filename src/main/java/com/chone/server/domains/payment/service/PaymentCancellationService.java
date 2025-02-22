package com.chone.server.domains.payment.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.OrderFacade;
import com.chone.server.commons.lock.DistributedLockTemplate;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderCancelReason;
import com.chone.server.domains.order.service.OrderCancelledEvent;
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
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
  private final OrderFacade orderFacade;
  private final PgApiService pgApiService;
  private final DistributedLockTemplate lockTemplate;

  @Transactional
  public CancelPaymentResponse cancelPayment(
      CustomUserDetails principal, UUID paymentId, @Valid CancelPaymentRequest requestDto) {
    Payment payment = paymentRepository.findById(paymentId);
    User currentUser = principal.getUser();

    domainService.validateCancelPaymentRequest(currentUser, payment);

    try {
      return executeCancellationWithLock(payment, requestDto, true);
    } catch (LockTimeoutException e) {
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_CANCELLATION_IN_PROGRESS);
    } catch (ApiBusinessException e) {
      throw e;
    } catch (Exception e) {
      logError("결제 취소 중 오류 발생", e);
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_CANCELLATION_ERROR);
    }
  }

  @EventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleOrderCancellation(OrderCancelledEvent event) {
    Order cancelledOrder = event.getOrder();
    logInfo("주문 취소 이벤트 수신: orderId={}", cancelledOrder.getId());
    try {
      Payment payment = paymentRepository.findByOrderIdOrNull(cancelledOrder.getId());
      if (payment == null) {
        log.info("주문과 관련된 결제가 없습니다.");
        return;
      }

      domainService.validateEventBasedCancellation(payment);
      String reason = event.isByUser() ? "사용자 주문 취소로 인한 결제 취소" : "시스템 주문 취소로 인한 결제 취소";
      CancelPaymentRequest requestDto = new CancelPaymentRequest(reason);

      CancelPaymentResponse response = executeCancellationWithLock(payment, requestDto, false);

      if (!response.isSuccess()) {
        notifyPaymentCancellationFailure(payment, cancelledOrder);
      }
    } catch (Exception e) {
      logError("주문 취소 후 결제 취소 처리 중 오류: orderId={}", cancelledOrder.getId(), e);
      notifyPaymentCancellationError(cancelledOrder, e);
    }
  }

  private CancelPaymentResponse executeCancellationWithLock(
      Payment payment, CancelPaymentRequest requestDto, boolean isByUser) {
    return lockTemplate.executeWithLock(
        LOCK_KEY_PREFIX + payment.getId(),
        10,
        TimeUnit.SECONDS,
        () -> processCancellationTransaction(payment, requestDto, isByUser));
  }

  private CancelPaymentResponse processCancellationTransaction(
      Payment payment, CancelPaymentRequest requestDto, boolean isByUser) {
    try {
      if (isByUser) cancelOrder(payment);

      Map<String, String> pgResponse = pgApiService.requestCancel();
      String status = pgResponse.get(PG_STATUS_KEY);
      String message = pgResponse.get(PG_MESSAGE_KEY);
      logInfo(message);

      if ("success".equals(status)) {
        updatePaymentCancellation(payment, requestDto.reason());
        logInfo("결제 취소 완료: paymentId={}, 취소 사유={}", payment.getId(), requestDto.reason());
        return CancelPaymentResponse.from(true, payment);
      } else {
        if (!isByUser) {
          logError("PG사 결제 취소 실패: paymentId={}, message={}", payment.getId(), message);
          return CancelPaymentResponse.from(false, payment);
        }
        throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_CANCELLATION_FAILED);
      }
    } catch (ApiBusinessException e) {
      if (!isByUser) {
        logError("결제 취소 비즈니스 예외: paymentId={}, error={}", payment.getId(), e.getMessage());
        return CancelPaymentResponse.from(false, payment);
      }
      throw e;
    } catch (Exception e) {
      logCancellationFailure(e);
      if (!isByUser) {
        return CancelPaymentResponse.from(false, payment);
      }
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_CANCELLATION_FAILED);
    }
  }

  private Order cancelOrder(Payment payment) {
    Order order = orderFacade.findById(payment.getOrder().getId());
    OrderCancelReason cancelReason = OrderCancelReason.CANCEL_PAYMENT;
    order.cancel(cancelReason);
    return orderFacade.save(order);
  }

  private void updatePaymentCancellation(Payment payment, String reason) {
    payment.updateStatus(PaymentStatus.CANCELED);
    payment.updateCancelReason(reason);
    paymentRepository.save(payment);

    PgPaymentLog pgLog = pgPaymentLogRepository.findByPayment(payment);
    pgLog.cancel();
    pgPaymentLogRepository.save(pgLog);
  }

  private void logInfo(String message, Object... args) {
    log.info(message, args);
  }

  private void logError(String message, Object... args) {
    log.error(message, args);
  }

  private void logError(String message, Throwable e) {
    log.error(message + ": {}", e.getMessage(), e);
  }

  private void logError(String message, Object arg, Throwable e) {
    log.error(message, arg, e);
  }

  private void logCancellationFailure(Exception e) {
    String errorMessage = e.getMessage();
    String errorType = e.getClass().getSimpleName();

    logError(
        "결제 취소 중 오류 발생: errorType-{} : message-{} : exception-{},", errorType, errorMessage, e);
  }

  private void notifyPaymentCancellationFailure(Payment payment, Order order) {
    String message =
        String.format(
            "주문(%s)이 취소되었으나 연관된 결제(%s) 취소에 실패했습니다. 수동 처리가 필요합니다.", order.getId(), payment.getId());
    logError(message);
  }

  private void notifyPaymentCancellationError(Order order, Exception e) {
    String message =
        String.format("주문(%s) 취소 후 결제 취소 처리 중 오류가 발생했습니다. 오류: %s", order.getId(), e.getMessage());
    logError(message);
  }
}

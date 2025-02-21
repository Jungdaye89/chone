package com.chone.server.domains.payment.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.OrderFacade;
import com.chone.server.commons.lock.DistributedLockTemplate;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderCancelReason;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.domain.PaymentStatus;
import com.chone.server.domains.payment.domain.PgPaymentLog;
import com.chone.server.domains.payment.domain.PgStatus;
import com.chone.server.domains.payment.dto.request.CreatePaymentRequest;
import com.chone.server.domains.payment.dto.response.CreatePaymentResponse;
import com.chone.server.domains.payment.exception.PaymentExceptionCode;
import com.chone.server.domains.payment.infrastructure.pg.PgApiService;
import com.chone.server.domains.payment.repository.PaymentRepository;
import com.chone.server.domains.payment.repository.PgPaymentLogRepository;
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
public class PaymentProcessService {
  private static final String LOCK_KEY_PREFIX = "payment:process:";
  private static final String PG_STATUS_KEY = "status";
  private static final String PG_MESSAGE_KEY = "message";
  private static final String PG_TRANSACTION_ID_KEY = "transactionId";

  private final PaymentRepository repository;
  private final PgPaymentLogRepository pgPaymentLogRepository;

  private final PaymentDomainService domainService;
  private final OrderFacade orderFacade;
  private final PgApiService pgApiService;
  private final DistributedLockTemplate lockTemplate;

  @Transactional
  public CreatePaymentResponse processPayment(
      @Valid CreatePaymentRequest requestDto, CustomUserDetails principal) {
    Order order = orderFacade.findById(requestDto.orderId());
    verifyNoExistingPayment(order.getId());
    domainService.validatePaymentRequest(order, requestDto, principal.getUser());

    try {
      return executePaymentWithLock(requestDto, order);
    } catch (LockTimeoutException e) {
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_IN_PROGRESS);
    } catch (ApiBusinessException e) {
      throw e;
    } catch (Exception e) {
      log.error("결제 처리 중 오류 발생: {}", e.getMessage(), e);
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_PROCESSING_ERROR);
    }
  }

  private CreatePaymentResponse executePaymentWithLock(
      CreatePaymentRequest requestDto, Order order) {
    return lockTemplate.executeWithLock(
        LOCK_KEY_PREFIX + order.getId(),
        10,
        TimeUnit.SECONDS,
        () -> processPaymentTransaction(requestDto, order));
  }

  private CreatePaymentResponse processPaymentTransaction(
      CreatePaymentRequest requestDto, Order order) {
    verifyNoExistingPayment(order.getId());

    Payment payment = createPaymentEntity(requestDto, order);
    Payment savedPayment = repository.save(payment);

    try {
      Map<String, String> pgResponse = pgApiService.requestPayment();
      updatePaymentWithPgResponse(payment, pgResponse);
      updateOrderStatusBasedOnPayment(order, payment);

      return CreatePaymentResponse.from(savedPayment);
    } catch (Exception e) {
      handlePaymentFailure(payment, e);
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_GATEWAY_ERROR);
    }
  }

  private void verifyNoExistingPayment(UUID orderId) {
    if (repository.existsByOrderId(orderId)) {
      throw new ApiBusinessException(PaymentExceptionCode.ALREADY_PAID);
    }
  }

  private Payment createPaymentEntity(CreatePaymentRequest requestDto, Order order) {
    return Payment.builder(order, PaymentStatus.PENDING, requestDto.paymentMethod()).build();
  }

  private void updatePaymentWithPgResponse(Payment payment, Map<String, String> pgResponse) {
    String status = pgResponse.get(PG_STATUS_KEY);
    String message = pgResponse.get(PG_MESSAGE_KEY);
    String transactionId = pgResponse.get(PG_TRANSACTION_ID_KEY);

    if ("success".equals(status)) {
      updatePaymentStatus(
          payment, PaymentStatus.COMPLETED, PgStatus.PROCESSING_SUCCESS, message, transactionId);
    } else {
      updatePaymentStatus(payment, PaymentStatus.FAILED, PgStatus.PROCESSING_FAILED, message, null);
    }
  }

  private void updatePaymentStatus(
      Payment payment,
      PaymentStatus status,
      PgStatus pgStatus,
      String message,
      String transactionId) {
    payment.updateStatus(status);
    if (status == PaymentStatus.FAILED) payment.updateCancelReason(message);
    repository.save(payment);

    PgPaymentLog.PgPaymentLogBuilder logBuilder = PgPaymentLog.builder(payment, pgStatus, message);
    if (transactionId != null) logBuilder.pgTransactionId(transactionId);
    pgPaymentLogRepository.save(logBuilder.build());

    if (status == PaymentStatus.COMPLETED) {
      log.info("결제 성공: paymentId={}, transactionId={}", payment.getId(), transactionId);
    } else {
      log.warn("PG 결제 실패: paymentId={}, 실패 사유={}", payment.getId(), message);
    }
  }

  private void updateOrderStatusBasedOnPayment(Order order, Payment payment) {
    // todo: listener
    if (payment.getStatus() == PaymentStatus.COMPLETED) {
      order.updateStatus(OrderStatus.PAID);
    } else if (payment.getStatus() == PaymentStatus.FAILED) {
      order.cancel(OrderCancelReason.PAYMENT_FAILED);
    }
    orderFacade.save(order);
  }

  private void handlePaymentFailure(Payment payment, Exception e) {
    String errorMessage = e.getMessage();
    String errorType = e.getClass().getSimpleName();
    String errorCause = e.getCause() != null ? e.getCause().getMessage() : "Unknown cause";
    String failureReason = String.format("[%s] %s - %s", errorType, errorMessage, errorCause);

    payment.updateStatus(PaymentStatus.FAILED);
    payment.updateCancelReason(failureReason);
    repository.save(payment);

    PgPaymentLog pgLog =
        PgPaymentLog.builder(payment, PgStatus.PROCESSING_ERROR, "결제 처리 중 오류 발생: " + e.getMessage())
            .build();
    pgPaymentLogRepository.save(pgLog);

    log.error(
        "결제 처리 중 오류 발생: errorType-{} : message-{} : exception-{},", errorType, errorMessage, e);
  }
}

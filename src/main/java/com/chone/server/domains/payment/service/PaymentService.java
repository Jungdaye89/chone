package com.chone.server.domains.payment.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.lock.DistributedLockTemplate;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.service.OrderService;
import com.chone.server.domains.payment.dto.request.CreatePaymentRequest;
import com.chone.server.domains.payment.dto.response.CreatePaymentResponse;
import com.chone.server.domains.payment.exception.PaymentExceptionCode;
import com.chone.server.domains.payment.repository.PaymentRepository;
import com.chone.server.domains.payment.repository.PgPaymentLogRepository;
import jakarta.persistence.LockTimeoutException;
import jakarta.validation.Valid;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {
  private static final String LOCK_KEY_PREFIX = "payment:order:";
  private final PaymentRepository repository;
  private final PgPaymentLogRepository pgPaymentLogRepository;
  private final PaymentDomainService domainService;
  private final OrderService orderService;
  private final DistributedLockTemplate lockTemplate;

  @Transactional
  public CreatePaymentResponse processPayment(
      @Valid CreatePaymentRequest requestDto, CustomUserDetails principal) {
    Order order = orderService.findByOrderId(requestDto.orderId());
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
    return null;
  }

  private void verifyNoExistingPayment(UUID orderId) {
    if (repository.existsByOrderId(orderId)) {
      throw new ApiBusinessException(PaymentExceptionCode.ALREADY_PAID);
    }
  }
}

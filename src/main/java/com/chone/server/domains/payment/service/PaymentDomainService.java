package com.chone.server.domains.payment.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.domain.PaymentStatus;
import com.chone.server.domains.payment.dto.request.CreatePaymentRequest;
import com.chone.server.domains.payment.dto.response.PaymentDetailResponse;
import com.chone.server.domains.payment.exception.PaymentExceptionCode;
import com.chone.server.domains.user.domain.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class PaymentDomainService {

  public void validatePaymentRequest(
      Order order, @Valid CreatePaymentRequest requestDto, User user) {
    validateOrderStatus(order);
    validatePriceMatch(order, requestDto);
    validatePermissionByRole(order, user);
  }

  public void validateCustomerViewPermission(
      PaymentDetailResponse paymentResponse, User currentUser) {
    if (!(paymentResponse.order().userId().equals(currentUser.getId()))) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_CUSTOMER_PAYMENT_HISTORY);
    }
  }

  public void validateOwnerViewPermission(PaymentDetailResponse paymentResponse, User currentUser) {
    if (!(paymentResponse.order().storeUserId().equals(currentUser.getId()))) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_OWNER_PAYMENT_HISTORY);
    }
  }

  public void validateCancelPaymentRequest(User user, Payment payment) {
    validateCancellationPermission(user, payment);
    validateCancellation(payment);
  }

  private void validateCancellation(Payment payment) {
    if (payment.getStatus().isSameStauts(PaymentStatus.CANCELED)) {
      throw new ApiBusinessException(PaymentExceptionCode.PAYMENT_ALREADY_CANCELED);
    }
    if (payment.getStatus().isSameStauts(PaymentStatus.FAILED)) {
      throw new ApiBusinessException(PaymentExceptionCode.FAILED_PAYMENT);
    }

    if (!payment.getOrder().isCancelable()) {
      throw new ApiBusinessException(PaymentExceptionCode.ORDER_NOT_CANCELABLE);
    }
  }

  private void validateCancellationPermission(User user, Payment payment) {
    switch (user.getRole()) {
      case CUSTOMER -> {
        if (!isOrderOwner(user, payment))
          throwApiException(PaymentExceptionCode.ORDER_CUSTOMER_ACCESS_DENIED);
        return;
      }
      case OWNER -> {
        if (!isOrderStoreOwner(user, payment))
          throwApiException(PaymentExceptionCode.ORDER_STORE_OWNER_ACCESS_DENIED);
        return;
      }
      case MANAGER, MASTER -> {}
    }
    throw new ApiBusinessException(PaymentExceptionCode.CANCEL_PERMISSION_DENIED);
  }

  private void validateOrderStatus(Order order) {
    if (order.getStatus().isSameStatus(OrderStatus.CANCELED)) {
      throwApiException(PaymentExceptionCode.CANCELED_ORDER);
    }
    if (order.getStatus().isSameStatus(OrderStatus.PAID)) {
      throwApiException(PaymentExceptionCode.ALREADY_PAID);
    }
  }

  private void validatePriceMatch(Order order, CreatePaymentRequest requestDto) {
    if (order.getTotalPrice() != requestDto.totalPrice()) {
      throwApiException(PaymentExceptionCode.PRICE_MISMATCH);
    }
  }

  private void validatePermissionByRole(Order order, User currentUser) {
    switch (currentUser.getRole()) {
      case CUSTOMER -> validateCustomerPayPermission(order, currentUser);
      case OWNER -> validateOwnerPayPermission(order, currentUser);
      case MANAGER, MASTER -> {}
    }
  }

  private void validateCustomerPayPermission(Order order, User currentUser) {
    if (order.getOrderType() != OrderType.ONLINE) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_ALLOW_PAY_ONLINE_ORDER);
    }

    if (!(order.getUser().equals(currentUser))) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_CUSTOMER_PAYMENT);
    }
  }

  private void validateOwnerPayPermission(Order order, User currentUser) {
    if (order.getOrderType() != OrderType.OFFLINE) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_ALLOW_PAY_OFFLINE_ORDER);
    }

    if (!(order.getStore().getUser().equals(currentUser))) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_OWNER_PAYMENT);
    }
  }

  private boolean isOrderOwner(User currentUser, Payment payment) {
    User orderOwner = payment.getOrder().getUser();
    return isSameUser(orderOwner, currentUser);
  }

  private boolean isOrderStoreOwner(User currentUser, Payment payment) {
    User storeOwner = payment.getOrder().getStore().getUser();
    return isSameUser(storeOwner, currentUser);
  }

  private boolean isSameUser(User currentUser, User targetUser) {
    return currentUser.equals(targetUser);
  }

  private void throwApiException(PaymentExceptionCode exceptionCode) {
    throw new ApiBusinessException(exceptionCode);
  }
}

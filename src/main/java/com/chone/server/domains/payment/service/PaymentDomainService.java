package com.chone.server.domains.payment.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
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

  private void validateOrderStatus(Order order) {
    if (order.getStatus() == OrderStatus.CANCELED) {
      throw new ApiBusinessException(PaymentExceptionCode.CANCELED_ORDER);
    }
    if (order.getStatus() == OrderStatus.PAID) {
      throw new ApiBusinessException(PaymentExceptionCode.ALREADY_PAID);
    }
  }

  private void validatePriceMatch(Order order, CreatePaymentRequest requestDto) {
    if (order.getTotalPrice() != requestDto.totalPrice()) {
      throw new ApiBusinessException(PaymentExceptionCode.PRICE_MISMATCH);
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

    if (!(order.getUser().getId().equals(currentUser.getId()))) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_CUSTOMER_PAYMENT);
    }
  }

  private void validateOwnerPayPermission(Order order, User currentUser) {
    if (order.getOrderType() != OrderType.OFFLINE) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_ALLOW_PAY_OFFLINE_ORDER);
    }

    if (!(order.getStore().getUser().getId().equals(currentUser.getId()))) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_OWNER_PAYMENT);
    }
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
}

package com.chone.server.domains.payment.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.payment.dto.request.CreatePaymentRequest;
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
      case CUSTOMER -> validateCustomerPermission(order, currentUser);
      case OWNER -> validateOwnerPermission(order, currentUser);
      case MANAGER, MASTER -> {}
    }
  }

  private void validateCustomerPermission(Order order, User currentUser) {
    if (order.getOrderType() != OrderType.ONLINE) {
      throw new ApiBusinessException(PaymentExceptionCode.CUSTOMER_OFFLINE_ORDER);
    }

    if (!order.getUser().getId().equals(currentUser.getId())) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_ORDER_CUSTOMER);
    }
  }

  private void validateOwnerPermission(Order order, User currentUser) {
    if (order.getOrderType() != OrderType.OFFLINE) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_OWNER_STORE);
    }

    if (!order.getStore().getUser().getId().equals(currentUser.getId())) {
      throw new ApiBusinessException(PaymentExceptionCode.NOT_OWNER_STORE);
    }
  }
}

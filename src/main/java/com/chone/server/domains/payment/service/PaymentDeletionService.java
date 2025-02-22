package com.chone.server.domains.payment.service;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.service.OrderDeletedEvent;
import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentDeletionService {

  private final PaymentRepository repository;

  @EventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleOrderDeletion(OrderDeletedEvent event) {
    Order deletedOrder = event.getOrder();
    log.info("[결제] 주문 삭제 이벤트 수신: orderId={}", deletedOrder.getId());

    try {
      Payment payment = repository.findByOrderIdOrNull(deletedOrder.getId());
      if (payment == null) {
        log.info("주문과 관련된 결제가 없습니다.");
      }
      log.info("결제 삭제가 완료되었습니다={}", payment.getId());
      payment.delete(event.getDeletedBy());
    } catch (Exception e) {
      log.error("주문 삭제 후 결제 삭제 처리 중 오류: orderId={}, exception={}", deletedOrder.getId(), e);
    }
  }
}

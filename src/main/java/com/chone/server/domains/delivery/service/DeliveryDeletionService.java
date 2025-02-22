package com.chone.server.domains.delivery.service;

import com.chone.server.domains.delivery.repsitory.DeliveryRepository;
import com.chone.server.domains.order.domain.Delivery;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.service.OrderDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class DeliveryDeletionService {
  private final DeliveryRepository repository;

  @EventListener
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void handleOrderDeletion(OrderDeletedEvent event) {
    Order deletedOrder = event.getOrder();
    log.info("[배달] 주문 삭제 이벤트 수신: orderId={}", deletedOrder.getId());

    try {
      Delivery delivery = repository.findByOrderIdOrNull(deletedOrder.getId());
      if (delivery == null) {
        log.info("주문과 관련된 배송이 없습니다.");
      }
      log.info("배송 삭제가 완료되었습니다={}", delivery.getId());
      delivery.delete(event.getDeletedBy());
    } catch (Exception e) {
      log.error("주문 삭제 후 배송 삭제 처리 중 오류: orderId={}, exception={}", deletedOrder.getId(), e);
    }
  }
}

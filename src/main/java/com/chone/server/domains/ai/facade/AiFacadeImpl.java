package com.chone.server.domains.ai.facade;

import com.chone.server.commons.facade.AiFacade;
import com.chone.server.domains.ai.domain.Ai;
import com.chone.server.domains.ai.repository.AiRepository;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiFacadeImpl implements AiFacade {

  private final AiRepository aiRepository;

  @Override
  public Ai saveAiRecord(Product product, User user, String requestText, String responseText) {

    Ai ai = Ai.builder(product.getStore(), product, requestText, responseText).build();
    ai.updateCreatedBy(user.getUsername());
    return aiRepository.save(ai);
  }
}

package com.chone.server.commons.facade;

import com.chone.server.domains.ai.domain.Ai;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.user.domain.User;

public interface AiFacade {

  Ai saveAiRecord(Product product, User user, String requestText, String responseText);
}

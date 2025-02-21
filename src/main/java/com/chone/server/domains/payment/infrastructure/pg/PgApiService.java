package com.chone.server.domains.payment.infrastructure.pg;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class PgApiService {

  public Map<String, String> requestPayment() {
    return new MockPgController().processPayment();
  }

  public Map<String, String> requestCancel() {
    return new MockPgController().processCancel();
  }

  class MockPgController {

    public Map<String, String> processPayment() {
      boolean isSuccess = true;
      String status = isSuccess ? "success" : "fail";
      String message =
          "저희 서비스의 %s로 결제가 %s했습니다."
              .formatted(isSuccess ? "의 정상 작동으" : "오류", isSuccess ? "성공" : "실패");

      Map<String, String> response = new HashMap<>();
      response.put("status", status);
      response.put("message", message);
      if (isSuccess) {
        response.put("transactionId", RandomStringUtils.randomAlphabetic(10));
      }
      return response;
    }

    public Map<String, String> processCancel() {
      boolean isSuccess = false;
      String status = isSuccess ? "success" : "fail";
      String message =
          "저희 서비스의 %s로 결제 취소가 %s했습니다."
              .formatted(isSuccess ? "정상 작동" : "오류", isSuccess ? "성공" : "실패");

      Map<String, String> response = new HashMap<>();
      response.put("status", status);
      response.put("message", message);

      return response;
    }
  }
}

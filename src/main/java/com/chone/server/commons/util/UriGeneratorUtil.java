package com.chone.server.commons.util;

import java.net.URI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UriGeneratorUtil {
  public static URI generateUriWithPathVariable(String path, Object... uriVariables) {
    return ServletUriComponentsBuilder.fromCurrentRequest()
        .path(path)
        .buildAndExpand(uriVariables)
        .toUri();
  }

  public static URI generateUri(String path) {
    return ServletUriComponentsBuilder.fromCurrentRequest().path(path).build().toUri();
  }
}

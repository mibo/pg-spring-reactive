package de.mirb.pg.spring.reactivespring.boundary;

import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.List;

public class RequestUtil {
  public static final String HEADER_CONSUME = "consume";
  public static final String HEADER_AMOUNT = "amount";
  public static final String HEADER_SKIP = "skip";

  static String getFirstHeader(ServerRequest request, String header, String defIfNotFound) {
    ServerRequest.Headers headers = request.headers();
    List<String> value = headers.header(header);
    if (value.isEmpty()) {
      return defIfNotFound;
    }
    return value.get(0);
  }

  static Integer getFirstHeaderAsInt(ServerRequest request, String header, Integer defIfNotFound) {
    ServerRequest.Headers headers = request.headers();
    List<String> value = headers.header(header);
    if (value.isEmpty()) {
      return defIfNotFound;
    }
    try {
      return Integer.parseInt(value.get(0));
    } catch (NumberFormatException e) {
      return null;
    }
  }

  static Boolean getFirstHeaderAsBoolean(ServerRequest request, String header, Boolean defIfNotFound) {
    ServerRequest.Headers headers = request.headers();
    List<String> headerValue = headers.header(header);
    if (headerValue.isEmpty()) {
      return defIfNotFound;
    }
    return Boolean.valueOf(headerValue.get(0));
  }

}

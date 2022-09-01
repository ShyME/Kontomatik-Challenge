package me.imshy.scraper.domain.http;

public class HttpCodeError extends RuntimeException {

  public HttpCodeError(int code) {
    super("Code: " + code);
  }

}

package me.imshy.scraper.domain.http.exception;

public class HttpCodeError extends RuntimeException {

  public HttpCodeError(int code) {
    super("Code: " + code);
  }

}

package me.imshy.bankingInfo.adapters.general.exception;

public class HttpCodeError extends RuntimeException {
  public HttpCodeError(int code) {
    super("Request failed with code: " + code);
  }
}

package me.imshy.bankingInfo.general.exception;

public class RequestError extends RuntimeException {

  public RequestError(String msg) {
    super(msg);
  }
}

package me.imshy.bankingInfo.domain.exception;

public class UnsuccessfulSignIn extends RuntimeException {

  public UnsuccessfulSignIn(String msg) {
    super(msg);
  }
}

package me.imshy.bankingInfo.domain.general.http.exception.signIn;

public class UnsuccessfulSignIn extends RuntimeException {
  public UnsuccessfulSignIn(String msg) {
    super(msg);
  }
}

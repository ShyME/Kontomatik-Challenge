package me.imshy.bankingInfo.domain.general.http.exception.signIn;

public class InvalidPassword extends RuntimeException {
  public InvalidPassword(String msg) {
    super(msg);
  }
}

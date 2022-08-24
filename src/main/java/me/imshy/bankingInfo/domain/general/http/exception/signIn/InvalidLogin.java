package me.imshy.bankingInfo.domain.general.http.exception.signIn;

public class InvalidLogin extends RuntimeException {
  public InvalidLogin(String msg) {
    super(msg);
  }
}

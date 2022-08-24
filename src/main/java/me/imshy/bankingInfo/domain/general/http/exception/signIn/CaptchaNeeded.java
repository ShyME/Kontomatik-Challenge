package me.imshy.bankingInfo.domain.general.http.exception.signIn;

public class CaptchaNeeded extends RuntimeException {
  public CaptchaNeeded(String msg) {
    super(msg);
  }
}

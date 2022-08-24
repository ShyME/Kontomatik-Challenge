package me.imshy.bankingInfo.domain.general.http.exception.signIn;

public class BlockedChannel extends RuntimeException {
  public BlockedChannel(String msg) {
    super(msg);
  }
}

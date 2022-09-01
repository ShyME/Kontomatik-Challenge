package me.imshy.scraper.domain.exception;

public class AccessBlocked extends RuntimeException {

  public AccessBlocked(String msg) {
    super(msg);
  }

}

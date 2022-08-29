package me.imshy.scraper.domain.http.exception.signIn;

public class AccessBlocked extends RuntimeException {

  public AccessBlocked(String msg) {
    super(msg);
  }

}

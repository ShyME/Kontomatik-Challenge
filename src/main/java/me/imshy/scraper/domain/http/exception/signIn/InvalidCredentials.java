package me.imshy.scraper.domain.http.exception.signIn;

public class InvalidCredentials extends RuntimeException {

  public InvalidCredentials(String msg) {
    super(msg);
  }

}

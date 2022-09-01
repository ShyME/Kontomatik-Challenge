package me.imshy.scraper.domain.exception;

public class InvalidCredentials extends RuntimeException {

  public InvalidCredentials(String msg) {
    super(msg);
  }

}

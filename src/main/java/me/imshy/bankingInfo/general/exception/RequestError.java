package me.imshy.bankingInfo.general.exception;

import java.util.List;

public class RequestError extends RuntimeException {

  public final int CODE;
  public final List<String> ERROR_DESCRIPTIONS;
  public RequestError(int CODE, List<String> ERROR_DESCRIPTIONS) {
    super("Request failed with code: " + CODE + ", Errors: " + ERROR_DESCRIPTIONS);
    this.CODE = CODE;
    this.ERROR_DESCRIPTIONS = ERROR_DESCRIPTIONS;
  }
}

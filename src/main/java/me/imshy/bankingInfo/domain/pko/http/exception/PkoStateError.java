package me.imshy.bankingInfo.domain.pko.http.exception;

import java.util.List;

public class PkoStateError extends RuntimeException {
  private final String stateId;

  public PkoStateError(String stateId, List<String> errorDescriptions) {
    super("Request resulted in error on state: " + stateId + ", with error descriptions: " + errorDescriptions);
    this.stateId = stateId;
  }

  public String getStateId() {
    return stateId;
  }
}
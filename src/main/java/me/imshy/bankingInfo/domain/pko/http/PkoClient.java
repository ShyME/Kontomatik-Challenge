package me.imshy.bankingInfo.domain.pko.http;

import me.imshy.bankingInfo.domain.general.http.HttpClient;
import me.imshy.bankingInfo.domain.general.http.request.JsonPostRequest;
import me.imshy.bankingInfo.domain.general.http.request.Response;
import me.imshy.bankingInfo.domain.pko.http.exception.PkoStateError;
import me.imshy.bankingInfo.domain.pko.http.util.Requests;

import java.util.List;

public class PkoClient {
  private final HttpClient httpClient;

  public PkoClient(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public Response fetch(JsonPostRequest jsonPostRequest) {
    Response response = httpClient.fetch(jsonPostRequest);
    assertNoErrors(response);
    return response;
  }

  private void assertNoErrors(Response response) {
    int pkoHttpStatus = response.toJson().get("httpStatus").asInt();
    List<String> errorDescriptions = Requests.getErrorDescriptions(response);
    if (!errorDescriptions.isEmpty() || pkoHttpStatus >= 400) {
      String stateId = response.toJson().get("state_id").textValue();
      throw new PkoStateError(stateId, errorDescriptions, pkoHttpStatus);
    }
  }
}

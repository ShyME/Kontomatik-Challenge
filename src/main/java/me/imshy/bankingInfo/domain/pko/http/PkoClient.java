package me.imshy.bankingInfo.domain.pko.http;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.bankingInfo.domain.general.http.HttpClient;
import me.imshy.bankingInfo.domain.general.http.request.JsonPostRequest;
import me.imshy.bankingInfo.domain.general.http.request.Response;
import me.imshy.bankingInfo.domain.pko.http.exception.PkoStateError;
import me.imshy.bankingInfo.domain.pko.http.util.Requests;
import me.imshy.bankingInfo.infrastructure.general.exception.HttpCodeError;

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
    JsonNode responseJson = response.toJson();
    int httpStatus = responseJson.get("httpStatus").asInt();
    if (httpStatus >= 400) {
      throw new HttpCodeError(httpStatus);
    }
    List<String> errorDescriptions = Requests.getErrorDescriptions(response);
    if (!errorDescriptions.isEmpty()) {
      String stateId = responseJson.get("state_id").textValue();
      throw new PkoStateError(stateId, errorDescriptions);
    }
  }
}

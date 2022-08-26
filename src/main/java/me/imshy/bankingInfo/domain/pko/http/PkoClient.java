package me.imshy.bankingInfo.domain.pko.http;

import com.fasterxml.jackson.databind.JsonNode;
import me.imshy.bankingInfo.domain.general.http.HttpClient;
import me.imshy.bankingInfo.domain.general.http.request.JsonPostRequest;
import me.imshy.bankingInfo.domain.general.http.request.Response;
import me.imshy.bankingInfo.domain.pko.http.exception.PkoStateError;
import me.imshy.bankingInfo.infrastructure.general.util.JsonUtils;

import java.util.ArrayList;
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
    List<String> errorDescriptions = getErrorDescriptions(response);
    if (!errorDescriptions.isEmpty() || pkoHttpStatus >= 400) {
      String stateId = response.toJson().get("state_id").textValue();
      throw new PkoStateError(stateId, errorDescriptions, pkoHttpStatus);
    }
  }

  private static List<String> getErrorDescriptions(Response response) {
    List<JsonNode> errorJsons = JsonUtils.findValuesByKey("errors", response.body());
    List<String> errors = new ArrayList<>();
    for (JsonNode errorNode : errorJsons) {
      if (errorNode.isArray()) {
        for (int i = 0; i < errorNode.size(); i++) {
          JsonNode element = errorNode.get(i);
          errors.addAll(getErrorDescriptions(element));
        }
      } else {
        errors.addAll(getErrorDescriptions(errorNode));
      }
    }
    return errors;
  }

  private static List<String> getErrorDescriptions(JsonNode errorNode) {
    List<String> errors = new ArrayList<>();
    errorNode.elements().forEachRemaining(element -> {
      if (!element.textValue().equals("")) {
        errors.add(element.textValue());
      }
    });
    return errors;
  }
}

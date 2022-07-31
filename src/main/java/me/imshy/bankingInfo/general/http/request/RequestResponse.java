package me.imshy.bankingInfo.general.http.request;

import me.imshy.bankingInfo.general.util.EntityUtils;
import me.imshy.bankingInfo.general.util.JsonUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;

import java.util.Optional;

public class RequestResponse {
  private final CloseableHttpResponse response;
  private final String responseJson;

  public RequestResponse(CloseableHttpResponse response) {
    this.response = response;
    this.responseJson = EntityUtils.toString(response.getEntity());
  }

  public Optional<String> getHeaderValue(String headerName) {
    if (!response.containsHeader(headerName)) {
      return Optional.empty();
    }
    return Optional.of(response.getFirstHeader(headerName).getValue());
  }

  public boolean isSuccessful() {
    if (response.getCode() >= 400) {
      return false;
    }
    return containsErrors();
  }

  public boolean containsErrors() {
    Optional<String> errors = JsonUtils.getNestedValueFromJson(new String[]{"response", "fields", "errors"}, responseJson);
    return errors.isEmpty();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RequestResponse={");
    sb.append("headers=[");
    for (Header header : response.getHeaders()) {
      sb.append(header.getName());
      sb.append(": ");
      sb.append(header.getValue());
      sb.append(",");
    }
    sb.append("],body=");
    sb.append(responseJson);
    sb.append("}");
    return sb.toString();
  }

  public String getResponseJson() {
    return responseJson;
  }
}

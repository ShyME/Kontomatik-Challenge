package me.imshy.bankingInfo.domain.general.http;

import me.imshy.bankingInfo.domain.general.http.request.JsonPostRequest;
import me.imshy.bankingInfo.domain.general.http.request.Response;

public interface HttpClient {
  Response fetch(JsonPostRequest jsonPostRequest);
}

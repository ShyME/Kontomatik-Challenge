package me.imshy.bankingInfo.adapters.general.http.client;

import me.imshy.bankingInfo.adapters.general.http.request.PostRequest;
import me.imshy.bankingInfo.adapters.general.http.request.Response;

public interface HttpClient {

  Response fetchRequest(PostRequest postRequest);
}

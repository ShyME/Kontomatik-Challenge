package me.imshy.bankingInfo.general.http.client;

import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.general.http.request.Response;

public interface HttpClient {

  Response fetchRequest(PostRequest postRequest);
}

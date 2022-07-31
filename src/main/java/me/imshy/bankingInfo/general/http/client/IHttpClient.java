package me.imshy.bankingInfo.general.http.client;

import me.imshy.bankingInfo.general.http.request.PostRequest;
import me.imshy.bankingInfo.general.http.request.RequestResponse;

import java.io.Closeable;

public interface IHttpClient extends Closeable {

  RequestResponse sendRequest(PostRequest postRequest);
}

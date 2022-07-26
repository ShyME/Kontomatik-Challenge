package me.imshy.client;

import me.imshy.request.PostRequest;
import me.imshy.request.RequestResponse;

import java.io.Closeable;

public interface IHttpClient extends Closeable {
    RequestResponse sendRequest(PostRequest postRequest);
}

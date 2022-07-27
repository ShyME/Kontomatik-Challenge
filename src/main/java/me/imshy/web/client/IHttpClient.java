package me.imshy.web.client;

import me.imshy.web.request.PostRequest;
import me.imshy.web.request.RequestResponse;

import java.io.Closeable;

public interface IHttpClient extends Closeable {
    RequestResponse sendRequest(PostRequest postRequest);
}

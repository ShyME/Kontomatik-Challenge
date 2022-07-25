package me.imshy.client;

import me.imshy.request.PostRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

import java.io.Closeable;
import java.io.IOException;

public class HttpClient implements Closeable {

    private final CloseableHttpClient client;

    public HttpClient() {
        client = HttpClientBuilder.create()
                .build();
    }

    public CloseableHttpResponse sendRequest(PostRequest postRequest) throws IOException {
        return client.execute(postRequest.getHttpPost());
    }

    @Override
    public void close() throws IOException {
        client.close();
    }
}

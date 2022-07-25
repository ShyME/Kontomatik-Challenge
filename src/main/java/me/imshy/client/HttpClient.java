package me.imshy.client;

import me.imshy.Main;
import me.imshy.request.PostRequest;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

public class HttpClient implements Closeable {
    private static final Logger LOGGER = LogManager.getLogger(HttpClient.class);

    private final CloseableHttpClient client;

    public HttpClient() {
        LOGGER.debug("Creating HttpClient");
        client = HttpClientBuilder.create()
                .build();
    }

    public CloseableHttpResponse sendRequest(PostRequest postRequest) throws IOException {
        LOGGER.debug("Sending Request: " + postRequest.toString());
        CloseableHttpResponse response = client.execute(postRequest.getHttpPost());
        LOGGER.debug("Got Response: " + response.toString());

        return response;
    }

    @Override
    public void close() throws IOException {
        LOGGER.debug("Closing HttpClient");
        client.close();
    }
}

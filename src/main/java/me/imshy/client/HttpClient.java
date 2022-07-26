package me.imshy.client;

import me.imshy.request.PostRequest;
import me.imshy.request.RequestResponse;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ParseException;
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

    public RequestResponse sendRequest(PostRequest postRequest) throws IOException, ParseException {
        LOGGER.debug("Sending Request: " + postRequest);
        CloseableHttpResponse response = client.execute(postRequest.getHttpPost());

        RequestResponse requestResponse = new RequestResponse(response);
        LOGGER.debug("Got Response: " + requestResponse);

        return requestResponse;
    }

    @Override
    public void close() throws IOException {
        LOGGER.debug("Closing HttpClient");
        client.close();
    }
}

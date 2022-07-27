package me.imshy.web.client;

import lombok.SneakyThrows;
import me.imshy.web.request.PostRequest;
import me.imshy.web.request.RequestResponse;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;

@Component
public class HttpClient implements Closeable, IHttpClient {
    private static final Logger LOGGER = LogManager.getLogger(HttpClient.class);

    private final CloseableHttpClient client;

    public HttpClient() {
        LOGGER.debug("Creating HttpClient");
        client = HttpClientBuilder.create()
                .build();
    }

    @SneakyThrows
    public RequestResponse sendRequest(PostRequest postRequest) {
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

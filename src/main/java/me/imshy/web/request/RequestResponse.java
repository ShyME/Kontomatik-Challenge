package me.imshy.web.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import me.imshy.util.JsonUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.Optional;

@Getter
public class RequestResponse {
    private final CloseableHttpResponse response;
    private final String responseJson;

    public RequestResponse(CloseableHttpResponse response) throws IOException, ParseException {
        this.response = response;
        this.responseJson = EntityUtils.toString(response.getEntity());
    }

    public Optional<String> getHeaderValue(String headerName) {
        if(!response.containsHeader(headerName)) {
            return Optional.empty();
        }
        return Optional.of(response.getFirstHeader(headerName).getValue());
    }

    public boolean isSuccessful() {
        if(getResponse().getCode() >= 400) {
            return false;
        }
        return containsErrors();
    }

    public boolean containsErrors() {
        try {
            String errors = JsonUtils.getNestedValueFromJson(new String[]{"response", "fields", "errors"}, responseJson);
            return errors == null;
        } catch (JsonProcessingException | NullPointerException e) {
            return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RequestResponse={");
        sb.append("headers=[");
        for(Header header : response.getHeaders()) {
            sb.append(header.getName());
            sb.append(": ");
            sb.append(header.getValue());
            sb.append(",");
        }
        sb.append("],body=");
        sb.append(responseJson);
        sb.append("}");
        return sb.toString();
    }
}

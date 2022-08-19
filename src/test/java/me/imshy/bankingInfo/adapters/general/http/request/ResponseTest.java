package me.imshy.bankingInfo.adapters.general.http.request;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ResponseTest {

  private final String responseBody = """
      {
      	"httpStatus": 200,
      	"state_id": "password",
      	"errors": {
          "description": "error1"
      	},
      	"test": {
      	  "errors": {
      	    "description": "error2"
      	  }
      	},
      	"response": {
      		"fields": null,
      		"errors": {
      		  "description": "error3"
      		}
      	}
      }""";

  private final Response responseWithErrors = new Response.ResponseBuilder()
      .setBody(responseBody)
      .setHeaders(Map.of("X-Session-Id", "sessionIdValue"))
      .setCode(200)
      .build();

  @Test
  void getErrorDescriptions_shouldSuccess() {
    List<String> errorDescriptions = responseWithErrors.getErrorDescriptions();
    assertThat(errorDescriptions.size()).isEqualTo(3);
    assertThat(errorDescriptions.get(2)).isEqualTo("error3");
  }
}
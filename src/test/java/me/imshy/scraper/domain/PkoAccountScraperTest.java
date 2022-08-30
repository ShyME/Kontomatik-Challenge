package me.imshy.scraper.domain;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import me.imshy.scraper.infrastructure.apache.ApacheHttpClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@WireMockTest(proxyMode = true, httpsEnabled = true)
class PkoAccountScraperTest {

  private static final String PKO_LOGIN_PATH = "/ipko3/login";
  private static final String PKO_INIT_PATH = "/ipko3/init";

  private static final String LOGIN_RESPONSE_BODY = """
      {
      	"httpStatus": 200,
      	"response": {
      		"fields": {
      			"password": {
      				"value": null,
      				"errors": null,
      				"widget": {
      					"max_len": 16,
      					"field_type": "password",
      					"required": true
      				}
      			},
      			"errors": null
      		}
      	},
      	"flow_id": "FLOW_ID",
      	"state_id": "password",
      	"token": "TOKEN"
      }
      """;
  private static final String PASSWORD_RESPONSE_BODY = """
      {
      	"httpStatus": 200,
      	"flow_id": "FLOW_ID",
      	"state_id": "END",
      	"token": "TOKEN",
      	"finished": true,
      	"response": {
      		"data": {
      			"login_type": "NORMAL"
      		}
      	}
      }
      """;

  private static final String INIT_RESPONSE_BODY = """
      {
      	"httpStatus": 200,
      	"response": {
      		"data": {
      			"accounts": {
      				"ACCOUNT_ID": {
      					"number": {
      						"value": "01234567890123456789012345"
      						},
      					"currency": "PLN",
      					"balance": "1000.00"
      				}
      			}
      		}
      	}
      }
      """;

  @Test
  void importAccounts(WireMockRuntimeInfo wireMockRuntimeInfo) {
    var pkoAccountScraper = createTestScraperWithProxy(wireMockRuntimeInfo.getHttpBaseUrl());
    defineSuccessfulLoginRequestHandlingStub();
    defineSuccessfulPasswordRequestHandlingStub();
    defineSuccessfulInitRequestHandlingStub();

    List<Account> accounts = pkoAccountScraper.importAccounts(new Credentials("LOGIN", "PASSWORD"));

    verify(postRequestedFor(anyUrl()));
    Assertions.assertThat(accounts).isNotEmpty();
  }

  private static PkoAccountScraper createTestScraperWithProxy(String baseUrl) {
    return new PkoAccountScraper(
        new ApacheHttpClient.Builder()
            .setProxy(getUri(baseUrl))
            .trustAll()
            .build()
    );
  }

  private static URI getUri(String baseUrl) {
    try {
      return new URI(baseUrl);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  private void defineSuccessfulLoginRequestHandlingStub() {
    givenThat(post(urlPathEqualTo(PKO_LOGIN_PATH))
        .withScheme("https")
        .withRequestBody(matchingJsonPath("$[?(@.action == \"submit\")]"))
        .withRequestBody(matchingJsonPath("$[?(@.state_id == \"login\")]"))
        .withRequestBody(matchingJsonPath("$.data[?(@.login == \"LOGIN\")]"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withHeader("X-Session-Id", "SESSION_ID")
            .withBody(LOGIN_RESPONSE_BODY)));
  }

  private void defineSuccessfulPasswordRequestHandlingStub() {
    givenThat(post(urlPathEqualTo(PKO_LOGIN_PATH))
        .withScheme("https")
        .withRequestBody(matchingJsonPath("$[?(@.action == \"submit\")]"))
        .withRequestBody(matchingJsonPath("$[?(@.state_id == \"password\")]"))
        .withRequestBody(matchingJsonPath("$[?(@.flow_id == \"FLOW_ID\")]"))
        .withRequestBody(matchingJsonPath("$.data[?(@.password == \"PASSWORD\")]"))
        .withHeader("X-Session-Id", equalTo("SESSION_ID"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(PASSWORD_RESPONSE_BODY)));
  }

  private static void defineSuccessfulInitRequestHandlingStub() {
    givenThat(post(urlPathEqualTo(PKO_INIT_PATH))
        .withScheme("https")
        .withRequestBody(matchingJsonPath("$.data[?(@.account_ids)]"))
        .withRequestBody(matchingJsonPath("$.data[?(@.accounts)]"))
        .withHeader("X-Session-Id", equalTo("SESSION_ID"))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(INIT_RESPONSE_BODY)));
  }

}
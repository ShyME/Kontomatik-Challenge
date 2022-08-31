package me.imshy.scraper.domain;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import me.imshy.scraper.domain.pko.http.PkoRequests;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class PkoWireMock {

  private final Credentials CREDENTIALS;

  public PkoWireMock(Credentials credentials) {
    this.CREDENTIALS = credentials;
  }

  void mockValidLogin() {
    givenThat(matchLoginRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withHeader(PkoRequests.SESSION_ID_HEADER_NAME, "SESSION_ID")
            .withBody(ResponseJsonBodies.LOGIN_RESPONSE_BODY)));
  }

  private MappingBuilder matchLoginRequest() {
    return post(urlPathEqualTo(PkoRequests.LOGIN_PATH))
        .withScheme("https")
        .withRequestBody(matchingJsonPath("$[?(@.action == \"submit\")]"))
        .withRequestBody(matchingJsonPath("$[?(@.state_id == \"login\")]"))
        .withRequestBody(matchingJsonPath("$.data[?(@.login == '" + CREDENTIALS.login() + "')]"));
  }

  void mockValidPassword() {
    givenThat(matchPasswordRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(ResponseJsonBodies.PASSWORD_RESPONSE_BODY)));
  }

  private MappingBuilder matchPasswordRequest() {
    return post(urlPathEqualTo(PkoRequests.LOGIN_PATH))
        .withScheme("https")
        .withRequestBody(matchingJsonPath("$[?(@.action == \"submit\")]"))
        .withRequestBody(matchingJsonPath("$[?(@.state_id == \"password\")]"))
        .withRequestBody(matchingJsonPath("$[?(@.flow_id == \"FLOW_ID\")]"))
        .withRequestBody(matchingJsonPath("$.data[?(@.password == '" + CREDENTIALS.password() + "')]"))
        .withHeader(PkoRequests.SESSION_ID_HEADER_NAME, equalTo("SESSION_ID"));
  }

  void mockInit() {
    givenThat(matchInitRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(ResponseJsonBodies.INIT_RESPONSE_BODY)));
  }

  private MappingBuilder matchInitRequest() {
    return post(urlPathEqualTo(PkoRequests.INIT_PATH))
        .withScheme("https")
        .withRequestBody(matchingJsonPath("$.data[?(@.account_ids)]"))
        .withRequestBody(matchingJsonPath("$.data[?(@.accounts)]"))
        .withHeader("X-Session-Id", equalTo("SESSION_ID"));
  }

  void mockLoginCaptcha() {
    givenThat(matchLoginRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withHeader("X-Session-Id", "SESSION_ID")
            .withBody(ResponseJsonBodies.CAPTCHA_LOGIN_RESPONSE_BODY)));
  }

  void mockInvalidPassword() {
    givenThat(matchPasswordRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(ResponseJsonBodies.INVALID_PASSWORD_RESPONSE_BODY)));
  }

  void mockAccountBlocked() {
    givenThat(matchPasswordRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withHeader("X-Session-Id", "SESSION_ID")
            .withBody(ResponseJsonBodies.ACCESS_BLOCKED_PASSWORD_RESPONSE_BODY)));
  }

  private static class ResponseJsonBodies {

    private static final String LOGIN_RESPONSE_BODY = """
        {
        	"httpStatus": 200,
        	"response": {
        		"fields": {
        			"password": {
        				"value": null,
        				"errors": null
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
    private static final String INVALID_PASSWORD_RESPONSE_BODY = """
        {
        	"httpStatus": 200,
        	"response": {
        		"fields": {
        			"login": {
        				"errors": null
        			},
        			"errors": {
        				"description": "Nieudane logowanie. Aby chronić Twoje finanse, zablokujemy dostęp do iPKO po kilku nieudanych próbach logowania. Nie pamiętasz hasła? Skorzystaj z opcji \\"Odzyskaj hasło\\" podczas logowania"
        			}
        		}
        	},
        	"token": "TOKEN",
        	"flow_id": "FLOW_ID",
        	"state_id": "login"
        }
        """;
    private static final String CAPTCHA_LOGIN_RESPONSE_BODY = """
        {
        	"state_id": "captcha",
        	"httpStatus": 200,
        	"token": "TOKEN",
        	"response": {
        		"fields": {
        			"errors": null,
        			"image": {
        				"errors": null
        			}
        		},
        		"data": {
        			"question": "Wybierz wszystkie obrazki, na których są ..."
        		}
        	},
        	"flow_id": "FLOW_ID"
        }
        """;
    private static final String ACCESS_BLOCKED_PASSWORD_RESPONSE_BODY = """
        {
        	"finished": true,
        	"flow_id": "FLOW_ID",
        	"httpStatus": 200,
        	"token": "TOKEN",
        	"response": {},
        	"state_id": "blocked_channel"
        }
        """;

  }

}

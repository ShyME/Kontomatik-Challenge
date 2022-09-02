package unit.me.imshy.scraper;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import me.imshy.scraper.domain.Credentials;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

class PkoWireMock {

  private static final String LOGIN_PATH = "/ipko3/login";
  private static final String SESSION_ID_HEADER_NAME = "X-Session-Id";
  public static final Credentials CREDENTIALS = new Credentials("LOGIN", "PASSWORD");

  static void mockSuccessfulImport() {
    mockValidLogin();
    mockValidPassword();
    mockInit();
  }

  static void mockInvalidCredentials() {
    mockValidLogin();
    mockInvalidPassword();
  }

  static void mockAccessBlocked() {
    mockValidLogin();
    mockAccountBlocked();
  }

  static void mockLoginCaptcha() {
    givenThat(matchLoginRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withHeader("X-Session-Id", "SESSION_ID")
            .withBody("""
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
                """)));
  }

  private static void mockValidLogin() {
    givenThat(matchLoginRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withHeader(SESSION_ID_HEADER_NAME, "SESSION_ID")
            .withBody("""
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
                """)));
  }

  private static MappingBuilder matchLoginRequest() {
    return post(urlPathEqualTo(LOGIN_PATH))
        .withRequestBody(equalToJson("""
            {
              "data" : {
                "login" : "LOGIN"
              },
              "action" : "submit",
              "state_id" : "login"
            }
            """));
  }

  private static void mockValidPassword() {
    givenThat(matchPasswordRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
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
                """)));
  }

  private static MappingBuilder matchPasswordRequest() {
    return post(urlPathEqualTo(LOGIN_PATH))
        .withRequestBody(equalToJson("""
            {
              "data": {
                "password" : "PASSWORD"
              },
              "action" : "submit",
              "token" : "TOKEN",
              "state_id" : "password",
              "flow_id" : "FLOW_ID"
            }
            """))
        .withHeader(SESSION_ID_HEADER_NAME, equalTo("SESSION_ID"));
  }

  private static void mockInit() {
    givenThat(matchInitRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
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
                """)));
  }

  private static MappingBuilder matchInitRequest() {
    return post(urlPathEqualTo("/ipko3/init"))
        .withRequestBody(equalToJson("""
            {
              "data" : {
                "account_ids" : { },
                "accounts" : { }
              }
            }
            """))
        .withHeader("X-Session-Id", equalTo("SESSION_ID"));
  }

  private static void mockInvalidPassword() {
    givenThat(matchPasswordRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody("""
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
                """)));
  }

  private static void mockAccountBlocked() {
    givenThat(matchPasswordRequest()
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withHeader("X-Session-Id", "SESSION_ID")
            .withBody("""
                {
                  "finished": true,
                  "flow_id": "FLOW_ID",
                  "httpStatus": 200,
                  "token": "TOKEN",
                  "response": {},
                  "state_id": "blocked_channel"
                }
                """)));
  }

}

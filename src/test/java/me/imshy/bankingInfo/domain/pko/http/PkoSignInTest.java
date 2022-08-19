package me.imshy.bankingInfo.domain.pko.http;

import me.imshy.bankingInfo.CredentialsFileReader;
import me.imshy.bankingInfo.adapters.general.http.client.HttpClient;
import me.imshy.bankingInfo.adapters.general.http.client.apache.ApacheHttpClient;
import me.imshy.bankingInfo.domain.accountDetails.Credentials;
import me.imshy.bankingInfo.domain.exception.UnsuccessfulSignIn;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PkoSignInTest {
  private final HttpClient httpClient = new ApacheHttpClient();

  @Test
  void login_shouldSuccess() {
    Credentials credentials = CredentialsFileReader.readCredentials();
    PkoSignIn pkoSignIn = new PkoSignIn(httpClient);

    pkoSignIn.login(credentials);
  }


  @Test
  void whenBadCredentials_login_shouldThrowUnsuccessfulSignIn() {
    Credentials credentials = new Credentials("25160944", "badPassword");
    PkoSignIn pkoSignIn = new PkoSignIn(httpClient);

    assertThatThrownBy(() -> {
      pkoSignIn.login(credentials);
    }).isInstanceOf(UnsuccessfulSignIn.class);
  }
}
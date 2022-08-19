package me.imshy.bankingInfo.pko;

import me.imshy.bankingInfo.CredentialsFileReader;
import me.imshy.bankingInfo.domain.pko.http.PkoSignIn;
import me.imshy.bankingInfo.domain.accountDetails.Credentials;
import me.imshy.bankingInfo.domain.exception.UnsuccessfulSignIn;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PkoSignInTest {
  private final String CREDENTIALS_FILEPATH = "credentials.txt";

  @Test
  void login_shouldSuccess() {
    Credentials credentials = CredentialsFileReader.readCredentials(CREDENTIALS_FILEPATH);
    PkoSignIn pkoSignIn = new PkoSignIn();

    pkoSignIn.login(credentials);
  }


  @Test
  void whenBadCredentials_login_shouldThrowUnsuccessfulSignIn() {
    Credentials credentials = new Credentials("25160944", "badPassword");
    PkoSignIn pkoSignIn = new PkoSignIn();

    assertThatThrownBy(() -> {
      pkoSignIn.login(credentials);
    }).isInstanceOf(UnsuccessfulSignIn.class);
  }
}
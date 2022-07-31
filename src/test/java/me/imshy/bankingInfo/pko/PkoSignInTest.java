package me.imshy.bankingInfo.pko;

import me.imshy.bankingInfo.general.accountDetails.LoginCredentials;
import me.imshy.bankingInfo.general.exception.UnsuccessfulSignIn;
import me.imshy.bankingInfo.general.loginCredentialsReader.LoginCredentialsFileReader;
import me.imshy.bankingInfo.general.loginCredentialsReader.LoginCredentialsInputStreamReader;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PkoSignInTest {
  private final String loginCredentialsFilePath = "loginCredentials.txt";

  @Test
  void login_shouldSuccess() {
    LoginCredentialsInputStreamReader loginCredentialsInputStreamReader = new LoginCredentialsFileReader(loginCredentialsFilePath);
    LoginCredentials loginCredentials = loginCredentialsInputStreamReader.readLoginCredentials();
    PkoSignIn pkoSignIn = new PkoSignIn();

    pkoSignIn.login(loginCredentials);
  }


  @Test
  void whenBadCredentials_login_shouldThrowUnsuccessfulSignIn() {
    LoginCredentials loginCredentials = new LoginCredentials("25160944", "badPassword");
    PkoSignIn pkoSignIn = new PkoSignIn();

    assertThatThrownBy(() -> {
      pkoSignIn.login(loginCredentials);
    }).isInstanceOf(UnsuccessfulSignIn.class);
  }
}
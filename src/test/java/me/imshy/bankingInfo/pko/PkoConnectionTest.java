package me.imshy.bankingInfo.pko;


import me.imshy.bankingInfo.general.accountDetails.AccountBalance;
import me.imshy.bankingInfo.general.accountDetails.LoginCredentials;
import me.imshy.bankingInfo.general.loginCredentialsReader.LoginCredentialsFileReader;
import me.imshy.bankingInfo.general.loginCredentialsReader.LoginCredentialsInputStreamReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PkoConnectionTest {
  private final String loginCredentialsFilePath = "loginCredentials.txt";

  @Test
  void getAccountBalances_shouldSuccess() {
    PkoConnection pkoConnection = createValidPkoConnection();

    List<AccountBalance> accountBalanceList = pkoConnection.getAccountBalances();
    assertThat(accountBalanceList.size()).isGreaterThan(0);
  }

  @Test
  void logout_shouldSuccess() {
    PkoConnection pkoConnection = createValidPkoConnection();

    pkoConnection.logout();
  }

  private PkoConnection createValidPkoConnection() {
    LoginCredentialsInputStreamReader loginCredentialsInputStreamReader = new LoginCredentialsFileReader(loginCredentialsFilePath);
    LoginCredentials loginCredentials = loginCredentialsInputStreamReader.readLoginCredentials();
    PkoSignIn pkoSignIn = new PkoSignIn();
    return pkoSignIn.login(loginCredentials);
  }
}
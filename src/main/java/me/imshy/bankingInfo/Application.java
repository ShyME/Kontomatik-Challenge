package me.imshy.bankingInfo;

import me.imshy.bankingInfo.general.accountDetails.AccountBalance;
import me.imshy.bankingInfo.general.accountDetails.LoginCredentials;
import me.imshy.bankingInfo.general.loginCredentialsReader.LoginCredentialsInputStreamReader;
import me.imshy.bankingInfo.general.loginCredentialsReader.LoginCredentialsStdInReader;
import me.imshy.bankingInfo.pko.PkoConnection;
import me.imshy.bankingInfo.pko.PkoSignIn;

import java.util.List;

public class Application {

  public static void main(String[] args) {
    LoginCredentialsInputStreamReader loginCredentialsInputStreamReader = new LoginCredentialsStdInReader();
    LoginCredentials loginCredentials = loginCredentialsInputStreamReader.readLoginCredentials();

    PkoSignIn pkoSignIn = new PkoSignIn();
    PkoConnection pkoConnection = pkoSignIn.login(loginCredentials);

    List<AccountBalance> accountBalanceList = pkoConnection.getAccountBalances();
    accountBalanceList.forEach(System.out::println);

    pkoConnection.logout();
  }

}

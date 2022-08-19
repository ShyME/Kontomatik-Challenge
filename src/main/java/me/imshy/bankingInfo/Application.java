package me.imshy.bankingInfo;

import me.imshy.bankingInfo.general.accountDetails.Account;
import me.imshy.bankingInfo.general.accountDetails.Credentials;
import me.imshy.bankingInfo.general.credentialsReader.CredentialsStdInReader;
import me.imshy.bankingInfo.pko.PkoConnection;
import me.imshy.bankingInfo.pko.PkoSignIn;

import java.util.List;

public class Application {

  public static void main(String[] args) {
    Credentials credentials = CredentialsStdInReader.readCredentials();

    PkoSignIn pkoSignIn = new PkoSignIn();
    PkoConnection pkoConnection = pkoSignIn.login(credentials);

    List<Account> accountList = pkoConnection.getAccountBalances();
    accountList.forEach(System.out::println);

    pkoConnection.logout();
  }

}

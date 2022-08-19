package me.imshy.bankingInfo;

import me.imshy.bankingInfo.domain.accountDetails.Account;
import me.imshy.bankingInfo.domain.accountDetails.Credentials;
import me.imshy.bankingInfo.adapters.general.credentialsReader.CredentialsStdInReader;
import me.imshy.bankingInfo.domain.pko.PkoAccountInfoProvider;

import java.util.List;

public class Application {

  public static void main(String[] args) {
    Credentials credentials = CredentialsStdInReader.readCredentials();

    PkoAccountInfoProvider pkoAccountInfoProvider = new PkoAccountInfoProvider(credentials);
    List<Account> accounts = pkoAccountInfoProvider.getAccountsInfo();

    accounts.forEach(System.out::println);
  }

}

package me.imshy.bankingInfo;

import me.imshy.bankingInfo.adapters.general.http.client.HttpClient;
import me.imshy.bankingInfo.adapters.general.http.client.apache.ApacheHttpClient;
import me.imshy.bankingInfo.domain.accountDetails.Account;
import me.imshy.bankingInfo.domain.accountDetails.Credentials;
import me.imshy.bankingInfo.adapters.general.credentialsReader.CredentialsStdInReader;
import me.imshy.bankingInfo.domain.pko.PkoAccountInfoProvider;

import java.util.List;

public class Application {

  public static void main(String[] args) {
    Credentials credentials = CredentialsStdInReader.readCredentials();
    HttpClient httpClient = new ApacheHttpClient();

    PkoAccountInfoProvider pkoAccountInfoProvider = new PkoAccountInfoProvider(httpClient, credentials);
    List<Account> accounts = pkoAccountInfoProvider.getAccountsInfo();

    accounts.forEach(System.out::println);
  }

}

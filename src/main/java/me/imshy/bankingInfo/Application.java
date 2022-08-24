package me.imshy.bankingInfo;

import me.imshy.bankingInfo.adapters.general.http.client.apache.ApacheHttpClient;
import me.imshy.bankingInfo.domain.general.accountDetails.Account;
import me.imshy.bankingInfo.domain.general.accountDetails.Credentials;
import me.imshy.bankingInfo.domain.pko.PkoAccountScraper;
import me.imshy.bankingInfo.domain.pko.http.PkoClient;
import me.imshy.bankingInfo.domain.pko.http.PkoSession;

import java.util.List;
import java.util.Scanner;

public class Application {
  public static void main(String[] args) {
    Credentials credentials = Application.readCredentials();
    var pkoClient = new PkoClient(new ApacheHttpClient());
    var pkoAccountScraper = new PkoAccountScraper(pkoClient);
    PkoSession pkoSession = pkoAccountScraper.openSession(credentials);
    List<Account> accounts = pkoSession.importAccounts();
    accounts.forEach(System.out::println);
  }

  private static Credentials readCredentials() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("PKO Sign In");
    System.out.print("Login: ");
    String login = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();
    return new Credentials(login, password);
  }

}

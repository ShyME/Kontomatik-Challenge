package me.imshy.scraper;

import me.imshy.scraper.domain.Account;
import me.imshy.scraper.domain.Credentials;
import me.imshy.scraper.domain.PkoAccountScraper;
import me.imshy.scraper.infrastructure.apache.ApacheHttpClient;

import java.util.List;
import java.util.Scanner;

public class Application {

  public static void main(String... args) {
    PkoAccountScraper pkoAccountScraper = createPkoAccountScraper();
    List<Account> accounts = pkoAccountScraper.importAccounts(readCredentials());
    accounts.forEach(System.out::println);
  }

  private static PkoAccountScraper createPkoAccountScraper() {
    return new PkoAccountScraper(new ApacheHttpClient());
  }

  private static Credentials readCredentials() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("PKO Sign In");
    System.out.print("Login: ");
    String login = scanner.nextLine();
    System.out.print("Password: ");
    String password = scanner.nextLine();
    scanner.close();
    return new Credentials(login, password);
  }

}

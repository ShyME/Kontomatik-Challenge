package me.imshy.scraper.domain;

import me.imshy.scraper.domain.http.HttpClient;
import me.imshy.scraper.domain.pko.PkoSession;
import me.imshy.scraper.domain.pko.PkoSignIn;

import java.util.List;

public class PkoAccountScraper {

  private final PkoSignIn pkoSignIn;

  public PkoAccountScraper(HttpClient httpClient) {
    pkoSignIn = new PkoSignIn(httpClient);
  }

  public List<Account> importAccounts(Credentials credentials) {
    PkoSession pkoSession = pkoSignIn.signIn(credentials);
    return pkoSession.importAccounts();
  }

}

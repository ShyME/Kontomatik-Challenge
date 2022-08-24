package me.imshy.bankingInfo.domain.pko;

import me.imshy.bankingInfo.domain.general.accountDetails.Credentials;
import me.imshy.bankingInfo.domain.pko.http.PkoClient;
import me.imshy.bankingInfo.domain.pko.http.PkoSession;
import me.imshy.bankingInfo.domain.pko.http.PkoSignIn;

public class PkoAccountScraper {
  private final PkoSignIn pkoSignIn;

  public PkoAccountScraper(PkoClient pkoClient) {
    pkoSignIn = new PkoSignIn(pkoClient);
  }

  public PkoSession openSession(Credentials credentials) {
    return pkoSignIn.signIn(credentials);
  }
}

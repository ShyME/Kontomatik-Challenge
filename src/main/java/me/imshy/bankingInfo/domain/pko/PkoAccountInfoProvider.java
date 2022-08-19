package me.imshy.bankingInfo.domain.pko;

import me.imshy.bankingInfo.domain.accountDetails.Account;
import me.imshy.bankingInfo.domain.accountDetails.Credentials;
import me.imshy.bankingInfo.domain.pko.http.PkoConnection;
import me.imshy.bankingInfo.domain.pko.http.PkoSignIn;

import java.util.List;

public class PkoAccountInfoProvider {
  private final PkoConnection pkoConnection;

  public PkoAccountInfoProvider(Credentials credentials) {
    PkoSignIn pkoSignIn = new PkoSignIn();
    pkoConnection = pkoSignIn.login(credentials);
  }

  public List<Account> getAccountsInfo() {
    return pkoConnection.getAccountBalances();
  }
}

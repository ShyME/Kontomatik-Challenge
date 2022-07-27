package me.imshy.bankingInfo;

import me.imshy.accountDetails.AccountBalance;
import me.imshy.accountDetails.LoginCredentials;

import java.util.List;

public interface BankingInfoProvider {

    List<AccountBalance> getAccountBalances(LoginCredentials loginCredentials);
}

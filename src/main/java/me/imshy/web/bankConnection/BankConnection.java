package me.imshy.web.bankConnection;

import me.imshy.accountDetails.AccountBalance;
import me.imshy.exception.RequestErrorException;
import me.imshy.accountDetails.LoginCredentials;
import me.imshy.exception.UnsuccessfulSignInException;

import java.util.List;

public interface BankConnection {

    List<AccountBalance> getAccountBalances() throws RequestErrorException;
    void login(LoginCredentials loginCredentials) throws UnsuccessfulSignInException;
    void logout() throws RequestErrorException;
}

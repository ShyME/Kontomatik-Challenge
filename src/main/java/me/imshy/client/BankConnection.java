package me.imshy.client;

import me.imshy.account.AccountBalance;
import me.imshy.exception.SessionExpiredException;
import me.imshy.exception.UnsuccessfulSignInException;
import me.imshy.loginCredentials.LoginCredentials;

import java.io.Closeable;
import java.util.List;

public abstract class BankConnection implements Closeable {

    protected final HttpClient httpClient;

    protected BankConnection(LoginCredentials loginCredentials) throws UnsuccessfulSignInException {
        httpClient = new HttpClient();
        login(loginCredentials);
    }

    public abstract List<AccountBalance> getAccountBalances() throws SessionExpiredException;
    public abstract void login(LoginCredentials loginCredentials) throws UnsuccessfulSignInException;
}

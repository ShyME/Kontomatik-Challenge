package me.imshy.bankConnection;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import me.imshy.account.AccountBalance;
import me.imshy.client.HttpClient;
import me.imshy.client.IHttpClient;
import me.imshy.exception.RequestErrorException;
import me.imshy.loginCredentials.LoginCredentials;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public abstract class BankConnection implements Closeable {

    protected final IHttpClient httpClient;

    protected BankConnection(LoginCredentials loginCredentials) throws RequestErrorException {
        httpClient = new HttpClient();
        login(loginCredentials);
    }

    public abstract List<AccountBalance> getAccountBalances() throws RequestErrorException;
    public abstract void login(LoginCredentials loginCredentials) throws RequestErrorException;
    public abstract void logout() throws RequestErrorException;

    @SneakyThrows(RequestErrorException.class)
    @Override
    public void close() throws IOException {
        logout();
        httpClient.close();
    }
}

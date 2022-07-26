package me.imshy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.imshy.Main;
import me.imshy.exception.SessionExpiredException;
import me.imshy.exception.UnsuccessfulSignInException;
import me.imshy.loginCredentials.LoginCredentials;
import me.imshy.account.AccountBalance;
import me.imshy.request.PostRequest;
import me.imshy.request.RequestResponse;
import me.imshy.request.SessionPostRequest;
import me.imshy.request.body.*;
import me.imshy.util.JsonUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PkoConnection extends BankConnection {
    private static final Logger LOGGER = LogManager.getLogger(PkoConnection.class);

    private final String LOGIN_URL = "https://www.ipko.pl/ipko3/login";
    private final String INIT_URL = "https://www.ipko.pl/ipko3/init";

    private String sessionId;
    private String flowId;
    private String token;

    public PkoConnection(LoginCredentials loginCredentials) throws UnsuccessfulSignInException {
        super(loginCredentials);
    }

    @Override
    public void login(LoginCredentials loginCredentials) throws UnsuccessfulSignInException {
        try {
            RequestBody loginRequestBody = new LoginRequestBody(loginCredentials.getLogin());
            PostRequest loginRequest = new PostRequest(LOGIN_URL, loginRequestBody);

            RequestResponse loginResponse = httpClient.sendRequest(loginRequest);
            if(!loginResponse.isSuccessful()) {
                throw new UnsuccessfulSignInException("Bad sign in credentials");
            }

            Optional<String> sessionIdOptional = loginResponse.getHeaderValue("X-Session-Id");
            if(sessionIdOptional.isEmpty()) {
                throw new UnsuccessfulSignInException("Response lacks session id");
            }
            sessionId = sessionIdOptional.get();

            flowId = JsonUtils.getValueFromJson("flow_id", loginResponse.getResponseJson()).replace("\"", "");
            token = JsonUtils.getValueFromJson("token", loginResponse.getResponseJson()).replace("\"", "");

            RequestBody passwordRequestBody = new PasswordRequestBody(loginCredentials.getPassword(), token, flowId);
            PostRequest passwordRequest = new SessionPostRequest(LOGIN_URL, passwordRequestBody, sessionId);

            RequestResponse passwordResponse = httpClient.sendRequest(passwordRequest);
            if(!passwordResponse.isSuccessful()) {
                throw new UnsuccessfulSignInException("Bad sign in credentials");
            }
            return;
        } catch (IOException | ParseException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        throw new UnsuccessfulSignInException("Unsuccessfull Sign In");
    }

    @Override
    public List<AccountBalance> getAccountBalances() throws SessionExpiredException {
        try {
            RequestBody initRequestBody = new InitRequestBody();
            PostRequest initRequest = new SessionPostRequest(INIT_URL, initRequestBody, sessionId);

            RequestResponse initResponse = httpClient.sendRequest(initRequest);

            if(!initResponse.isSuccessful()) {
                throw new SessionExpiredException("Session expired");
            }

            return parseAccountBalances(initResponse.getResponseJson());
        } catch (ParseException | IOException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        throw new RuntimeException("Init Request went wrong");
    }

    private List<AccountBalance> parseAccountBalances(String initResponseJson) {
        List<AccountBalance> accountBalances = new ArrayList<>(1);

        try {
            String accountIds = JsonUtils.getValueFromJson("account_ids",  initResponseJson);

            List<String> accounts = JsonUtils.parseFlatStringArray(accountIds);
            String accountsJson = JsonUtils.getValueFromJson("accounts",  initResponseJson);

            for(String accountId : accounts) {
                String currency = JsonUtils.getValueFromJson("currency",  accountsJson).replace("\"", "");
                String balance = JsonUtils.getValueFromJson("ledger",  accountsJson).replace("\"", "");

                AccountBalance accountBalance = AccountBalance.builder()
                        .accountId(accountId)
                        .currency(currency)
                        .balance(new BigDecimal(balance))
                        .build();

                accountBalances.add(accountBalance);
            }
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getLocalizedMessage());
        }

        return accountBalances;
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}

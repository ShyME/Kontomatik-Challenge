package me.imshy.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.imshy.loginCredentials.LoginCredentials;
import me.imshy.account.AccountBalance;
import me.imshy.request.PostRequest;
import me.imshy.request.SessionPostRequest;
import me.imshy.request.body.*;
import me.imshy.util.JsonUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PkoConnection implements BankConnection, Closeable {
    private final String loginUrl = "https://www.ipko.pl/ipko3/login";
    private final String initUrl = "https://www.ipko.pl/ipko3/init";

    private final HttpClient httpClient;

    private String sessionId;
    private String flowId;
    private String token;



    public PkoConnection(LoginCredentials loginCredentials) {
        httpClient = new HttpClient();

        login(loginCredentials);
    }

    public void login(LoginCredentials loginCredentials) {
        try {
            RequestBody loginRequestBody = new LoginRequestBody(loginCredentials.getLogin());
            PostRequest loginRequest = new PostRequest(loginUrl, loginRequestBody);

            CloseableHttpResponse loginResponse = httpClient.sendRequest(loginRequest);
            System.out.println(loginResponse.getCode());
            String loginResponseJson = EntityUtils.toString(loginResponse.getEntity());
            System.out.println(loginResponseJson);

            sessionId = loginResponse.getFirstHeader("X-Session-Id").getValue();
            flowId = JsonUtils.getValueFromJson("flow_id", loginResponseJson).replace("\"", "");
            token = JsonUtils.getValueFromJson("token", loginResponseJson).replace("\"", "");


            RequestBody passwordRequestBody = new PasswordRequestBody(loginCredentials.getPassword(), token, flowId);
            PostRequest passwordRequest = new SessionPostRequest(loginUrl, passwordRequestBody, sessionId);

            System.out.println("Password request body");
            System.out.println(JsonUtils.getJson(passwordRequestBody));

            CloseableHttpResponse passwordResponse = httpClient.sendRequest(passwordRequest);
            System.out.println(passwordResponse.getCode());

            String passwordResponseJson = EntityUtils.toString(passwordResponse.getEntity());
            System.out.println(passwordResponseJson);


        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AccountBalance> getAccountBalances() {
        try {
            RequestBody initRequestBody = new InitRequestBody();
            PostRequest initRequest = new SessionPostRequest(initUrl, initRequestBody, sessionId);

            System.out.println("TU");
            System.out.println(EntityUtils.toString(initRequest.getHttpPost().getEntity()));

            CloseableHttpResponse initResponse = httpClient.sendRequest(initRequest);
            System.out.println(initResponse.getCode());

            String initResponseJson = EntityUtils.toString(initResponse.getEntity());
            System.out.println(initResponseJson);

            return parseAccountBalances(initResponseJson);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<AccountBalance> parseAccountBalances(String initResponseJson) {
        List<AccountBalance> accountBalances = new ArrayList<>(1);

        try {
            String accountIds = JsonUtils.getNestedValueFromJson(new String[]{"response", "data", "account_ids"},  initResponseJson);

            List<String> accounts = JsonUtils.parseFlatStringArray(accountIds);
            for(String accountId : accounts) {
                String currency = JsonUtils.getNestedValueFromJson(new String[]{"response", "data", "accounts", accountId, "currency"},  initResponseJson).replace("\"", "");
                String balance = JsonUtils.getNestedValueFromJson(new String[]{"response", "data", "accounts", accountId, "ledger"},  initResponseJson).replace("\"", "");

                AccountBalance accountBalance = AccountBalance.builder()
                        .accountId(accountId)
                        .currency(currency)
                        .balance(new BigDecimal(balance))
                        .build();

                accountBalances.add(accountBalance);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return accountBalances;
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}

package me.imshy.web.bankConnection.pko;

import me.imshy.util.JsonUtils;
import me.imshy.web.bankConnection.BankConnection;
import me.imshy.web.bankConnection.pko.util.ResponseParserUtils;
import me.imshy.exception.RequestErrorException;
import me.imshy.exception.UnsuccessfulSignInException;
import me.imshy.accountDetails.LoginCredentials;
import me.imshy.accountDetails.AccountBalance;
import me.imshy.web.client.IHttpClient;
import me.imshy.web.request.PostRequest;
import me.imshy.web.request.RequestResponse;
import me.imshy.web.request.body.InitRequestBody;
import me.imshy.web.request.body.LoginRequestBody;
import me.imshy.web.request.body.LogoutRequestBody;
import me.imshy.web.request.body.PasswordRequestBody;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class PkoConnection implements BankConnection {
    private static final Logger LOGGER = LogManager.getLogger(PkoConnection.class);

    private final String LOGIN_URL = "https://www.ipko.pl/ipko3/login";
    private final String INIT_URL = "https://www.ipko.pl/ipko3/init";
    private final String LOGOUT_URL ="https://www.ipko.pl/ipko3/logout";

    private final IHttpClient httpClient;
    private SessionAttributes sessionAttributes;

    public PkoConnection(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public void login(LoginCredentials loginCredentials) throws UnsuccessfulSignInException {
        try {
            executeLoginRequest(loginCredentials.getLogin());
            executePasswordRequest(loginCredentials.getPassword());

        } catch (IOException | ParseException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public List<AccountBalance> getAccountBalances() throws RequestErrorException {
        try {
            RequestResponse initResponse = executeInitRequest();

            return ResponseParserUtils.parseAccountBalances(initResponse.getResponseJson());
        } catch (ParseException | IOException | RequestErrorException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void logout() throws RequestErrorException {
        try {
            executeLogoutRequest();
        } catch (IOException | ParseException | RequestErrorException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private RequestResponse executeLoginRequest(String login) throws UnsuccessfulSignInException, IOException, ParseException {
        PostRequest loginRequest = new PostRequest.PostRequestBuilder(LOGIN_URL)
                .requestBody(new StringEntity(JsonUtils.getJson(new LoginRequestBody(login))))
                .build();
        RequestResponse loginResponse = httpClient.sendRequest(loginRequest);
        if(!loginResponse.isSuccessful()) {
            throw new UnsuccessfulSignInException("Unsuccessful Sign In");
        }

        sessionAttributes = ResponseParserUtils.parseSessionAttributes(loginResponse);

        return loginResponse;
    }

    private RequestResponse executePasswordRequest(String password) throws IOException, ParseException, UnsuccessfulSignInException {
        PostRequest passwordRequest = new PostRequest.PostRequestBuilder(LOGIN_URL)
                .requestBody(new StringEntity(JsonUtils.getJson(new PasswordRequestBody(password, sessionAttributes))))
                .addHeader("x-session-id", sessionAttributes.getSessionId())
                .build();
        RequestResponse passwordResponse = httpClient.sendRequest(passwordRequest);
        if(!passwordResponse.isSuccessful()) {
            throw new UnsuccessfulSignInException("Unsuccessful Sign In");
        }

        return passwordResponse;
    }

    private RequestResponse executeLogoutRequest() throws IOException, ParseException, RequestErrorException {
        PostRequest logoutRequest = new PostRequest.PostRequestBuilder(LOGOUT_URL)
                .requestBody(new StringEntity(JsonUtils.getJson(new LogoutRequestBody())))
                .addHeader("x-session-id", sessionAttributes.getSessionId())
                .build();
        RequestResponse logoutResponse = httpClient.sendRequest(logoutRequest);

        if(!logoutResponse.isSuccessful()) {
            throw new RequestErrorException("Bad logoutRequest");
        }

        return logoutResponse;
    }

    private RequestResponse executeInitRequest() throws IOException, ParseException, RequestErrorException {
        PostRequest initRequest = new PostRequest.PostRequestBuilder(INIT_URL)
                .requestBody(new StringEntity(JsonUtils.getJson(new InitRequestBody())))
                .addHeader("x-session-id", sessionAttributes.getSessionId())
                .build();
        RequestResponse initResponse = httpClient.sendRequest(initRequest);

        if(!initResponse.isSuccessful()) {
            throw new RequestErrorException("Bad initRequest");
        }

        return initResponse;
    }
}

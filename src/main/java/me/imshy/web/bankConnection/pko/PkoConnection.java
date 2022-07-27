package me.imshy.web.bankConnection.pko;

import me.imshy.util.JsonUtils;
import me.imshy.web.bankConnection.BankConnection;
import me.imshy.web.bankConnection.pko.util.RequestCreator;
import me.imshy.web.bankConnection.pko.util.ResponseParserUtils;
import me.imshy.exception.RequestErrorException;
import me.imshy.exception.UnsuccessfulSignInException;
import me.imshy.accountDetails.LoginCredentials;
import me.imshy.accountDetails.AccountBalance;
import me.imshy.web.client.IHttpClient;
import me.imshy.web.request.PostRequest;
import me.imshy.web.request.RequestResponse;
import org.apache.hc.core5.http.ParseException;
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
    private final ResponseParserUtils responseParserUtils;

    private SessionAttributes sessionAttributes;

    public PkoConnection(IHttpClient httpClient, ResponseParserUtils responseParserUtils) {
        this.httpClient = httpClient;
        this.responseParserUtils = responseParserUtils;
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

            return responseParserUtils.parseAccountBalances(initResponse.getResponseJson());
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
        PostRequest loginRequest = RequestCreator.getLoginRequest(LOGIN_URL, login);
        RequestResponse loginResponse = httpClient.sendRequest(loginRequest);
        if(!loginResponse.isSuccessful()) {
            throw new UnsuccessfulSignInException("Unsuccessful Sign In");
        }

        sessionAttributes = responseParserUtils.parseSessionAttributes(loginResponse);

        return loginResponse;
    }

    private RequestResponse executePasswordRequest(String password) throws IOException, ParseException, UnsuccessfulSignInException {
        PostRequest passwordRequest = RequestCreator.getPasswordRequest(LOGIN_URL, password, sessionAttributes);
        RequestResponse passwordResponse = httpClient.sendRequest(passwordRequest);
        if(!passwordResponse.isSuccessful()) {
            throw new UnsuccessfulSignInException("Unsuccessful Sign In");
        }

        return passwordResponse;
    }

    private RequestResponse executeLogoutRequest() throws IOException, ParseException, RequestErrorException {
        PostRequest logoutRequest = RequestCreator.getLogoutRequest(LOGOUT_URL, sessionAttributes.getSessionId());
        RequestResponse logoutResponse = httpClient.sendRequest(logoutRequest);

        if(!logoutResponse.isSuccessful()) {
            throw new RequestErrorException("Bad logoutRequest");
        }

        return logoutResponse;
    }

    private RequestResponse executeInitRequest() throws IOException, ParseException, RequestErrorException {
        PostRequest initRequest = RequestCreator.getInitRequest(INIT_URL, sessionAttributes.getSessionId());
        RequestResponse initResponse = httpClient.sendRequest(initRequest);

        if(!initResponse.isSuccessful()) {
            throw new RequestErrorException("Bad initRequest");
        }

        return initResponse;
    }
}

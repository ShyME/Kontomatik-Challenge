package me.imshy.bankConnection.pko;

import me.imshy.bankConnection.BankConnection;
import me.imshy.bankConnection.pko.util.ResponseParserUtils;
import me.imshy.exception.RequestErrorException;
import me.imshy.exception.UnsuccessfulSignInException;
import me.imshy.loginCredentials.LoginCredentials;
import me.imshy.account.AccountBalance;
import me.imshy.request.*;
import me.imshy.request.init.InitRequest;
import me.imshy.request.login.LoginRequest;
import me.imshy.request.logout.LogoutRequest;
import me.imshy.request.password.PasswordRequest;
import org.apache.hc.core5.http.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class PkoConnection extends BankConnection {
    private static final Logger LOGGER = LogManager.getLogger(PkoConnection.class);

    private final String LOGIN_URL = "https://www.ipko.pl/ipko3/login";
    private final String INIT_URL = "https://www.ipko.pl/ipko3/init";
    private final String LOGOUT_URL ="https://www.ipko.pl/ipko3/logout";

    private SessionAttributes sessionAttributes;

    public PkoConnection() {
        super();
    }

    @Override
    public void login(LoginCredentials loginCredentials) throws RequestErrorException {
        try {
            executeLoginRequest(loginCredentials.getLogin());
            executePasswordRequest(loginCredentials.getPassword());

            return;
        } catch (IOException | ParseException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        throw new UnsuccessfulSignInException("Unsuccessful Sign In");
    }

    @Override
    public List<AccountBalance> getAccountBalances() throws RequestErrorException {
        try {
            RequestResponse initResponse = executeInitRequest();

            return ResponseParserUtils.parseAccountBalances(initResponse.getResponseJson());
        } catch (ParseException | IOException | RequestErrorException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        throw new RequestErrorException("Bad initRequest");
    }

    @Override
    public void logout() throws RequestErrorException {
        try {
            executeLogoutRequest();

            return;
        } catch (IOException | ParseException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        throw new RequestErrorException("Bad logoutRequest");
    }

    private RequestResponse executeLoginRequest(String login) throws RequestErrorException, IOException, ParseException {
        PostRequest loginRequest = new LoginRequest(LOGIN_URL, login);
        RequestResponse loginResponse = httpClient.sendRequest(loginRequest);
        if(!loginResponse.isSuccessful()) {
            throw new UnsuccessfulSignInException("Unsuccessful Sign In");
        }

        sessionAttributes = ResponseParserUtils.parseSessionAttributes(loginResponse);

        return loginResponse;
    }

    private RequestResponse executePasswordRequest(String password) throws IOException, ParseException, RequestErrorException {
        PostRequest passwordRequest = new PasswordRequest(LOGIN_URL, password, sessionAttributes);
        RequestResponse passwordResponse = httpClient.sendRequest(passwordRequest);
        if(!passwordResponse.isSuccessful()) {
            throw new UnsuccessfulSignInException("Unsuccessful Sign In");
        }

        return passwordResponse;
    }

    private RequestResponse executeLogoutRequest() throws IOException, ParseException, RequestErrorException {
        PostRequest logoutRequest = new LogoutRequest(LOGOUT_URL, sessionAttributes.getSessionId());
        RequestResponse logoutResponse = httpClient.sendRequest(logoutRequest);

        if(!logoutResponse.isSuccessful()) {
            throw new RequestErrorException("Bad logoutRequest");
        }

        return logoutResponse;
    }

    private RequestResponse executeInitRequest() throws IOException, ParseException, RequestErrorException {
        PostRequest initRequest = new InitRequest(INIT_URL, sessionAttributes.getSessionId());
        RequestResponse initResponse = httpClient.sendRequest(initRequest);

        if(!initResponse.isSuccessful()) {
            throw new RequestErrorException("Bad initRequest");
        }

        return initResponse;
    }
}

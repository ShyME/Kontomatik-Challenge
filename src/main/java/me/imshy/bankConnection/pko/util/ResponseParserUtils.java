package me.imshy.bankConnection.pko.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.imshy.account.AccountBalance;
import me.imshy.bankConnection.pko.PkoConnection;
import me.imshy.bankConnection.pko.SessionAttributes;
import me.imshy.exception.UnsuccessfulSignInException;
import me.imshy.request.RequestResponse;
import me.imshy.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponseParserUtils {
    private static final Logger LOGGER = LogManager.getLogger(ResponseParserUtils.class);

    public static SessionAttributes parseSessionAttributes(RequestResponse loginResponse) throws UnsuccessfulSignInException, JsonProcessingException {
        Optional<String> sessionIdOptional = loginResponse.getHeaderValue("X-Session-Id");
        if(sessionIdOptional.isEmpty()) {
            throw new UnsuccessfulSignInException("Response lacks session id");
        }
        String sessionId = sessionIdOptional.get();

        String flowId = JsonUtils.getValueFromJson("flow_id", loginResponse.getResponseJson()).replace("\"", "");
        String token = JsonUtils.getValueFromJson("token", loginResponse.getResponseJson()).replace("\"", "");

        return new SessionAttributes(
                sessionId,
                flowId,
                token
        );
    }

    public static List<AccountBalance> parseAccountBalances(String initResponseJson) {
        List<AccountBalance> accountBalances = new ArrayList<>(1);

        try {
            String accountIds = JsonUtils.getValueFromJson("account_ids",  initResponseJson);

            List<String> accounts = JsonUtils.parseFlatStringArray(accountIds);
            String accountsJson = JsonUtils.getValueFromJson("accounts",  initResponseJson);

            for(String accountId : accounts) {
                String currency = JsonUtils.getValueFromJson("currency",  accountsJson).replace("\"", "");
                String balance = JsonUtils.getValueFromJson("ledger",  accountsJson).replace("\"", "");

                AccountBalance accountBalance = new AccountBalance(
                        accountId,
                        currency,
                        new BigDecimal(balance)
                );

                accountBalances.add(accountBalance);
            }
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getLocalizedMessage());
        }

        return accountBalances;
    }
}

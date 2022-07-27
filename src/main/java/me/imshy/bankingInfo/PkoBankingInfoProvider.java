package me.imshy.bankingInfo;

import me.imshy.accountDetails.AccountBalance;
import me.imshy.accountDetails.LoginCredentials;
import me.imshy.exception.RequestErrorException;
import me.imshy.web.bankConnection.pko.PkoConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PkoBankingInfoProvider implements BankingInfoProvider {
    private static final Logger LOGGER = LogManager.getLogger(PkoBankingInfoProvider.class);

    private final PkoConnection pkoConnection;

    public PkoBankingInfoProvider(PkoConnection pkoConnection) {
        this.pkoConnection = pkoConnection;
    }

    @Override
    public List<AccountBalance> getAccountBalances(LoginCredentials loginCredentials) {
        List<AccountBalance> accountBalanceList = null;
        try {
            pkoConnection.login(loginCredentials);
            accountBalanceList = pkoConnection.getAccountBalances();
            pkoConnection.logout();
        } catch (RequestErrorException e) {
            LOGGER.error(e.getMessage());
        }
        return accountBalanceList;
    }
}

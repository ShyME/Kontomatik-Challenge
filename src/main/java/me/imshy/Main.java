package me.imshy;

import me.imshy.account.AccountBalance;
import me.imshy.client.BankConnection;
import me.imshy.client.PkoConnection;
import me.imshy.loginCredentials.LoginCredentialsReader;
import me.imshy.loginCredentials.LoginCredentialsStdInReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.debug("Starting App");
        LoginCredentialsReader loginCredentialsReader = new LoginCredentialsStdInReader();

        try(BankConnection pkoConnection = new PkoConnection(loginCredentialsReader.readLoginCredentials())) {
            LOGGER.info("Got Accounts: " + pkoConnection.getAccountBalances());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

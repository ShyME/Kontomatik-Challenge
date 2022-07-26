package me.imshy;

import me.imshy.bankConnection.BankConnection;
import me.imshy.bankConnection.pko.PkoConnection;
import me.imshy.exception.RequestErrorException;
import me.imshy.loginCredentials.LoginCredentialsReader;
import me.imshy.loginCredentials.LoginCredentialsStdInReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.debug("Starting App");
        LoginCredentialsReader loginCredentialsReader = new LoginCredentialsStdInReader();

        try(BankConnection pkoConnection = new PkoConnection(loginCredentialsReader.readLoginCredentials())) {
            LOGGER.info("Got Accounts: " + pkoConnection.getAccountBalances());

        } catch (IOException | IllegalArgumentException | IllegalStateException | RequestErrorException e) {
            LOGGER.error(e.getLocalizedMessage());
        }
    }
}

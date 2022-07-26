package me.imshy;

import me.imshy.bankConnection.BankConnection;
import me.imshy.bankConnection.pko.PkoConnection;
import me.imshy.exception.RequestErrorException;
import me.imshy.loginCredentials.LoginCredentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class Application {
    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    private final LoginCredentials loginCredentials;

    public Application(LoginCredentials loginCredentials) {
        this.loginCredentials = loginCredentials;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner runBankConnection() {
        return args -> {
            try(BankConnection pkoConnection = new PkoConnection(loginCredentials)) {
                LOGGER.info("Got Accounts: " + pkoConnection.getAccountBalances());
            } catch (IOException | IllegalArgumentException | IllegalStateException | RequestErrorException e) {
                LOGGER.error(e.getLocalizedMessage());
            }
        };
    }
}

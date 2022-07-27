package me.imshy;

import me.imshy.accountDetails.LoginCredentials;
import me.imshy.bankingInfo.PkoBankingInfoProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    private final PkoBankingInfoProvider pkoBankingInfoProvider;
    private final LoginCredentials loginCredentials;

    public Application(PkoBankingInfoProvider pkoBankingInfoProvider, LoginCredentials loginCredentials) {
        this.pkoBankingInfoProvider = pkoBankingInfoProvider;
        this.loginCredentials = loginCredentials;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        LOGGER.info("Account Balances: " + pkoBankingInfoProvider.getAccountBalances(loginCredentials));
    }
}

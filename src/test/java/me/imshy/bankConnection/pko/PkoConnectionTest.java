package me.imshy.bankConnection.pko;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import me.imshy.account.AccountBalance;
import me.imshy.exception.RequestErrorException;
import me.imshy.exception.UnsuccessfulSignInException;
import me.imshy.loginCredentials.LoginCredentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PkoConnectionTest {
    private static final Logger LOGGER = LogManager.getLogger(PkoConnectionTest.class);

    @Autowired
    private LoginCredentials loginCredentials;

    @Autowired
    private PkoConnection pkoConnection;

    @Test
    @Order(1)
    void loginShouldSuccess() throws RequestErrorException {
        pkoConnection.login(loginCredentials);

    }

    @Test
    @Order(2)
    void getAccountBalances() throws RequestErrorException {
        List<AccountBalance> accountBalanceList = pkoConnection.getAccountBalances();
        LOGGER.info("Accounts: " + accountBalanceList);
        assertThat(accountBalanceList.size()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    void logout() throws RequestErrorException {
        pkoConnection.logout();
    }

    @Test
    @Order(4)
    void loginShouldFailBadPassword() {
        ReflectionTestUtils.setField(loginCredentials, "password", "badPassword");
        assertThatThrownBy(() -> {
            pkoConnection.login(loginCredentials);
        }).isInstanceOf(UnsuccessfulSignInException.class);
    }

    @Test
    @Order(5)
    void loginShouldFailBadLogin() {
        ReflectionTestUtils.setField(loginCredentials, "login", "badLogin");
        assertThatThrownBy(() -> {
            pkoConnection.login(loginCredentials);
        }).isInstanceOf(UnsuccessfulSignInException.class);

    }
}
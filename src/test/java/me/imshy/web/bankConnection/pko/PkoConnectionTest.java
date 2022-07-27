package me.imshy.web.bankConnection.pko;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import me.imshy.accountDetails.AccountBalance;
import me.imshy.exception.RequestErrorException;
import me.imshy.exception.UnsuccessfulSignInException;
import me.imshy.accountDetails.LoginCredentials;
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
    @Autowired
    private LoginCredentials loginCredentials;

    @Autowired
    private PkoConnection pkoConnection;

    @Test
    @Order(1)
    void whenGoodCredentials_login_shouldSuccess() throws RequestErrorException {
        pkoConnection.login(loginCredentials);
    }

    @Test
    @Order(2)
    void whenLoggedIn_getAccountBalances_shouldSuccess() throws RequestErrorException {
        List<AccountBalance> accountBalanceList = pkoConnection.getAccountBalances();
        assertThat(accountBalanceList.size()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    void whenLoggedIn_logout_shouldSuccess() throws RequestErrorException {
        pkoConnection.logout();
    }

    @Test
    @Order(4)
    void whenBadPassword_login_shouldThrowUnsuccessfulSignInException() {
        ReflectionTestUtils.setField(loginCredentials, "password", "badPassword");
        assertThatThrownBy(() -> {
            pkoConnection.login(loginCredentials);
        }).isInstanceOf(RequestErrorException.class);
    }
}
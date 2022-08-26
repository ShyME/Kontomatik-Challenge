package me.imshy.bankingInfo;

import me.imshy.bankingInfo.domain.general.accountDetails.Credentials;
import me.imshy.bankingInfo.domain.general.http.exception.signIn.InvalidLogin;
import me.imshy.bankingInfo.domain.general.http.exception.signIn.InvalidPassword;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

class ApplicationTest {

  @BeforeEach
  void setUp() {
    StdIOStub.startCapturingOutput();
  }

  @AfterEach
  void tearDown() {
    StdIOStub.resetDoubledStreams();
  }

  @Test
  void printAccounts() {
    Credentials credentials = CredentialsFileReader.readCredentials();
    StdIOStub.input(
        credentials.login() + System.getProperty("line.separator")
            + credentials.password() + System.getProperty("line.separator")
    );

    Application.main(new String[]{});

    String output = StdIOStub.getOutput();
    Assertions.assertThat(output)
        .containsPattern("Account\\[accountNumber=\\d{26},\\s?currency=\\w+,\\sbalance=\\d+\\.\\d{2}]");
  }

  @Test
  void invalidPassword() {
    Credentials credentials = CredentialsFileReader.readCredentials();
    StdIOStub.input(
        credentials.login() + System.getProperty("line.separator")
            + "badPassword" + System.getProperty("line.separator")
    );

    Assertions.assertThatThrownBy(() -> Application.main(new String[]{}))
        .isInstanceOf(InvalidPassword.class);
  }

  @Test
  void invalidLogin() {
    StdIOStub.input(
        "*#%(*#($*#($*@*)@" + System.getProperty("line.separator")
            + "badPassword" + System.getProperty("line.separator")
    );

    Assertions.assertThatThrownBy(() -> Application.main(new String[]{}))
        .isInstanceOf(InvalidLogin.class);
  }

  private static class StdIOStub {
    private static final InputStream DEFAULT_SYSTEM_IN = System.in;
    private static final PrintStream DEFAULT_SYSTEM_OUT = System.out;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    public static void startCapturingOutput() {
      outputStream = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputStream, false, StandardCharsets.UTF_8));
    }

    public static void input(String input) {
      inputStream = new ByteArrayInputStream(input.getBytes());
      System.setIn(inputStream);
    }

    public static String getOutput() {
      return outputStream.toString();
    }

    public static void resetDoubledStreams() {
      try {
        inputStream.close();
        outputStream.close();
        System.setIn(DEFAULT_SYSTEM_IN);
        System.setOut(DEFAULT_SYSTEM_OUT);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
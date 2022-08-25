package test.consoleApi.me.imshy.bankingInfo;

import me.imshy.bankingInfo.Application;
import me.imshy.bankingInfo.domain.general.accountDetails.Credentials;
import me.imshy.bankingInfo.domain.general.http.exception.signIn.InvalidLogin;
import me.imshy.bankingInfo.domain.general.http.exception.signIn.InvalidPassword;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.CredentialsFileReader;

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
    StdIOStub.resetDoubledStreams();
    Assertions.assertThat(output)
        .contains("accountNumber")
        .contains("currency")
        .contains("balance");
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
    private static InputStream originalInputStream;
    private static PrintStream originalOutputStream;
    private static OutputStream outputStream;

    private static void startCapturingOutput() {
      originalOutputStream = System.out;
      outputStream = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputStream, false, StandardCharsets.UTF_8));
    }

    private static void input(String input) {
      originalInputStream = System.in;
      System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    private static String getOutput() {
      return outputStream.toString();
    }

    private static void resetDoubledStreams() {
      System.setIn(originalInputStream);
      System.setOut(originalOutputStream);
    }
  }
}
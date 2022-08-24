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
    StdInOutMock.startCapturingOutput();
  }

  @AfterEach
  void tearDown() {
    StdInOutMock.resetMockedStreams();
  }

  @Test
  void happyPath() {
    Credentials credentials = CredentialsFileReader.readCredentials();
    StdInOutMock.mockInput(
        credentials.login() + System.getProperty("line.separator")
            + credentials.password() + System.getProperty("line.separator")
    );

    Application.main(new String[]{});

    String output = StdInOutMock.getOutput();
    StdInOutMock.resetMockedStreams();
    Assertions.assertThat(output)
        .contains("accountNumber")
        .contains("currency")
        .contains("balance");
  }

  @Test
  void invalidPassword() {
    Credentials credentials = CredentialsFileReader.readCredentials();
    StdInOutMock.mockInput(
        credentials.login() + System.getProperty("line.separator")
            + "badPassword" + System.getProperty("line.separator")
    );

    Assertions.assertThatThrownBy(() -> Application.main(new String[]{}))
        .isInstanceOf(InvalidPassword.class);
  }

  @Test
  void invalidLogin() {
    StdInOutMock.mockInput(
        "*#%(*#($*#($*@*)@" + System.getProperty("line.separator")
            + "badPassword" + System.getProperty("line.separator")
    );

    Assertions.assertThatThrownBy(() -> Application.main(new String[]{}))
        .isInstanceOf(InvalidLogin.class);
  }


  private static class StdInOutMock {
    private static InputStream originalInputStream;
    private static PrintStream originalOutputStream;
    private static OutputStream outputStream;

    public static void startCapturingOutput() {
      originalOutputStream = System.out;
      outputStream = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputStream, false, StandardCharsets.UTF_8));
    }

    public static void mockInput(String input) {
      originalInputStream = System.in;
      System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    public static String getOutput() {
      return outputStream.toString();
    }

    private static void resetMockedStreams() {
      System.setIn(originalInputStream);
      System.setOut(originalOutputStream);
    }
  }
}
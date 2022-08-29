package me.imshy.scraper;

import me.imshy.scraper.domain.Credentials;
import me.imshy.scraper.domain.http.exception.signIn.InvalidCredentials;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

class ApplicationTest {

  @Test
  void printAccounts() {
    StdIOStub.input(CredentialsFileReader.readCredentials());

    StdIOStub.around(Application::main);

    Assertions.assertThat(StdIOStub.getOutput())
        .containsPattern("Account\\[accountNumber=\\d{26},\\s?currency=\\w+,\\sbalance=\\d+\\.\\d{2}]");
  }

  @Test
  void invalidPassword() {
    Credentials credentials = CredentialsFileReader.readCredentials();
    StdIOStub.input(new Credentials(credentials.login(), "badPassword"));

    Assertions.assertThatThrownBy(Application::main)
        .isInstanceOf(InvalidCredentials.class);
  }

  private static class StdIOStub {
    private static final InputStream DEFAULT_SYSTEM_IN = System.in;
    private static final PrintStream DEFAULT_SYSTEM_OUT = System.out;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    static void around(Runnable action) {
      StdIOStub.startCapturingOutput();
      action.run();
      StdIOStub.resetDoubledStreams();
    }

    private static void startCapturingOutput() {
      outputStream = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputStream, false, StandardCharsets.UTF_8));
    }

    private static void resetDoubledStreams() {
      try {
        inputStream.close();
        outputStream.close();
        System.setIn(DEFAULT_SYSTEM_IN);
        System.setOut(DEFAULT_SYSTEM_OUT);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    static void input(Credentials credentials) {
      String input = credentials.login() + System.getProperty("line.separator")
          + credentials.password() + System.getProperty("line.separator");
      inputStream = new ByteArrayInputStream(input.getBytes());
      System.setIn(inputStream);
    }

    public static String getOutput() {
      return outputStream.toString();
    }

  }

}
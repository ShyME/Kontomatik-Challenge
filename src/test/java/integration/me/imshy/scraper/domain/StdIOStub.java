package integration.me.imshy.scraper.domain;

import me.imshy.scraper.domain.Credentials;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StdIOStub {

  private static final InputStream DEFAULT_SYSTEM_IN = System.in;
  private static final PrintStream DEFAULT_SYSTEM_OUT = System.out;
  private static InputStream inputStream;
  private static OutputStream outputStream;

  static void input(Credentials credentials) {
    String input = credentials.login() + System.getProperty("line.separator")
      + credentials.password() + System.getProperty("line.separator");
    inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
  }

  static void around(Runnable action) {
    StdIOStub.startCapturingOutput();
    action.run();
    StdIOStub.resetStreams();
  }

  private static void startCapturingOutput() {
    outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream, false, StandardCharsets.UTF_8));
  }

  private static void resetStreams() {
    try {
      inputStream.close();
      outputStream.close();
      System.setIn(DEFAULT_SYSTEM_IN);
      System.setOut(DEFAULT_SYSTEM_OUT);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static String getOutput() {
    return outputStream.toString();
  }

}

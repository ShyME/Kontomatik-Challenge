package me.imshy.scraper;

import me.imshy.scraper.domain.Credentials;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CredentialsFileReader {

  private static final String CREDENTIALS_FILEPATH = "credentials.txt";

  public static Credentials readCredentials() {
    try {
      List<String> fileLines = Files.readAllLines(Path.of(CREDENTIALS_FILEPATH));
      String login = fileLines.get(0);
      String password = fileLines.get(1);
      return new Credentials(login, password);
    } catch (IOException e) {
      throw new RuntimeException("Reading credentials from file: " + CREDENTIALS_FILEPATH + " failed.");
    }
  }

}

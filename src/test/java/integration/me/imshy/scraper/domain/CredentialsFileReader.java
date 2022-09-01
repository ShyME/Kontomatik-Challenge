package integration.me.imshy.scraper.domain;

import me.imshy.scraper.domain.Credentials;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CredentialsFileReader {

  private static final String CREDENTIALS_FILEPATH = "credentials.txt";

  public static Credentials readCredentials() {
    List<String> fileLines;
    try {
      fileLines = Files.readAllLines(Path.of(CREDENTIALS_FILEPATH));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    String login = fileLines.get(0);
    String password = fileLines.get(1);
    return new Credentials(login, password);
  }

}

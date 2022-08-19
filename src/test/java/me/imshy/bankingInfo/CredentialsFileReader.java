package me.imshy.bankingInfo;

import me.imshy.bankingInfo.domain.accountDetails.Credentials;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CredentialsFileReader {

  public static Credentials readCredentials(String filePath) {
    try {
      List<String> fileLines = Files.readAllLines(Path.of(filePath));

      String login = fileLines.get(0);
      String password = fileLines.get(1);

      return new Credentials(login, password);
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Reading credentials from file: " + filePath + " failed.");
  }
}

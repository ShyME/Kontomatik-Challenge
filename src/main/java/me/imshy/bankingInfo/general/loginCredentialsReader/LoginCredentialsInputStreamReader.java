package me.imshy.bankingInfo.general.loginCredentialsReader;


import me.imshy.bankingInfo.general.accountDetails.LoginCredentials;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public interface LoginCredentialsInputStreamReader {

  default LoginCredentials readLoginCredentials() {
    return getLoginCredentials(getLoginCredentialsInputStream());
  }

  InputStream getLoginCredentialsInputStream();

  private LoginCredentials getLoginCredentials(InputStream inputStream) {
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line = bufferedReader.readLine();
      StringTokenizer stringTokenizer = new StringTokenizer(line, " ");

      String login;
      String password;

      if (stringTokenizer.countTokens() == 2) {
        login = stringTokenizer.nextToken();
        password = stringTokenizer.nextToken();
      } else {
        login = stringTokenizer.nextToken();
        password = bufferedReader.readLine();
      }

      return new LoginCredentials(login, password);
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("Reading login credentials failed");
  }

}

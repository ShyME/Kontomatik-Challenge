package me.imshy.bankingInfo.general.loginCredentialsReader;

import java.io.InputStream;

public class LoginCredentialsStdInReader implements LoginCredentialsInputStreamReader {

  public InputStream getLoginCredentialsInputStream() {
    return System.in;
  }

}
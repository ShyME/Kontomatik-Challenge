package me.imshy.bankingInfo.general.loginCredentialsReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class LoginCredentialsFileReader implements LoginCredentialsInputStreamReader {

  private final InputStream fileInputStream;

  public LoginCredentialsFileReader(String filePath) {
    try {
      fileInputStream = new FileInputStream(filePath);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new RuntimeException("Creating file input stream from: " + filePath + " failed");
    }
  }

  @Override
  public InputStream getLoginCredentialsInputStream() {
    return fileInputStream;
  }
}

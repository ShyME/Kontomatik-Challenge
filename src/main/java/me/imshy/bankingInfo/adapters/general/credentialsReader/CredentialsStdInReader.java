package me.imshy.bankingInfo.adapters.general.credentialsReader;

import me.imshy.bankingInfo.domain.accountDetails.Credentials;

import java.util.Scanner;

public class CredentialsStdInReader {

  public static Credentials readCredentials() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("PKO login: ");
    String login = scanner.nextLine();
    System.out.print("PKO password: ");
    String password = scanner.nextLine();

    return new Credentials(login, password);
  }
}
package me.imshy.bankingInfo.adapters.general.credentialsReader;

import me.imshy.bankingInfo.domain.accountDetails.Credentials;

import java.util.Scanner;

public class CredentialsStdInReader {

  /*public static Credentials readCredentials() {
    // Cannot invoke "java.io.Console.readLine()" because the return value of "java.lang.System.console()" is null
    System.out.print("PKO login: ");
    String login = System.console().readLine();

    System.out.print("PKO password: ");
    String password = String.valueOf(System.console().readPassword());

    return new Credentials(login, password);
  }*/

  public static Credentials readCredentials() {
    Scanner scanner = new Scanner(System.in);

    System.out.print("PKO login: ");
    String login = scanner.nextLine();
    System.out.print("PKO password: ");
    String password = scanner.nextLine();

    return new Credentials(login, password);
  }
}
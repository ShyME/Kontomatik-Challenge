package me.imshy;

import me.imshy.account.AccountBalance;
import me.imshy.client.BankConnection;
import me.imshy.client.PkoConnection;
import me.imshy.loginCredentials.LoginCredentialsReader;
import me.imshy.loginCredentials.LoginCredentialsStdInReader;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        LoginCredentialsReader loginCredentialsReader = new LoginCredentialsStdInReader();

        try(BankConnection pkoConnection = new PkoConnection(loginCredentialsReader.readLoginCredentials())) {
            printAccountBalances(pkoConnection.getAccountBalances());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printAccountBalances(List<AccountBalance> accountBalances) {
        System.out.println("Accounts: ");
        accountBalances.forEach(System.out::println);
    }
}

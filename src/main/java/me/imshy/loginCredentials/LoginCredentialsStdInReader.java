package me.imshy.loginCredentials;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class LoginCredentialsStdInReader implements LoginCredentialsReader {
    @Override
    public LoginCredentials readLoginCredentials() {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            String line = bufferedReader.readLine();
            StringTokenizer stringTokenizer = new StringTokenizer(line, " ");

            String login;
            String password;

            if(stringTokenizer.countTokens() == 2) {
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
        return null;
    }
}

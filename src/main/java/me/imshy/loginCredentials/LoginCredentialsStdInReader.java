package me.imshy.loginCredentials;

import me.imshy.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class LoginCredentialsStdInReader implements LoginCredentialsReader {
    private static final Logger LOGGER = LogManager.getLogger(LoginCredentialsStdInReader.class);

    @Override
    public LoginCredentials readLoginCredentials() {
        LOGGER.debug("Reading login credentials");
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

            LoginCredentials loginCredentials = new LoginCredentials(login, password);
            LOGGER.debug(loginCredentials.toString());

            return loginCredentials;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Login credentials weren't provided properly");
    }
}

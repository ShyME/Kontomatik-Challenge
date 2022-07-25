package me.imshy.loginCredentials;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class LoginCredentials {
    private String login;
    private String password;
}

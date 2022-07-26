package me.imshy.request.login;

import me.imshy.request.body.SignInRequestBody;

public class LoginRequestBody extends SignInRequestBody {

    public LoginRequestBody(String login) {
        super("login");
        addData("login", login);
    }
}

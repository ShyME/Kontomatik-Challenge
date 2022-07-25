package me.imshy.request.body;

public class LoginRequestBody extends SignInRequestBody {

    public LoginRequestBody(String login) {
        super("login");
        addData("login", login);
    }
}

package me.imshy.request.body;

import lombok.Getter;

@Getter
public class PasswordRequestBody extends SignInRequestBody {
    private final String flow_id;
    private final String token;

    public PasswordRequestBody(String password, String token, String flow_id) {
        super("password");
        addData("password", password);
        this.token = token;
        this.flow_id = flow_id;
    }
}

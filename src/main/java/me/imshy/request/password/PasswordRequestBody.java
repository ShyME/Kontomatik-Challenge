package me.imshy.request.password;

import lombok.Getter;
import me.imshy.request.body.SignInRequestBody;

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

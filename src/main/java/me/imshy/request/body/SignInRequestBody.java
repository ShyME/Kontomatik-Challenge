package me.imshy.request.body;

import lombok.Getter;

@Getter
public abstract class SignInRequestBody extends RequestBody {

    private final String action;
    private final String state_id;

    protected SignInRequestBody(String state_id) {
        super();

        this.state_id = state_id;
        this.action = "submit";
    }
}

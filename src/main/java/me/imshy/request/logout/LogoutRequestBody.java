package me.imshy.request.logout;

import me.imshy.request.body.RequestBody;

public class LogoutRequestBody extends RequestBody {

    public LogoutRequestBody() {
        super();
        addData("reason", "CLIENT_LOGOUT");
    }
}

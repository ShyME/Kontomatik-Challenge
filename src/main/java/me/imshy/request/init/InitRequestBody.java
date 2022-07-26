package me.imshy.request.init;

import me.imshy.request.body.RequestBody;

import java.util.HashMap;
import java.util.Map;

public class InitRequestBody extends RequestBody {

    public InitRequestBody() {
        super();
        Map<Object, Object> emptyDictionary = new HashMap<>();
        addData("account_ids", emptyDictionary);
        addData("accounts", emptyDictionary);
    }
}

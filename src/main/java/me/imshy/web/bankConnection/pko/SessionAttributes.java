package me.imshy.web.bankConnection.pko;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionAttributes {
    private String sessionId;
    private String flowId;
    private String token;
}

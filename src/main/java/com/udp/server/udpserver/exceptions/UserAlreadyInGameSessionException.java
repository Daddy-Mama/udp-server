package com.udp.server.udpserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Player already in this game session")
public class UserAlreadyInGameSessionException extends Exception {
    public UserAlreadyInGameSessionException(String message) {
        super("Player already in this game session: " + message);
    }
}

package com.udp.server.udpserver.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "This game session not contains such user")
public class GameSessionNotContainsUserException extends Exception {
    public GameSessionNotContainsUserException(String message) {
        super("This game session not contains such user: " + message);
    }
}


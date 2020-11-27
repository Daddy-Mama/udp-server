package com.udp.server.udpserver.dto;


import java.io.Serializable;

public class WSMessageDto implements Serializable {
    String message;

    public WSMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

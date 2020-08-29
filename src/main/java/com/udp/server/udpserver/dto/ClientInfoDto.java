package com.udp.server.udpserver.dto;

import lombok.Value;

@Value
public class ClientInfoDto {
    private String ip;
    private Integer port;
}

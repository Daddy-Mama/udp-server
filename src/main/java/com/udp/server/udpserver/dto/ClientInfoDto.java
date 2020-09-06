package com.udp.server.udpserver.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@EqualsAndHashCode
public class ClientInfoDto {
    private String ip;
    private Integer port;
    private String username;

}

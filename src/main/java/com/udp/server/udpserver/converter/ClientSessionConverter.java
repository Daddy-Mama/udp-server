package com.udp.server.udpserver.converter;

import com.udp.server.udpserver.dto.ClientInfoDto;
import com.udp.server.udpserver.model.ClientSession;
import org.springframework.stereotype.Component;

@Component
public class ClientSessionConverter {
    public ClientSession toClientSession(ClientInfoDto clientInfoDto, String ip) {

        return ClientSession.builder()
                .port(clientInfoDto.getPort())
                .username(clientInfoDto.getUsername())
                .ip(ip)
                .build();
    }
}

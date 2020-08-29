package com.udp.server.udpserver.server;

import com.udp.server.udpserver.dto.ClientInfoDto;

public interface UdpServer {
    void joinNewClient(ClientInfoDto clientInfoDto) throws Exception;

    void exitClient(ClientInfoDto clientInfoDto);
}

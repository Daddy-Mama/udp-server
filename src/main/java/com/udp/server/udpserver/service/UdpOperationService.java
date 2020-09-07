package com.udp.server.udpserver.service;

import com.udp.server.udpserver.dto.ClientInfoDto;
import com.udp.server.udpserver.dto.GameDto;
import com.udp.server.udpserver.exceptions.GameSessionNotContainsUserException;
import com.udp.server.udpserver.exceptions.UserAlreadyInGameSessionException;

import java.io.IOException;

public interface UdpOperationService {
    GameDto joinClient(ClientInfoDto clientInfoDto, String ip) throws UserAlreadyInGameSessionException, IOException;


    void removeClient(ClientInfoDto clientInfoDto, String ip, String gameId) throws IOException, GameSessionNotContainsUserException;
}

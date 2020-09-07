package com.udp.server.udpserver.contoller;

import com.udp.server.udpserver.dto.ClientInfoDto;
import com.udp.server.udpserver.dto.ExitClientDto;
import com.udp.server.udpserver.dto.GameDto;
import com.udp.server.udpserver.service.UdpOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/session")
public class SessionController {

    @Autowired
    private UdpOperationService udpServer;


    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public GameDto joinClient(@RequestBody ClientInfoDto clientInfoDto, HttpServletRequest request) throws Exception {
        return udpServer.joinClient(clientInfoDto, request.getRemoteAddr());
    }

    @RequestMapping(value = "/exit", method = RequestMethod.POST)
    public void exitClient(@RequestBody ExitClientDto exitClientDto, HttpServletRequest request) throws Exception {
        udpServer.removeClient(exitClientDto.getClientInfoDto(), request.getRemoteAddr(), exitClientDto.getGameId());
    }
}

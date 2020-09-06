package com.udp.server.udpserver.contoller;

import com.udp.server.udpserver.dto.ClientInfoDto;
import com.udp.server.udpserver.dto.GameDto;
import com.udp.server.udpserver.service.UdpOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(value = "/session")
public class SessionController {

    @Autowired
    private UdpOperationService udpServer;


    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public GameDto joinClient(@RequestBody ClientInfoDto clientInfoDto) throws Exception {
        return udpServer.joinClient(clientInfoDto);
    }

    @RequestMapping(value = "/{gameId}/exit", method = RequestMethod.POST)
    public void exitClient(@RequestBody ClientInfoDto clientInfoDto, @RequestParam String gameId) throws Exception {
        udpServer.removeClient(clientInfoDto, gameId);
    }
}

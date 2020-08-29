package com.udp.server.udpserver.contoller;

import com.udp.server.udpserver.dto.ClientInfoDto;
import com.udp.server.udpserver.server.UdpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/session")
public class SessionController {

    @Autowired
    private UdpServer udpServer;


    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public void joinClient(@RequestBody ClientInfoDto clientInfoDto) throws Exception {
        udpServer.joinNewClient(clientInfoDto);
    }

    @RequestMapping(value = "/exit", method = RequestMethod.POST)
    public void exitClient(@RequestBody ClientInfoDto clientInfoDto) throws Exception {
        udpServer.exitClient(clientInfoDto);
    }
}

package com.udp.server.udpserver.contoller;

import com.udp.server.udpserver.dto.Greeting;
import com.udp.server.udpserver.dto.HelloMessage;
import com.udp.server.udpserver.dto.WSMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate template;


    @MessageMapping("/maniac-home-room/{room}")
    @SendTo(value = "/maniac-home-subscribe/{room}")
    public Greeting greeting1(@DestinationVariable String room, HelloMessage message) throws Exception {
        System.out.println("ROOM: " + room + message.getName());
        return new Greeting(message.getName());
    }
}

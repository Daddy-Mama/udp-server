package com.udp.server.udpserver.contoller;

import com.udp.server.udpserver.dto.Greeting;
import com.udp.server.udpserver.dto.HelloMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;



@Controller
public class WebSocketController {


    @MessageMapping("/maniac-home-room/{room}")
    @SendTo(value = "/maniac-home-subscribe/{room}")
    public Greeting greeting1(@DestinationVariable String room, HelloMessage message) throws Exception {
        System.out.println("ROOM: " + room + message.getName());
        return new Greeting(message.getName());
    }
}

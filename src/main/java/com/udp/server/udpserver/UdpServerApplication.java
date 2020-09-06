package com.udp.server.udpserver;

import com.udp.server.udpserver.service.GameInstanceThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UdpServerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UdpServerApplication.class, args);
        GameInstanceThread gameInstanceThread = new GameInstanceThread("123");
        gameInstanceThread.start();

    }


}

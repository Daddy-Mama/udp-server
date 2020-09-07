package com.udp.server.udpserver;

import com.udp.server.udpserver.contoller.SessionController;
import com.udp.server.udpserver.service.GameInstanceThread;
import com.udp.server.udpserver.service.UdpOperationServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {SessionController.class, UdpOperationServiceImpl.class}, basePackages = "com")
public class UdpServerApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(UdpServerApplication.class, args);
//        GameInstanceThread gameInstanceThread = new GameInstanceThread("123");
//        gameInstanceThread.start();

    }


}

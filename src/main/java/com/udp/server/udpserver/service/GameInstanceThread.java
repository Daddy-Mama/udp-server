package com.udp.server.udpserver.service;


import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.udp.server.udpserver.dto.ClientInfoDto;
import com.udp.server.udpserver.exceptions.GameSessionNotContainsUserException;
import com.udp.server.udpserver.exceptions.UserAlreadyInGameSessionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import static com.udp.server.udpserver.MessagesEnum.PLAYER_JOINED;
import static com.udp.server.udpserver.MessagesEnum.PLAYER_LEFT;

@Slf4j
public class GameInstanceThread extends Thread {

    private boolean started = false;
    protected DatagramSocket socket;
    //Todo: annotation not works because of non-spring bean class
    @Value(value = "${server.broadcast.port:11234}")
    private Integer broadcastPort = 11234;
    @Value(value = "${server.receive.port:11235}")
    private Integer receivePort = 11235;


    private List<ClientInfoDto> players = new ArrayList<>();

    public GameInstanceThread(String name) throws IOException {
        super(name);
        //Todo check here port usability
        socket = new DatagramSocket(receivePort);

//        Server server = new Server();
//        server.start();
//        server.bind(8081, receivePort);
//
//        server.addListener(createListener());
    }

    public void run() {
        while (true) {
            try {
                String messageToSend = "service response";

                //read message from player
                DatagramPacket packet = readMessageFromPlayer();

                log.info("received: " + new String(packet.getData()));
//                if (!allowBroadCasting(packet.getAddress().getHostAddress())) {
//                    continue;
//                }

                // send response
                sendMessageToAllPlayers(messageToSend);
                // sleep for a while

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private boolean allowBroadCasting(String playerIp) {
        return players.stream()
                .map(player -> player.getIp())
                .anyMatch(ip -> ip.equals(playerIp));


    }

    private DatagramPacket readMessageFromPlayer() throws IOException {
        byte[] receiveData = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        return receivePacket;
    }

    private void sendMessageToAllPlayers(String messageToSend) throws IOException {
        byte[] buf = messageToSend.getBytes();

        InetAddress group = InetAddress.getByName("230.199.234.255");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, broadcastPort);
        socket.send(packet);
    }

    public synchronized void joinNewPlayer(ClientInfoDto clientInfoDto) throws UserAlreadyInGameSessionException, IOException {
        if (!players.contains(clientInfoDto)) {
            //Session is full
            if (!isAvailable()) return;

            informGroupNewPlayerJoinedGame(clientInfoDto.getUsername());
            players.add(clientInfoDto);

            //start game if it's full
            if (players.size() == 10) {
                started = true;
            }

            return;
        }
        throw new UserAlreadyInGameSessionException(clientInfoDto.getUsername());
    }

    public boolean isAvailable() {
        return !started;
    }

    private void informGroupNewPlayerJoinedGame(String username) throws IOException {
        sendMessageToAllPlayers(username + PLAYER_JOINED.name());
    }

    private void informGroupPlayerLeftGame(String username) throws IOException {
        sendMessageToAllPlayers(username + PLAYER_LEFT.name());
    }

    public void removePlayer(ClientInfoDto clientInfoDto) throws GameSessionNotContainsUserException, IOException {
        if (players.contains(clientInfoDto)) {
            informGroupPlayerLeftGame(clientInfoDto.getUsername());
            players.remove(clientInfoDto);
            return;
        }
        throw new GameSessionNotContainsUserException(clientInfoDto.getUsername());
    }

    public int getPlayersCount() {
        return players.size();
    }

    //TODO: Server must calculate when game should be cancelled
    public boolean isFinished() {
        return players.size() == 0 && started;
    }

//    private Listener createListener() {
//        return new Listener() {
//            public void received(Connection connection, String message) throws IOException {
//                String messageToSend = "service response";
//
//                log.info("received: " +message);
//
//                sendMessageToAllPlayers(messageToSend);
//
//            }
//        };
//    }
}

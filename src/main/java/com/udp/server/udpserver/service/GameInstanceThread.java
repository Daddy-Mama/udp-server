package com.udp.server.udpserver.service;


import com.udp.server.udpserver.dto.ClientInfoDto;
import com.udp.server.udpserver.exceptions.GameSessionNotContainsUserException;
import com.udp.server.udpserver.exceptions.UserAlreadyInGameSessionException;
import com.udp.server.udpserver.model.ClientSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.udp.server.udpserver.MessagesEnum.PLAYER_JOINED;
import static com.udp.server.udpserver.MessagesEnum.PLAYER_LEFT;

@Slf4j
public class GameInstanceThread extends Thread {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private boolean started = false;
    protected DatagramSocket socket;

    private Integer port = 11234;


    private List<ClientSession> players = new ArrayList<>();

    public GameInstanceThread(String name) throws IOException {
        super(name);
        //Todo check here port usability
        socket = new DatagramSocket(port);
        running.set(true);
    }

    public void run() {
        while (running.get()) {
            try {
                log.info("Server session STARTED");

                String messageToSend = "service response";

                //read message from player
                DatagramPacket packet = readMessageFromPlayer();

                log.info("received: " + new String(packet.getData()));
                log.info("sender info: " + packet.getAddress().getHostAddress() + ":" + packet.getPort());
                if (!allowBroadCasting(packet.getAddress().getHostAddress())) {
                    continue;
                }

                // send response
                sendMessageToAllPlayers(messageToSend, packet.getAddress().getHostAddress());
                // sleep for a while

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("Socket closed");
        socket.close();
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

    private void sendMessageToAllPlayers(String messageToSend, String sender) throws IOException {
        byte[] toSendData = messageToSend.getBytes();

        players.stream()
                .filter(player -> !player.getIp().equals(sender))
                .forEach(player -> {
                    try {
                        DatagramPacket sendPacket =
                                new DatagramPacket(toSendData,
                                        toSendData.length,
                                        InetAddress.getByName(player.getIp()),
                                        player.getPort());

                        socket.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }

    public synchronized void joinNewPlayer(ClientSession clientSession) throws UserAlreadyInGameSessionException, IOException {
        if (!players.contains(clientSession)) {
            //Session is full
            if (!isAvailable()) return;

            informGroupNewPlayerJoinedGame(clientSession.getUsername());
            players.add(clientSession);

            //start game if it's full
            if (players.size() == 10) {
                started = true;
            }

            return;
        }
        throw new UserAlreadyInGameSessionException(clientSession.getUsername());
    }

    public boolean isAvailable() {
        return !started;
    }

    private void informGroupNewPlayerJoinedGame(String username) throws IOException {
        sendMessageToAllPlayers(username + PLAYER_JOINED.name(), null);
    }

    private void informGroupPlayerLeftGame(String username) throws IOException {
        sendMessageToAllPlayers(username + PLAYER_LEFT.name(), null);
    }

    public void removePlayer(ClientSession clientSession) throws GameSessionNotContainsUserException, IOException {
        if (players.contains(clientSession)) {
            informGroupPlayerLeftGame(clientSession.getUsername());
            players.remove(clientSession);
            return;
        }
        throw new GameSessionNotContainsUserException(clientSession.getUsername());
    }

    public int getPlayersCount() {
        return players.size();
    }

    //TODO: Server must calculate when game should be cancelled
    public boolean isFinished() {
        if (players.size() == 0 && !started) {
            socket.close();
            return true;
        }
        return false;

    }

}

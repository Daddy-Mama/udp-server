//package com.udp.service.udpserver.service;
//
//import com.udp.service.udpserver.dto.ClientInfoDto;
//import com.udp.service.udpserver.model.ClientSession;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.net.*;
//import java.util.*;
//
//@Service
//public class UdpServerService implements UdpServer {
//    private final Integer port = 11234;
//
//
//    private DatagramSocket serverSocket;
//    private byte[] receiveData;
//
//    private Map<Integer, Set<ClientSession>> clientsPerSession = new HashMap<>();
//    int i = 1;
//
//    public UdpServerService() throws SocketException {
//        serverSocket = new DatagramSocket(port);
//        receiveData = new byte[1024];
//
//        start();
//    }
//
//    private void start() {
//
//        while (true) {
//            try {
//                System.out.println("==========================================================================");
//                DatagramPacket packet = readMessage();
//
//                Integer sessionId = findUsersSession(packet.getAddress().getHostAddress(), packet.getPort());
//                String sentence = new String(packet.getData());
//                System.out.println("RECEIVED: " + sentence + "\n FROM IP:" + packet.getAddress().toString() + ":" + packet.getPort());
//
//                if (sessionId != null) {
//                    System.out.println("USER EXIST");
//
//
//                    sendToAllClientsInSession(packet, sessionId);
//                } else {
//                    System.out.println("USER NOT FOUND");
////                    Set<ClientSession> sessions = clientsPerSession.get(i);
////                    if (sessions==null){
////                        sessions=new HashSet<>();
////                    }
////                    ClientSession clientSession = ClientSession.builder().userIp(packet.getAddress().getHostAddress()).port(packet.getPort()).build();
////                    sessions.add(clientSession);
////                    clientsPerSession.put(i,sessions);
////                    System.out.println("USER ADDED: "+ clientSession.getUserIp()+":"+clientSession.getPort());
////
////                    sendToAllClientsInSession(packet, i);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private DatagramPacket readMessage() throws IOException {
//        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//        serverSocket.receive(receivePacket);
//
//        return receivePacket;
//    }
//
//    private void sendToAllClientsInSession(DatagramPacket packet, Integer sessionId) {
//        System.out.println("Send to all clients");
//        if (clientsPerSession.get(1) != null) {
//            System.out.println("CLIENTS IN SESSION 1: " + clientsPerSession.get(1).size());
//        }
//        String senderData = new String(packet.getData());
//
//        clientsPerSession.get(sessionId).stream()
//                .forEach(client -> sendMessageToSession(client.getUserIp(), client.getPort(), senderData.getBytes()));
//    }
//
//    private void sendMessageToSession(String ip, int port, byte[] toSendData) {
//        try {
//            DatagramPacket sendPacket =
//                    new DatagramPacket(toSendData, toSendData.length, InetAddress.getByName(ip), port);
//            System.out.println(("Send message to: " + ip + ":" + port));
//            serverSocket.send(sendPacket);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private Integer findUsersSession(String ip, Integer port) {
//        Optional<Integer> session = clientsPerSession.keySet().stream()
//                .filter(sessionId -> clientsPerSession.get(sessionId).stream()
//                        .filter(clientSession -> clientSession.getUserIp().equals(ip) && clientSession.getPort().equals(port))
//                        .findFirst()
//                        .isPresent())
//                .findFirst();
//
//        if (session.isPresent()) {
//            return session.get();
//        } else {
//            return null;
//        }
//        //ToDo add search-error case
//    }
//
//    public synchronized void joinNewClient(ClientInfoDto clientInfoDto) throws Exception {
//        Integer sessionId = findUsersSession(clientInfoDto.getIp(), clientInfoDto.getPort()) == null ''
//        if (sessionId == null) {
//            Set<ClientSession> sessions = clientsPerSession.get(i);
//            if (sessions == null) {
//                sessions = new HashSet<>();
//            }
//            ClientSession clientSession = ClientSession.builder()
//                    .userIp(clientInfoDto.getIp())
//                    .port(clientInfoDto.getPort())
//                    .build();
//
//            sessions.add(clientSession);
//            clientsPerSession.put(i, sessions);
//            System.out.println("USER ADDED: " + clientSession.getUserIp() + ":" + clientSession.getPort());
//
//            //send message that user connected
//            String message = clientInfoDto.getIp();
//            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length);
//            sendToAllClientsInSession(packet, sessionId);
//        }
//
//        throw new Exception("User already exist in session");
//    }
//
//
//    public void exitClient(ClientInfoDto clientInfoDto) {
//        Integer sessiondId = findUsersSession(clientInfoDto.getIp(), clientInfoDto.getPort());
//        if (sessiondId != null) {
//            Set<ClientSession> sessions = clientsPerSession.get(sessiondId);
//            ClientSession clientSession = ClientSession.builder()
//                    .userIp(clientInfoDto.getIp())
//                    .port(clientInfoDto.getPort())
//                    .build();
//            sessions.remove(clientSession);
//
//            //send message that user exit
//            String message = clientInfoDto.getIp();
//            DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length);
//            sendToAllClientsInSession(packet, clientSession.getUserIp());
//        }
//    }
//}
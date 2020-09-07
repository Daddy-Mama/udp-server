package com.udp.server.udpserver.service;

import com.udp.server.udpserver.converter.ClientSessionConverter;
import com.udp.server.udpserver.dto.ClientInfoDto;
import com.udp.server.udpserver.dto.GameDto;
import com.udp.server.udpserver.exceptions.GameSessionNotContainsUserException;
import com.udp.server.udpserver.exceptions.UserAlreadyInGameSessionException;
import com.udp.server.udpserver.model.ClientSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UdpOperationServiceImpl implements UdpOperationService {

    @Autowired
    private ClientSessionConverter clientSessionConverter;
    private List<GameInstanceThread> activeGames;

    @Autowired
    public UdpOperationServiceImpl() {
        this.activeGames = new ArrayList<>();
    }

    @Override
    public GameDto joinClient(ClientInfoDto clientInfoDto, String ip) throws UserAlreadyInGameSessionException, IOException {
        ClientSession clientSession = clientSessionConverter.toClientSession(clientInfoDto, ip);

        log.info("New client: " + clientSession.toString());
        Optional<GameInstanceThread> gameOptional = activeGames.stream()
                .parallel()
                .filter(game -> game.isAvailable())
                .findAny();

        if (gameOptional.isPresent()) {
            GameInstanceThread game = gameOptional.get();
            game.joinNewPlayer(clientSession);
            return GameDto.builder()
                    .gameId(game.getName())
                    .build();
        }
        GameInstanceThread newGame = new GameInstanceThread(String.valueOf(activeGames.size() + 1));
        newGame.joinNewPlayer(clientSession);
        newGame.start();
        activeGames.add(newGame);

        return GameDto.builder()
                .gameId(newGame.getName())
                .build();
    }

    @Override
    public void removeClient(ClientInfoDto clientInfoDto, String ip, String gameId) throws IOException, GameSessionNotContainsUserException {
        ClientSession clientSession = clientSessionConverter.toClientSession(clientInfoDto, ip);

        Optional<GameInstanceThread> gameOptional =
                activeGames.stream()
                        .parallel()
                        .filter(game -> game.getName().equals(gameId))
                        .findFirst();

        if (gameOptional.isPresent()) {
            GameInstanceThread game = gameOptional.get();
            game.removePlayer(clientSession);
            //Todo shoulld be changed, when server can calculate game end
            //Delete game from active games if there is no players
            if (game.isFinished()) {
                activeGames.remove(game);
                game.stop();
                log.info("Game finished: " + game.getId());
            }
            log.info("Player: " + clientSession.toString() + " disconnected from gameId:" + gameId);
        }

    }
}

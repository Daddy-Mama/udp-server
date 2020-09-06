package com.udp.server.udpserver.service;

import com.udp.server.udpserver.dto.ClientInfoDto;
import com.udp.server.udpserver.dto.GameDto;
import com.udp.server.udpserver.exceptions.GameSessionNotContainsUserException;
import com.udp.server.udpserver.exceptions.UserAlreadyInGameSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UdpOperationServiceImpl implements UdpOperationService {

    private List<GameInstanceThread> activeGames;

    @Autowired
    public UdpOperationServiceImpl() {
        this.activeGames = new ArrayList<>();
    }

    @Override
    public GameDto joinClient(ClientInfoDto clientInfoDto) throws UserAlreadyInGameSessionException, IOException {
        Optional<GameInstanceThread> gameOptional = activeGames.stream()
                .parallel()
                .filter(game -> game.isAvailable())
                .findAny();

        if (gameOptional.isPresent()) {
            GameInstanceThread game = gameOptional.get();
            game.joinNewPlayer(clientInfoDto);
            return GameDto.builder()
                    .gameId(game.getName())
                    .build();
        }
        GameInstanceThread newGame = new GameInstanceThread(String.valueOf(activeGames.size() + 1));
        newGame.joinNewPlayer(clientInfoDto);
        return GameDto.builder()
                .gameId(newGame.getName())
                .build();
    }

    @Override
    public void removeClient(ClientInfoDto clientInfoDto, String gameId) throws IOException, GameSessionNotContainsUserException {
        Optional<GameInstanceThread> gameOptional =
                activeGames.stream()
                        .parallel()
                        .filter(game -> game.getName().equals(gameId))
                        .findFirst();

        if (gameOptional.isPresent()) {
            GameInstanceThread game = gameOptional.get();
            game.removePlayer(clientInfoDto);
            //Todo shoulld be changed, when server can calculate game end
            //Delete game from active games if there is no players
            if (game.isFinished()) {
                activeGames.remove(game);
            }
        }

    }
}

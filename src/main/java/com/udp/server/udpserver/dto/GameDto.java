package com.udp.server.udpserver.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameDto {
    String gameId;
    int gamePort;
}

package com.udp.server.udpserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.util.Objects;

@Builder
@Getter
@ToString
public class ClientSession {
    private final Integer port;
    private final String ip;
    private final String username;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientSession that = (ClientSession) o;
        return port.equals(that.port) &&
                ip.equals(that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, ip);
    }
}

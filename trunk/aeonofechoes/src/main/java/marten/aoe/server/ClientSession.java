package marten.aoe.server;

import java.io.Serializable;

public class ClientSession implements Serializable {
    private static final long serialVersionUID = 1L;
    public String username;

    public ClientSession(String username) {
        this.username = username;
    }
}

package marten.aoe.server.serializable;

import java.io.Serializable;

public class ClientSession implements Serializable {
    private static final long serialVersionUID = 1L;
    public String secret;
    public String id;

    public ClientSession(String username, String secret) {
        this.secret = secret;
        this.id = username;
    }
}

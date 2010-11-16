package marten.aoe.server;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;

public class ClientSession implements Serializable {
    private static final long serialVersionUID = 1L;
    public String secret;
    public String id;

    public ClientSession(String username) {
        this.secret = new BigInteger(130, new SecureRandom()).toString(32);
        this.id = username;
    }
}

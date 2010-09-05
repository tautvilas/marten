package marten.aoe.server;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    public String sender;
    public String message;
    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }
}

package marten.aoe.server;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    public String from;
    public String message;

    public ChatMessage(String from, String message) {
        this.from = from;
        this.message = message;
    }
}

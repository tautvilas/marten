package marten.aoe.server;

import java.util.LinkedList;

public class ClientMessageProvider {

    private LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>();

    public synchronized void addChatMessage(ChatMessage message) {
        messages.add(message);
        this.notify();
    }

    public synchronized ChatMessage getMessage() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return messages.getLast();
    }
}

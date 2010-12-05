package marten.aoe.server;

import java.util.LinkedList;

import marten.aoe.server.serializable.ChatMessage;
import marten.aoe.server.serializable.ServerNotification;

public class ServerClient {
    private LinkedList<ChatMessage> inbox = new LinkedList<ChatMessage>();
    private LinkedList<ServerNotification> notifier = new LinkedList<ServerNotification>();
    private String username;

    public ServerClient(String username) {
        this.username = username;
    }

    public LinkedList<ServerNotification> getNotifier() {
        return this.notifier;
    }

    public String getUsername() {
        return this.username;
    }

    public synchronized ChatMessage getLastMessage() {
        if (inbox.isEmpty()) {
            return null;
        } else {
            return inbox.pop();
        }
    }

    public synchronized void addMessage(ChatMessage message) {
        inbox.add(message);
        synchronized (this.notifier) {
            this.notifier.add(ServerNotification.NEW_MESSAGE);
            this.notifier.notifyAll();
        }
    }

    public void notify(ServerNotification notification) {
        synchronized (this.notifier) {
            this.notifier.add(notification);
            this.notifier.notifyAll();
        }
    }
}

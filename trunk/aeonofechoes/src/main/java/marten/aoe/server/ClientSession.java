package marten.aoe.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

public class ClientSession extends UnicastRemoteObject implements Session {

    private LinkedList<ChatMessage> messages = new LinkedList<ChatMessage>();

    private String username;

    public ClientSession(String username) throws RemoteException {
        super();
        this.username = username;
    }

    public String getUsername() throws RemoteException {
        return this.username;
    }

    protected void publishMessage(String from, String message) {
        messages.add(new ChatMessage(from, message));
    }

    private static final long serialVersionUID = 1L;

    @Override
    public ChatMessage popMessage() throws RemoteException {
        if (messages.isEmpty()) {
            return null;
        } else {
            return messages.remove();
        }
    }

}

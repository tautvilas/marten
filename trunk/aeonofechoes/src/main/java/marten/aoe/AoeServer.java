package marten.aoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class AoeServer implements Runnable {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(AoeServer.class);

    private static final int PORT = 4561;
    private static final int MAX_CONNECTIONS = 100;

    public AoeServer() {
    }

    public void start() {
        Thread server = new Thread(this);
        server.start();
        log.info("AOE server instance started");
    }

    @Override
    public void run() {
        int connections = 0;

        try {
            ServerSocket listener = new ServerSocket(PORT);
            Socket server;

            while ((connections++ < MAX_CONNECTIONS)) {
                server = listener.accept();
                CommandHandler conn_c = new CommandHandler(server);
                Thread t = new Thread(conn_c);
                t.start();
                log.info("Client " + connections + "connected");
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}

class CommandHandler implements Runnable {
    private static org.apache.log4j.Logger log = Logger
            .getLogger(CommandHandler.class);
    private Socket server;
    private String line, input;

    CommandHandler(Socket server) {
        this.server = server;
    }

    public void run() {

        input = "";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(server
                    .getInputStream()));
            PrintStream out = new PrintStream(server.getOutputStream());

            while ((line = in.readLine()) != null && !line.equals(".")) {
                input = input + line;
                out.println("I got:" + line);
            }

            log.info("Message is:" + input);
            out.println("Overall message is:" + input);

            server.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}

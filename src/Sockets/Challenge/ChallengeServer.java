package Sockets.Challenge;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Kévin on 23/03/2016.
 */
public class ChallengeServer {

    private ServerSocket serverSocket;

    final ArrayList<ServerHandler> threads = new ArrayList<ServerHandler>();

    public ChallengeServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[ChallengeServer] En attente de connexions sur le port "+port);
            acceptLoop();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptLoop(){
        try {
            Socket client = serverSocket.accept();
            System.out.println("[ChallengeServer] Connexion entrante: <" + client.getInetAddress().getHostAddress() + ":"+client.getPort()+">");
            ServerHandler handler = new ServerHandler(client, this);
            synchronized (threads) {
                threads.add(handler);
            }
            handler.start();
            acceptLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChallengeServer server = new ChallengeServer(5000);
    }

}

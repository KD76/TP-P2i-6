package Sockets.NombreSecret;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Kévin on 23/03/2016.
 */
public class SecretServer {

    private ServerSocket serverSocket;

    final ArrayList<ServerHandler> threads = new ArrayList<ServerHandler>();

    public static boolean benchMode = true;

    public SecretServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[SecretServer] En attente de connexions sur le port "+port);
            acceptLoop();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptLoop(){
        try {
            Socket client = serverSocket.accept();
            System.out.println("[SecretServer] Connexion entrante: <" + client.getInetAddress().getHostAddress() + ":"+client.getPort()+">");
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
        SecretServer server = new SecretServer(5000);
    }

}

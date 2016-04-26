package Sockets.NombreSecret;

import java.io.*;
import java.net.Socket;

/**
 * Created by Kévin on 23/03/2016.
 */
public class ServerHandler extends Thread {

    Socket clientSocket;
    SecretServer server;

    DataInputStream inputReader;
    PrintWriter outputWriter;

    int clientPhase;

    int number;
    int count;

    public ServerHandler(Socket client, SecretServer serv) {
        clientSocket = client;
        server = serv;
        try {
            inputReader = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            outputWriter = new PrintWriter(clientSocket.getOutputStream());

            if (!SecretServer.benchMode) {
                prepareNumber();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareNumber() {
        number = (int)(30000*Math.random());
        System.out.println("[ServerHandler <"+this.clientSocket.getInetAddress().getHostAddress()+":"+clientSocket.getPort()+">] Nombre secret : "+number);
    }

    public void sendVal(String val) {
        outputWriter.println(val);
        outputWriter.flush();
    }

    private void handleShort(short val) {
        System.out.println("[ServerHandler <"+this.clientSocket.getInetAddress().getHostAddress()+":"+clientSocket.getPort()+">] Nombre reçu : "+val);
        if (val > number) {
            sendVal("Trop grand !");
        } else if (val < number) {
            sendVal("Trop petit !");
        } else {
            sendVal("Bravo ! tu as trouvé le nombre secret: "+number);
            clientPhase+=2;
        }
    }

    private boolean isNumber(String s) {
        for(int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public void run() {
        try {
            while(clientPhase != 2) {
                if (SecretServer.benchMode && clientSocket.getInputStream().available() > 0) {
                    int val = clientSocket.getInputStream().read();
                    if ((char)val == 'B') {
                        count++;
                        if (count % 1000 == 0) {
                            System.out.println(count);
                        }
                    } else {
                        System.out.println("ERREUR VALEUR !");
                    }
                } else {
                    if (inputReader.available() > 0) {
                        short val = inputReader.readShort();
                        handleShort(val);
                    }
                }
                sleep(10);
            }
            clientSocket.close();
            System.out.println("[ServerHandler <"+clientSocket.getInetAddress().getHostAddress() + ":"+clientSocket.getPort()+">] Fermeture de la connexion.");
            synchronized (server.threads) {
                server.threads.remove(this);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
    }

}

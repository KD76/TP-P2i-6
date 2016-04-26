package Sockets.Challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Kévin on 23/03/2016.
 */
public class ServerHandler extends Thread {

    Socket clientSocket;
    ChallengeServer server;

    BufferedReader inputReader;
    PrintWriter outputWriter;

    int clientPhase;

    int number;

    public ServerHandler(Socket client, ChallengeServer serv) {
        clientSocket = client;
        server = serv;
        try {
            inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputWriter = new PrintWriter(clientSocket.getOutputStream());

            prepareNumber();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareNumber() {
        number = (int)(100*Math.random());
        sendVal("Carre de : "+number);
        System.out.println("[ServerHandler <"+this.clientSocket.getLocalAddress().getHostAddress()+":"+clientSocket.getLocalPort()+">] Carré du nombre "+number+" demandé. Attendu : "+number*number);
    }

    public void sendVal(String val) {
        outputWriter.println(val);
        outputWriter.flush();
    }

    private void handleString(String input) {
        if (clientPhase == 0 && isNumber(input)) {
            int val = Integer.parseInt(input);
            if (val == number*number) {
                sendVal("Bonne reponse. Entrez votre message.");
                System.out.println("[ServerHandler <"+clientSocket.getInetAddress().getHostAddress() + ":"+clientSocket.getPort()+">] Bonne réponse du client : "+val+". En attente du message");
                clientPhase++;
            }
        } else if (clientPhase == 1) {
            System.out.println("[ServerHandler <"+clientSocket.getInetAddress().getHostAddress() + ":"+clientSocket.getPort()+">] Message reçu : "+input);
            sendVal("Bye.");
            clientPhase++;
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
            if (inputReader.ready()) {
                String inputVal = inputReader.readLine();
                System.out.println(inputVal);
                handleString(inputVal);
            }
            sleep(10);
            if (clientPhase == 2) {
                clientSocket.close();
                System.out.println("[ServerHandler <"+clientSocket.getInetAddress().getHostAddress() + ":"+clientSocket.getPort()+">] Fermeture de la connexion.");
                synchronized (server.threads) {
                    server.threads.remove(this);
                }
            } else {
                run();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
    }

}

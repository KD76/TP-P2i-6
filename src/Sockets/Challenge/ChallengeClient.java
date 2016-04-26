package Sockets.Challenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Kévin on 23/03/2016.
 */
public class ChallengeClient {

    private Socket clientSocket;
    private BufferedReader inputBuffer;
    private PrintWriter outputWriter;

    private boolean loopEnabled = true;
    private boolean finished = false;

    private String currentLine = "";

    public ChallengeClient(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            inputBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputWriter = new PrintWriter(clientSocket.getOutputStream());

            startLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendValue(String val) {
        System.out.println("Envoi de la valeur :" + val);
        outputWriter.println(val);
        outputWriter.flush();
    }

    public void handleString() {
        if (currentLine.contains("Carr")) {
            int nb = Integer.parseInt(currentLine.substring(11));
            sendValue(""+nb*nb);
        } else if (currentLine.contains("Bonne reponse")) {
            sendValue("Super ! Merci !");
            finished = true;
        } else if (finished) {
            loopEnabled = false;
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startLoop() {
        try {
            if (inputBuffer.ready()) {
                currentLine = inputBuffer.readLine();
                System.out.println(currentLine);
                handleString();
            }
            if (loopEnabled) {
                Thread.sleep(10);
                startLoop();
            }
            //sendPing();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChallengeClient client = new ChallengeClient("127.0.0.1", 5000);
    }

}

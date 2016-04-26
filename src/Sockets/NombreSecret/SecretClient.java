package Sockets.NombreSecret;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Kévin on 23/03/2016.
 */
public class SecretClient {

    private Socket clientSocket;
    private BufferedReader inputBuffer;
    private DataOutputStream outputWriter;

    private boolean loopEnabled = true;
    private boolean finished = false;
    private boolean waitForInput = true;

    private String currentLine = "";

    private Scanner scanner;

    private static boolean benchMode = true;
    private static int bufferSize = 40000;

    public SecretClient(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            inputBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputWriter = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

            if (benchMode) {
                char c = 'B';

                byte[] data = new byte[bufferSize];
                for (int i=0; i<bufferSize; i++) {
                    data[i] = (byte)c;

                    clientSocket.getOutputStream().write(data[i]);
                    //outputWriter.writeByte(data[i]);
                }

                outputWriter.flush();
                clientSocket.close();

            } else {
                scanner = new Scanner(System.in);

                System.out.println("Bienvenue ! Trouvez le nombre secret, compris entre 0 et 30 000.");
            }


            startLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendValue(short val) {
        System.out.println("Envoi de la valeur :" + val);
        try {
            outputWriter.writeShort(val);
            outputWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleString() {
        if (currentLine.contains("Bravo")) {
            loopEnabled = false;
        } else {
            waitForInput = true;
        }
    }

    public void startLoop() {
        if (!benchMode) {
            try {
                if (inputBuffer.ready()) {
                    currentLine = inputBuffer.readLine();
                    System.out.println(currentLine);
                    handleString();
                }
                if (waitForInput) {
                    try {
                        short val = Short.parseShort(scanner.next());

                        sendValue(val);
                        waitForInput = false;
                    } catch (Exception e) {
                        System.out.println("Nombre invalide !");
                    }
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
    }

    public static void main(String[] args) {
        System.out.println("Entrez l'IP:");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.next();
        SecretClient client = new SecretClient(ip, 5000);
    }

}

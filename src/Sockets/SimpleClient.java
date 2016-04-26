package Sockets;

import java.io.*;
import java.net.Socket;

/**
 * Created by Kévin on 23/03/2016.
 */
public class SimpleClient {

    Socket clientSocket;
    BufferedReader inputBuffer;
    PrintWriter outputWriter;

    public SimpleClient(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            inputBuffer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputWriter = new PrintWriter(clientSocket.getOutputStream());

            sendPing();

            startLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPing() {
        outputWriter.print("Ping !");
        outputWriter.flush();
    }

    public void startLoop() {
        try {
            if (inputBuffer.ready()) {
                System.out.println(inputBuffer.readLine());
            }
            Thread.sleep(500);
            startLoop();
            sendPing();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SimpleClient client = new SimpleClient("y033.insa-lyon.fr", 5001);
    }

}

package org.academiadecodigo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

/**
 * Created by codecadet on 08/11/16.
 */
public class Webserver {


    public static void main(String[] args) throws IOException {

        int portNumber = 8080;

        ServerSocket serverSocket = new ServerSocket(portNumber);

        while (true) {

            Socket clientSocket = serverSocket.accept();
            Thread client = new Thread(new ClientConnection(clientSocket));
            client.start();

        }
    }
}

package org.academiadecodigo;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/**
 * Created by codecadet on 09/11/16.
 */
public class ClientConnection implements Runnable {


    private static ContentType_Generator mimeManager;
    private Socket clientSocket;

    public ClientConnection(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {


        try {

            mimeManager = new ContentType_Generator("mimeTypes.csv");


            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            String clientMessage;
            clientMessage = in.readLine();

            System.out.println(clientMessage);

            String filePath = "www" + clientMessage.substring(clientMessage.indexOf("/"), clientMessage.indexOf("H") - 1);
            System.out.println(filePath);

            File file = new File(filePath);
            System.out.println("This File Exists?: "+file.exists());

            String head;

            file = new File(filePath);

            if (filePath.equals("www/")) {
                file.toPath();
                filePath = "www/index.html";
            }

            if (!file.exists()) {

                file = new File("404.html");
                System.out.println("Get Path: " + file.getPath());
                filePath = file.getPath();

                head = headCreator(404, file.getPath());
                //head = "HTTP/1.0 404 Not Found Content-Type:"+mimeManager.getMime(file.getName().substring(file.getName().indexOf(".")))+"; charset=UTF-8\r\n Content-Length:" + readFile(file.getPath()).length + "\r\n\r\n";
            } else {

                System.out.println("This is the Content Type of the Page: " + mimeManager.getMime(filePath.substring(filePath.indexOf("."))));
                head = headCreator(200, filePath);


                byte[] contentToSend = new byte[0];
                contentToSend = new byte[head.getBytes().length + Files.readAllBytes(file.toPath()).length];

                System.arraycopy(head.getBytes(), 0, contentToSend, 0, head.getBytes().length);
                System.arraycopy(Files.readAllBytes(file.toPath()), 0, contentToSend, head.getBytes().length, Files.readAllBytes(file.toPath()).length);

                out.write(contentToSend);

                clientSocket.close();


            }

        } catch (IOException e) {
            e.printStackTrace();


        }
    }


    private static String headCreator(int statusCode, String filePath) throws IOException {
        if (statusCode == 404) {
            return "HTTP/1.0 404 Not Found Content-Type:" + mimeManager.getMime(filePath.substring(filePath.indexOf("."))) + "; charset=UTF-8\r\n Content-Length:" + String.valueOf(readFile(filePath).length + "\r\n\r\n");
        }

        return "HTTP/1.0 200 Document Follows\r\n" +
                "Content-Type:" + mimeManager.getMime(filePath.substring(filePath.indexOf("."))) + "; charset=UTF-8\r\n" +
                "Content-Length:" + String.valueOf(readFile(filePath).length) + " \r\n" +
                "\r\n";
    }


    private static byte[] readFile(String file) throws IOException {

        FileReader fileReader = new FileReader(file);

        char[] chars = new char[2048];

        fileReader.read(chars);

        String resultingString = new String(chars);

        fileReader.close();

        return resultingString.getBytes();
    }


}

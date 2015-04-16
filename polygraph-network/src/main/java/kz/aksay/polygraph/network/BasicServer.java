package kz.aksay.polygraph.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BasicServer {

    private ServerSocket server;
    private int port;

    public BasicServer(int port) {
        try {
        	this.port = port;
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	int port = Integer.parseInt(args[0]);
    	BasicServer example = new BasicServer(port);
        example.handleConnection();
    }

    public void handleConnection() {
        System.out.println("Waiting for client message...");
        // The server do a loop here to accept all connection initiated by the
        // client application.
        while (true) {
        	System.out.println("While waiting for client message...");
            try {
                Socket socket = server.accept();
                System.out.println("socket connected "+socket.getInetAddress());
                new ConnectionHandler(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ConnectionHandler implements Runnable {

    private Socket socket;

    public ConnectionHandler(Socket socket) {
        this.socket = socket;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
        // Read a message sent by client application
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message Received: " + message);
       // Send a response information to the client application
           ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
           oos.writeObject("Response");

            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
 

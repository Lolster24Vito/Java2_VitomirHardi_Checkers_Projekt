package hr.algebra.server;

import hr.algebra.Thread.ClientHandlerThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static ArrayList<ClientHandlerThread> clientHandlers = new ArrayList();

    public static final String HOST = "localhost";
    public static final int PORT = 1987;

    public static void main(String[] args) {
    //todo make collection of players
    //todo make a dictionary of rooms with codes
    acceptRequests();
    }

    private static void acceptRequests() {
    try(ServerSocket serverSocket=new ServerSocket(PORT)){
        System.err.println("Server listening on port: " + serverSocket.getLocalPort());
        while (true){
            Socket clientSocket = serverSocket.accept();
            System.err.println("Client connected from port: " + clientSocket.getPort()+"----"+clientSocket.getInetAddress());
            ClientHandlerThread clientHandler = new ClientHandlerThread(clientSocket);
            Thread clientHandlerThread=new Thread(clientHandler);
            clientHandlers.add(clientHandler);
             clientHandlerThread.start();
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }

    }

}

package hr.algebra.server;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.server.callable.ClientRoomHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.*;

public class Server {

    //private static ArrayList<ClientHandlerThread> clientHandlers = new ArrayList();
    private static HashMap<String, MatchmakingRoom> matchRooms=new HashMap<>();
    public static final String HOST = "localhost";
    public static final int PORT = 1987;

    public static void main(String[] args) {
    //todo make collection of players
    //todo make a dictionary of rooms with codes
        acceptRoomRequests();
    }

    private static void acceptRoomRequests() {
        CyclicBarrier cyclicBarrier=new CyclicBarrier(2);
        ExecutorService executorService= Executors.newCachedThreadPool();



        try(ServerSocket serverSocket=new ServerSocket(PORT)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (true){
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort()+"----"+clientSocket.getInetAddress());
                MatchmakingRoom matchmakingRoom=new MatchmakingRoom();
                Callable<MatchmakingRoom> callableMakeMatch=new ClientRoomHandler(clientSocket,cyclicBarrier, matchmakingRoom);
                Future<MatchmakingRoom> matchRoom=executorService.submit(callableMakeMatch);

            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


    }



}

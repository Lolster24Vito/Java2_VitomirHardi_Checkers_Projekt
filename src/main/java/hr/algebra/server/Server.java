package hr.algebra.server;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.*;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;
import hr.algebra.server.callable.ClientJoinRoomHandler;
import hr.algebra.server.callable.ClientMakeRoomHandler;
import hr.algebra.server.runnable.ClientTurnHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

public class Server {

    //private static ArrayList<ClientHandlerThread> clientHandlers = new ArrayList();

    public static HashMap<String, MatchmakingRoomInfo> matchRooms=new HashMap<>();



    public static final String HOST = "localhost";
    public static final int MAKE_ROOM_PORT = 1987;
    public static final int LISTEN_ROOM_PORT = 1998;
    public static final int LISTEN_TURN_PORT = 1999;



    public static void main(String[] args) {
    //todo make collection of players
    //todo make a dictionary of rooms with codes
        ExecutorService executorService =Executors.newFixedThreadPool(3);
        executorService.submit(Server::acceptMakeRoomRequests);
        executorService.submit(Server::acceptRoomJoinRequests);
        executorService.submit(Server::acceptMovesRequests);
        //acceptMakeRoomRequests();
        //acceptRoomJoinRequests();
    }

    private static void acceptMovesRequests() {
    ExecutorService executorService=Executors.newCachedThreadPool();
    try(ServerSocket serverSocket=new ServerSocket(LISTEN_TURN_PORT)) {
        System.err.println("Server listening for turn changes");
        while (true){
            Socket clientSocket=serverSocket.accept();
            System.err.println("Client turn connected from port: " + clientSocket.getPort()+"----"+clientSocket.getInetAddress());

            ClientTurnHandler clientTurnHandler=new ClientTurnHandler(clientSocket);
            executorService.submit(clientTurnHandler);

        }
    } catch (IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
    }
    }

    private static void acceptRoomJoinRequests() {
        ExecutorService executorService= Executors.newCachedThreadPool();
        try(ServerSocket serverSocket=new ServerSocket(LISTEN_ROOM_PORT)){
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (true){
                Socket clientSocket = serverSocket.accept();
                ClientJoinRoomHandler clientJoinRoomHandler=new ClientJoinRoomHandler(clientSocket);
                System.err.println("Client connected from port: " + clientSocket.getPort()+"----"+clientSocket.getInetAddress());

              /*  Runnable ClientJoinRoomHandler= () -> {
                    extracted(clientSocket);
                };*/
                executorService.execute(clientJoinRoomHandler);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private static void acceptMakeRoomRequests() {
        //CyclicBarrier cyclicBarrier=new CyclicBarrier(2);
        ExecutorService executorService= Executors.newCachedThreadPool();

        try(ServerSocket serverSocket=new ServerSocket(MAKE_ROOM_PORT)){
            System.err.println("Serverxxxxxxxxxxxxx listening on port: " + serverSocket.getLocalPort());
            while (true){
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort()+"----"+clientSocket.getInetAddress());
                Callable<MatchmakingRoomInfo> callableMakeMatch=new ClientMakeRoomHandler(clientSocket);
                MatchmakingRoomInfo matchRoom= executorService.submit(callableMakeMatch).get();
                System.out.println(matchRoom.getRoomCode());
                matchRooms.put(matchRoom.getRoomCode(),matchRoom);
            }
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }



}

package hr.algebra.server;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.RoomState;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;
import hr.algebra.server.callable.ClientMakeRoomHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.*;

public class Server {

    //private static ArrayList<ClientHandlerThread> clientHandlers = new ArrayList();
    private static HashMap<String, MatchmakingRoom> matchRooms=new HashMap<>();
    public static final String HOST = "localhost";
    public static final int MAKE_ROOM_PORT = 1987;
    public static final int LISTEN_ROOM_PORT = 1998;


    public static void main(String[] args) {
    //todo make collection of players
    //todo make a dictionary of rooms with codes
        ExecutorService executorService =Executors.newFixedThreadPool(2);
        executorService.submit(Server::acceptMakeRoomRequests);
        executorService.submit(Server::acceptRoomJoinRequests);
        //acceptMakeRoomRequests();
        //acceptRoomJoinRequests();
    }

    private static void acceptRoomJoinRequests() {
        ExecutorService executorService= Executors.newCachedThreadPool();
        try(ServerSocket serverSocket=new ServerSocket(LISTEN_ROOM_PORT)){
            System.err.println("Serversssssssssssssss listening on port: " + serverSocket.getLocalPort());
            while (true){
                Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected from port: " + clientSocket.getPort()+"----"+clientSocket.getInetAddress());
                Runnable ClientJoinRoomHandler= () -> {
                    try(
                            ObjectOutputStream oos=new ObjectOutputStream(clientSocket.getOutputStream());
                            ObjectInputStream ois=new ObjectInputStream(clientSocket.getInputStream());

                    ) {
                        LoginMessage loginMessage=(LoginMessage)ois.readObject();
                       System.out.println("read roomCode:"+loginMessage.getRoomCode());
                        RoomState roomState;
                        if(matchRooms.containsKey(loginMessage.getRoomCode())){
                            if(!matchRooms.get(loginMessage.getRoomCode()).isPlayerInMatch(loginMessage.getUsername())) {
                                matchRooms.get(loginMessage.getRoomCode()).addPlayer(new PlayerInfo(loginMessage.getUsername(), PlayerColor.black));
                            }
                            roomState = matchRooms.get(loginMessage.getRoomCode()).getRoomState();
                           System.out.println("wrote roomCode:"+loginMessage.getRoomCode());

                       }
                       else{
                           roomState=RoomState.NotExists;
                           System.out.println("wrote roomCode2:"+loginMessage.getRoomCode());
                       }

                        System.out.println("wroteBoolean");
                        oos.writeObject(roomState);
                        if(roomState==RoomState.ExistsAndEnoughPlayers){
                            oos.writeObject(matchRooms.get(loginMessage.getRoomCode()));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                };
                executorService.submit(ClientJoinRoomHandler);
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
              //  Callable<MatchmakingRoom> callableMakeMatch=new ClientRoomHandler(clientSocket,cyclicBarrier, matchmakingRoom);
                Callable<MatchmakingRoom> callableMakeMatch=new ClientMakeRoomHandler(clientSocket);
                MatchmakingRoom matchRoom= executorService.submit(callableMakeMatch).get();
                System.out.println(matchRoom.getRoomCode());
                matchRooms.put(matchRoom.getRoomCode(),matchRoom);
            }
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }


    }



}

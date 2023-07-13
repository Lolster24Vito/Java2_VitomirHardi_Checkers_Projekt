package hr.algebra.server.runnable;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.PlayerMoveSerializable;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.SerializableBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientTurnHandler implements Runnable {

    private static HashMap<String, MatchmakingRoom> matchmakingRooms=new HashMap<>();
    private SerializableBoard serializableBoard;
    private Socket clientSocket;
   private ObjectOutputStream oos ;
   private  ObjectInputStream ois;
    private PlayerInfo thisOnlinePlayer;



    private String roomCode;

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ClientTurnHandler(Socket clientSocket) {
        try{
        this.clientSocket=clientSocket;
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());
        thisOnlinePlayer=(PlayerInfo)ois.readObject();
        roomCode=(String)ois.readObject();

        if(!matchmakingRooms.containsKey(roomCode)){
            matchmakingRooms.put(roomCode,new MatchmakingRoom());
        }
            matchmakingRooms.get(roomCode).addClientTurnHandler(this);

        oos.writeObject( matchmakingRooms.get(roomCode).getSerializableBoard());

        } catch (Exception e) {
            e.printStackTrace();
            closeEverything();
        }

    }
    public PlayerInfo getThisOnlinePlayer() {
        return thisOnlinePlayer;
    }

    private void closeEverything() {
        try {
            clientSocket.close();
            oos.close();
            ois.close();
        } catch (IOException e) {
            System.out.println("AlreadyClosed");
        }

    }


    @Override
    public void run() {
        try {
    while (clientSocket.isConnected()){
         PlayerMoveSerializable playerMoveSerializable=(PlayerMoveSerializable) ois.readObject();
          serializableBoard=(SerializableBoard)ois.readObject();

            matchmakingRooms.get(roomCode).addMove(playerMoveSerializable);
            matchmakingRooms.get(roomCode).setSerializableBoard(serializableBoard);
            matchmakingRooms.get(roomCode).broadcastMove(playerMoveSerializable);
    }
        } catch (Exception e) {
            matchmakingRooms.get(roomCode).removeClientTurnHandler(this);
            System.out.println("Error reading object");
            closeEverything();
          //  e.printStackTrace();
        }
    }

}

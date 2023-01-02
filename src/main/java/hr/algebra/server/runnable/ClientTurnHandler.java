package hr.algebra.server.runnable;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.PlayerMoveSerializable;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientTurnHandler implements Runnable {

    private static HashMap<String, MatchmakingRoom> matchmakingRooms=new HashMap<>();
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
        } catch (Exception e) {
            closeEverything();
            e.printStackTrace();
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {
    while (true){
        try {

    PlayerMoveSerializable playerMoveSerializable=(PlayerMoveSerializable) ois.readObject();

            matchmakingRooms.get(roomCode).broadcastMove(playerMoveSerializable);

        } catch (Exception e) {
            System.out.println("errorr ReadObject");
            closeEverything();
            e.printStackTrace();
           // throw new RuntimeException(e);
        }
    }
    }

}

package hr.algebra.java2_vitomirhardi_checkers_projekt.Online.client;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoomInfo;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.PlayerMoveSerializable;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.client.interfaces.MoveReader;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.SerializableBoard;
import hr.algebra.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerConnection implements Runnable {
    private PlayerInfo playerInfo;
    private Socket clientSocket;
    private ObjectOutputStream oos ;
    private ObjectInputStream ois;
    private final MatchmakingRoomInfo matchmakingRoomInfo;
    private MoveReader moveReader;

    private SerializableBoard serializableBoard;
    private boolean shouldRun=true;

    public SerializableBoard getSerializableBoard() {
        return serializableBoard;
    }

    public PlayerConnection(PlayerInfo thisOnlinePlayer, MatchmakingRoomInfo matchmakingRoomInfo, MoveReader moveReader) throws IOException, ClassNotFoundException {

        clientSocket = new Socket(Server.HOST, Server.LISTEN_TURN_PORT);
         oos = new ObjectOutputStream(clientSocket.getOutputStream());
         ois = new ObjectInputStream(clientSocket.getInputStream());
        this.matchmakingRoomInfo=matchmakingRoomInfo;
        oos.writeObject(thisOnlinePlayer);
        oos.writeObject(matchmakingRoomInfo.getRoomCode());
        serializableBoard=(SerializableBoard)ois.readObject();
        this.moveReader=moveReader;
    }
    public void writePlayerMove(PlayerMoveSerializable playerMove, SerializableBoard serializableBoard) throws IOException {
        oos.writeObject(playerMove);
        oos.writeObject(serializableBoard);
    }
    public void closeConnection(){
        try {
            clientSocket.close();
            oos.close();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
    public String getRoomCode(){
        return matchmakingRoomInfo.getRoomCode();
    }

    @Override
    public void run() {
        while (shouldRun){
            try{
                PlayerMoveSerializable playerMoveSerializable=(PlayerMoveSerializable) ois.readObject();
                System.out.println("ReadObject");
                moveReader.madeMove(playerMoveSerializable);
            } catch (Exception e) {
                System.out.println("errorr ReadObject");
                closeConnection();
            }
        }
    }
}

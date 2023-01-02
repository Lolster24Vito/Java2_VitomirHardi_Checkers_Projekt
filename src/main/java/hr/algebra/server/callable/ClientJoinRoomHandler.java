package hr.algebra.server.callable;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.RoomPing;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.RoomState;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientJoinRoomHandler implements Callable {

    private  static  List<hr.algebra.java2_vitomirhardi_checkers_projekt.Online.client.ClientJoinRoomHandler>
    private Socket clientSocket;
    private ObjectOutputStream oos ;
    private ObjectInputStream ois;
    private PlayerInfo thisOnlinePlayer;

    public ClientJoinRoomHandler(Socket clientSocket) {
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

    @Override
    public Object call() throws Exception {
        try(
                ObjectOutputStream oos=new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois=new ObjectInputStream(clientSocket.getInputStream());

        ) {
            LoginMessage loginMessage=(LoginMessage)ois.readObject();

            /*
            System.out.println("read roomCode:"+loginMessage.getRoomCode());
            RoomState roomState;
            RoomPing roomPing;
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
            if(roomState==RoomState.ExistsAndEnoughPlayers){
                roomPing=new RoomPing(roomState,matchRooms.get(loginMessage.getRoomCode()));
                oos.writeObject(roomPing);

            }
            else {
                roomPing=new RoomPing(roomState);
                oos.writeObject(roomPing);
            }
            */

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

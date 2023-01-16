package hr.algebra.server.callable;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.RoomPing;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.RoomState;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;
import hr.algebra.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ClientJoinRoomHandler implements Runnable {

    private static ArrayList<ClientJoinRoomHandler> clientJoinRoomHandlersList=new ArrayList<>();
    private Socket clientSocket;
    private ObjectOutputStream oos ;
    private ObjectInputStream ois;
    private PlayerInfo thisOnlinePlayer;
    private LoginMessage loginMessage;

    public ClientJoinRoomHandler(Socket clientSocket) {
        try{
            this.clientSocket=clientSocket;
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void closeConnection() {
        try {
            oos.close();
        } catch (IOException e) {
            System.err.println("couldnt close oos");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try {
            ois.close();
        } catch (IOException e) {
            System.err.println("couldnt close ois");
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }



    @Override
    public void run() {
        RoomState roomState;
        RoomPing roomPing;
        try{
            do {
                loginMessage = (LoginMessage) ois.readObject();

                if (Server.matchRooms.containsKey(loginMessage.getRoomCode())) {
                    if (!Server.matchRooms.get(loginMessage.getRoomCode()).isPlayerInMatch(loginMessage.getUsername())) {
                        Server.matchRooms.get(loginMessage.getRoomCode()).addPlayer(new PlayerInfo(loginMessage.getUsername(), PlayerColor.black));
                    }
                    roomState = Server.matchRooms.get(loginMessage.getRoomCode()).getRoomState();

                } else {
                    roomState = RoomState.NotExists;
                }

                System.out.println("wroteBoolean");
                if (roomState == RoomState.ExistsAndEnoughPlayers) {
                    roomPing = new RoomPing(roomState, Server.matchRooms.get(loginMessage.getRoomCode()));
                    oos.writeObject(roomPing);
                } else {
                    roomPing = new RoomPing(roomState);
                    oos.writeObject(roomPing);
                }
            }while (!clientSocket.isClosed()&&roomState==RoomState.ExistsAndWaitingForPlayers);


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            closeConnection();

        }
        closeConnection();

    }
}

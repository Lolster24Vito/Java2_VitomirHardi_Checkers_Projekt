package hr.algebra.java2_vitomirhardi_checkers_projekt.Online.client;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.RoomState;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientJoinRoomHandler implements  Runnable{

    private LoginMessage loginMessage;
    private ObjectOutputStream oos ;
    private ObjectInputStream ois;
    private PlayerInfo thisOnlinePlayer;
    private Socket clientSocket;

    public ClientJoinRoomHandler(LoginMessage loginMessage) {
        this.loginMessage = loginMessage;
    }

    @Override
    public void run() {

        RoomState roomState = RoomState.ExistsAndWaitingForPlayers;
        roomState = getRoomState(loginMessage);

        while (roomState != RoomState.ExistsAndEnoughPlayers && roomState != RoomState.NotExists) {

        }
           /* RoomState roomState = RoomState.ExistsAndWaitingForPlayers;
            while (roomState != RoomState.ExistsAndWaitingForPlayers || roomState != RoomState.NotExists) {

                roomState = getRoomState(loginMessage);

                if (roomState == RoomState.ExistsAndWaitingForPlayers) {
                    lbRoomStatus.setText("Waiting game");
                }
                if (roomState == RoomState.ExistsAndEnoughPlayers)
                    lbRoomStatus.setText("Joining game");
                else {
                    lbRoomStatus.setText("Failed to join game");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }


            }

        }*/
    }
}

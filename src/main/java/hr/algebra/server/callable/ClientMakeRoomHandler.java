package hr.algebra.server.callable;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ClientMakeRoomHandler implements Callable<MatchmakingRoom> {

    private Socket clientSocket;
    //private CyclicBarrier cyclicBarrier;
  //  private CountDownLatch countDownLatch;

    public ClientMakeRoomHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    @Override
    public MatchmakingRoom call() throws Exception {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            //oos.flush();
            LoginMessage loginMessage = (LoginMessage) ois.readObject();
            System.out.println("Received new game state DTO with the name: " + loginMessage.getUsername());
            MatchmakingRoom matchmakingRoom=new MatchmakingRoom(loginMessage.getRoomCode());
            matchmakingRoom.addPlayer(new PlayerInfo(loginMessage.getUsername(), PlayerColor.white));
            oos.writeObject(matchmakingRoom);
            return matchmakingRoom;
        }
        catch(IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

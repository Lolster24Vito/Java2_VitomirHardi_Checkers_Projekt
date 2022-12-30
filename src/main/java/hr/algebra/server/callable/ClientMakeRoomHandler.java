package hr.algebra.server.callable;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoomInfo;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientMakeRoomHandler implements Callable<MatchmakingRoomInfo> {

    private Socket clientSocket;
    //private CyclicBarrier cyclicBarrier;
  //  private CountDownLatch countDownLatch;

    public ClientMakeRoomHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    @Override
    public MatchmakingRoomInfo call() throws Exception {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            //oos.flush();
            LoginMessage loginMessage = (LoginMessage) ois.readObject();
            System.out.println("Received new game state DTO with the name: " + loginMessage.getUsername());
            MatchmakingRoomInfo matchmakingRoomInfo =new MatchmakingRoomInfo(loginMessage.getRoomCode());
            matchmakingRoomInfo.addPlayer(new PlayerInfo(loginMessage.getUsername(), PlayerColor.white));
            oos.writeObject(matchmakingRoomInfo);
            return matchmakingRoomInfo;
        }
        catch(IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

package hr.algebra.server.callable;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

public class ClientRoomHandler implements Callable<MatchmakingRoom> {

    private Socket clientSocket;
    private CyclicBarrier cyclicBarrier;

   private MatchmakingRoom matchmakingRoom;
    public ClientRoomHandler(Socket clientSocket, CyclicBarrier cyclicBarrier, MatchmakingRoom matchmakingRoom) {
        this.clientSocket = clientSocket;
        this.cyclicBarrier = cyclicBarrier;
        this.matchmakingRoom = matchmakingRoom;
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
            System.out.println("waiting");
            matchmakingRoom.setRoomCode(loginMessage.getRoomCode());
            if(matchmakingRoom.getPlayerCount()<1){
                matchmakingRoom.addPlayer(new PlayerInfo(loginMessage.getUsername(), PlayerColor.white));
            }
            else{
                matchmakingRoom.addPlayer(new PlayerInfo(loginMessage.getUsername(), PlayerColor.black));

            }

            cyclicBarrier.await();
            System.out.println("Beyond wait ");
            oos.writeObject(matchmakingRoom);
            return matchmakingRoom;
        }
        catch(IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}

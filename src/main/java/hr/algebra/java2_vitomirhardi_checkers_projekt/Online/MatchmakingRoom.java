package hr.algebra.java2_vitomirhardi_checkers_projekt.Online;

import hr.algebra.server.callable.ClientTurnHandler;

import java.io.IOException;
import java.util.ArrayList;

public class MatchmakingRoom {
    private ArrayList<ClientTurnHandler> players=new ArrayList<>();

    public MatchmakingRoom() {
    }
    public  void addClientTurnHandler(ClientTurnHandler clientTurnHandler){
        players.add(clientTurnHandler);
    }
    public void broadcastMove(PlayerMoveSerializable playerMoveSerializable){
        for (ClientTurnHandler clientHandler:players
             ) {
            try {
                if(!clientHandler.getThisOnlinePlayer().getPlayerName().equals(
                        playerMoveSerializable.getPlayerInfo().getPlayerName())){
                    clientHandler.getOos().writeObject(playerMoveSerializable);
                }
                else{
                    System.out.println("didn't wrote to");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

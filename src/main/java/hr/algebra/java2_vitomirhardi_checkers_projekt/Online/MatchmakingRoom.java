package hr.algebra.java2_vitomirhardi_checkers_projekt.Online;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.SerializableBoard;
import hr.algebra.server.runnable.ClientTurnHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchmakingRoom {
    private ArrayList<ClientTurnHandler> players=new ArrayList<>();

    public SerializableBoard getSerializableBoard() {
        return serializableBoard;
    }

    public void setSerializableBoard(SerializableBoard serializableBoard) {
        this.serializableBoard = serializableBoard;
    }

    private SerializableBoard serializableBoard;
    private List<PlayerMoveSerializable> moves=new ArrayList<>();


    public MatchmakingRoom() {
    }
    public  void addClientTurnHandler(ClientTurnHandler clientTurnHandler){
        players.add(clientTurnHandler);
    }
    public  void removeClientTurnHandler(ClientTurnHandler clientTurnHandler){
        players.remove(clientTurnHandler);

    }
    public void broadcastMove(PlayerMoveSerializable playerMoveSerializable){
        for (ClientTurnHandler clientHandler:players
             ) {
            try {
                if(!clientHandler.getThisOnlinePlayer().getPlayerName().equals(
                        playerMoveSerializable.getPlayerInfo().getPlayerName())){
                    clientHandler.getOos().writeObject(playerMoveSerializable);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void addMove(PlayerMoveSerializable playerMoveSerializable) {
        moves.add(playerMoveSerializable);
    }



}

package hr.algebra.java2_vitomirhardi_checkers_projekt.Online;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchmakingRoomInfo implements Serializable {
    public MatchmakingRoomInfo(String roomCode) {
        this.roomCode = roomCode;
    }

    public RoomState getRoomState() {
        if(playersList.size()<2){
            return RoomState.ExistsAndWaitingForPlayers;
        }
        else{
            return RoomState.ExistsAndEnoughPlayers;
        }
    }

    public MatchmakingRoomInfo() {
    }

    private List<PlayerInfo> playersList=new ArrayList<>();
    public synchronized String getRoomCode() {
        return roomCode;
    }
    public   Optional<PlayerInfo> getPlayerByName(String playername){
       return playersList.stream().filter(p->p.getPlayerName().equals(playername)).findFirst();
    }

public synchronized  void addPlayer(PlayerInfo playerInfo){
        playersList.add(playerInfo);
    }

    public synchronized void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }
    public synchronized int getPlayerCount(){
        return playersList.size();
    }

    private String roomCode;

    public boolean isPlayerInMatch(String username) {
       return playersList.stream().anyMatch(p->p.getPlayerName().equals(username));
    }

    public Optional<PlayerInfo> getWhitePlayer() {
            Optional<PlayerInfo> firstWhite = playersList.stream().filter(p -> p.getColor() == PlayerColor.white).findFirst();
           return firstWhite;

        }

    public Optional<PlayerInfo> getBlackPlayer() {
        Optional<PlayerInfo> firstBlack = playersList.stream().filter(p -> p.getColor() == PlayerColor.black).findFirst();
       return firstBlack;
    }
}


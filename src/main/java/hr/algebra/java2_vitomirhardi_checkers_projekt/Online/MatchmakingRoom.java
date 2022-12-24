package hr.algebra.java2_vitomirhardi_checkers_projekt.Online;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MatchmakingRoom implements Serializable {
    public MatchmakingRoom(String roomCode) {
        this.roomCode = roomCode;
    }

    public MatchmakingRoom() {
    }

    private List<PlayerInfo> playersList=new ArrayList<>();
    public synchronized String getRoomCode() {
        return roomCode;
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

    String roomCode;
}

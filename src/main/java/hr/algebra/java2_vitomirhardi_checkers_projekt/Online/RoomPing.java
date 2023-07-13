package hr.algebra.java2_vitomirhardi_checkers_projekt.Online;

import java.io.Serializable;

public class RoomPing implements Serializable {
    private RoomState roomState;
    private MatchmakingRoomInfo matchmakingRoomInfo =null;


    public RoomPing(RoomState roomState) {
        this.roomState = roomState;
    }

    public RoomPing(RoomState roomState, MatchmakingRoomInfo matchmakingRoomInfo) {
        this.roomState = roomState;
        this.matchmakingRoomInfo = matchmakingRoomInfo;
    }

    public RoomState getRoomState() {
        return roomState;
    }

    public void setRoomState(RoomState roomState) {
        this.roomState = roomState;
    }

    public MatchmakingRoomInfo getMatchmakingRoom() {
        return matchmakingRoomInfo;
    }
}

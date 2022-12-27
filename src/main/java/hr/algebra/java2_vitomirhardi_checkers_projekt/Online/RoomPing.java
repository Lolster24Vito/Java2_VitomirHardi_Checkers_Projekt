package hr.algebra.java2_vitomirhardi_checkers_projekt.Online;

public class RoomPing {
    private RoomState roomState;
    private MatchmakingRoom matchmakingRoom=null;

    public RoomPing(RoomState roomState) {
        this.roomState = roomState;
    }

    public RoomPing(RoomState roomState, MatchmakingRoom matchmakingRoom) {
        this.roomState = roomState;
        this.matchmakingRoom = matchmakingRoom;
    }

    public RoomState getRoomState() {
        return roomState;
    }

    public void setRoomState(RoomState roomState) {
        this.roomState = roomState;
    }

    public MatchmakingRoom getMatchmakingRoom() {
        return matchmakingRoom;
    }
}

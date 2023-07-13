package hr.algebra.java2_vitomirhardi_checkers_projekt.Online;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PieceData;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.TileData;

import java.io.Serializable;

public class PlayerMoveSerializable implements Serializable {
    private final TileData selectedTile;
    private final TileData moveToTile;
    private final PieceData pieceData;



    private final String roomCode;
    private PlayerInfo playerInfo;

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public PlayerMoveSerializable(TileData selectedTile, TileData moveToTile, String roomCode,PlayerInfo playerInfo,PieceData pieceData) {
        this.selectedTile = selectedTile;
        this.moveToTile = moveToTile;
        this.roomCode = roomCode;
        this.playerInfo=playerInfo;
        this.pieceData=pieceData;
    }

    public TileData getSelectedTile() {
        return selectedTile;
    }
    public PieceData getPieceData() {
        return pieceData;
    }
    public TileData getMoveToTile() {
        return moveToTile;
    }

    public String getRoomCode() {
        return roomCode;
    }
}

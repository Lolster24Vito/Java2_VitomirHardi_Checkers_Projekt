package hr.algebra.java2_vitomirhardi_checkers_projekt.models;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.Position;


import java.io.Serializable;

public class PieceData implements Serializable {
    Position pos;
    private Boolean isKing=false;
    PlayerColor pieceColor;
    Boolean isAlive=true;

    public void setPosition(Position pos) {
        this.pos = pos;
    }
    public Position getPos() {
        return pos;
    }
    public PlayerColor getPieceColor() {
        return pieceColor;
    }
    public Boolean getIsKing() {
        return isKing;
    }
    public void setKing() {
        isKing = true;
    }

    public PieceData(Position pos, Boolean isKing, PlayerColor pieceColor) {
        this.pos = pos;
        this.isKing = isKing;
        this.pieceColor = pieceColor;
    }
}

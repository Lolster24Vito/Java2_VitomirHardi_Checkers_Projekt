package hr.algebra.java2_vitomirhardi_checkers_projekt.models;


import java.io.Serializable;

public class PieceData implements Serializable {
    Position pos;
    private Boolean isKing=false;
    PlayerColor pieceColor;
    Boolean isAlive=true;

    public PieceData(PieceData piece) {
        this.pos=new Position(piece.pos.getX(),piece.pos.getY());
        this.isKing=piece.isKing;
        this.pieceColor=piece.pieceColor;
        this.isAlive=piece.isAlive;
    }

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
    public void setAsKing() {
        isKing = true;
    }

    public void setKing(Boolean king) {
        isKing = king;
    }

    public void setAlive(Boolean alive) {
        isAlive = alive;
    }

    public PieceData(Position pos, Boolean isKing, PlayerColor pieceColor) {
        this.pos = pos;
        this.isKing = isKing;
        this.pieceColor = pieceColor;
    }
}

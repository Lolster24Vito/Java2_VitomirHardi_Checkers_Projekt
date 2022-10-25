package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

public class PieceData {
    Position pos;
    Boolean isKing=false;
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
    public Boolean getKing() {
        return isKing;
    }

    public PieceData(Position pos, Boolean isKing, PlayerColor pieceColor) {
        this.pos = pos;
        this.isKing = isKing;
        this.pieceColor = pieceColor;
    }
}

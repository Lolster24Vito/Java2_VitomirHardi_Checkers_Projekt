package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import java.util.ArrayList;
import java.util.List;

public class PlayerMove {
    Tile fromTile;

    public PlayerMove(Tile fromTile, Tile toTile) {
        this.fromTile = fromTile;
        this.toTile = toTile;
    }

    Tile toTile;
    Piece movedPiece;
    List<Piece> eatenPieces=new ArrayList<>();

    @Override
    public String toString() {
        return "fromTile=" + fromTile +
                ", toTile=" + toTile;
    }

    public Tile getFromTile() {
        return fromTile;
    }

    public void setFromTile(Tile fromTile) {
        this.fromTile = fromTile;
    }

    public Tile getToTile() {
        return toTile;
    }

    public void setToTile(Tile toTile) {
        this.toTile = toTile;
    }

    public Piece getMovedPiece() {
        return movedPiece;
    }

    public void setMovedPiece(Piece movedPiece) {
        this.movedPiece = movedPiece;
    }

    public List<Piece> getEatenPieces() {
        return eatenPieces;
    }

    public void setEatenPieces(List<Piece> eatenPieces) {
        this.eatenPieces = eatenPieces;
    }
}

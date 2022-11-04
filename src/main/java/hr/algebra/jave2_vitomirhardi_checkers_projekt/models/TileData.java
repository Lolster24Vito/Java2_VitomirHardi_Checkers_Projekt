package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import java.io.Serializable;

public class TileData implements Serializable {
    public TileData(Position position) {
        this.position = position;
    }

    private final Position position;
    private PieceData piece;

    public PieceData getPiece() {
        return piece;
    }

    public Position getPosition() {
        return position;
    }
    public boolean hasPiece() {
        return piece != null;

    }

    public void setPiece(PieceData piece) {
        this.piece = piece;
    }

    public TileType getTileType(){
        if(piece!=null){
            if (piece.getPieceColor().equals(PlayerColor.black)) {
                return TileType.blackPiece;
            }
            if (piece.getPieceColor().equals(PlayerColor.white)) {
                return TileType.whitePiece;
            }
        }
        return TileType.empty;
    }

}

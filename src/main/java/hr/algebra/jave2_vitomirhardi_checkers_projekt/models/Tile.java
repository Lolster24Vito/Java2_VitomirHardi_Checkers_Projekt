package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private Piece piece;
    int tileLocation;

   private final Position position;

    @Override
    public String toString() {
        return"position(" + position.getxPos() +", "+position.getyPos()+")";
    }

    public Position getPosition() {
        return position;
    }

    public Tile(double v, double v1, double v2, double v3, Position position,int tileLocation) {
        super(v, v1, v2, v3);
        this.position = position;
        this.tileLocation=tileLocation;
    }

    public int getTileLocation() {
        return tileLocation;
    }

    public boolean hasPiece() {
        return piece != null;

    }
    public TileType getTileType(){
       if(piece!=null){
            if (piece.getPieceColor().equals(PlayerColor.black)) {
                return TileType.black;
            }
            if (piece.getPieceColor().equals(PlayerColor.white)) {
                return TileType.white;
            }
        }
        return TileType.empty;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;

    }


    public void movePiecePosition() {

    }
}

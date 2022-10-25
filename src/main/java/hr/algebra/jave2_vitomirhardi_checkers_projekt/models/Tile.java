package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    //TileInfo
TileData tileData;
Piece piece;
    int tileLocation;


    @Override
    public String toString() {
        return"position(" + tileData.getPosition().getX() +", "+tileData.getPosition().getY()+")";
    }

    public Position getPosition() {
        return tileData.getPosition() ;
    }

    public Tile(double v, double v1, double v2, double v3, Position position,int tileLocation) {
        super(v, v1, v2, v3);
        tileData=new TileData(position);
        this.tileLocation=tileLocation;
    }

    public int getTileLocation() {
        return tileLocation;
    }

    public boolean hasPiece() {
        return tileData.getPiece() != null;

    }
    public TileType getTileType(){
     return tileData.getTileType();
    }

    public Piece getPiece() {
        return piece;
    }

    public TileData getTileData() {
        return tileData;
    }

    public void setPiece(Piece piece) {
        if(piece!=null)
        this.tileData.setPiece(piece.getPieceData());
        else
            this.tileData.setPiece(null);

        this.piece = piece;
    }
    public PlayerColor getPieceColor(){
        return piece.getPieceColor();
    }

}

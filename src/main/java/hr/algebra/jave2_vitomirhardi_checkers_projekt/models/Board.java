package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public Board(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tile[][] tiles;
    private List<Piece> whitePieces = new ArrayList<>();
    private List<Piece> blackPieces = new ArrayList<>();

    public void addWhitePiece(Piece piece) {
        whitePieces.add(piece);
    }

    public void addBlackPiece(Piece piece) {
        blackPieces.add(piece);
    }


    public List<Position> getMovesForKill(Position piecePosition, int yDir) {
return new ArrayList<Position>();
      /*
        List<Position> positions=new ArrayList<>();
        Tile tile = tiles[piecePosition.getxPos()][piecePosition.getyPos()];
        //leftTile
        Tile lT = tiles[piecePosition.getxPos() - 1][piecePosition.getyPos() + yDir];
        //rightTile
        Tile rT = tiles[piecePosition.getxPos() + 1][piecePosition.getyPos() + yDir];
        if(!lT.hasPiece())positions.add(lT.getPosition());
        if(!rT.hasPiece())positions.add(rT.getPosition());
        */

//return positions;
    }

    public boolean checkIfEmptyTile(Position dir, Position piecePosition) {
       return tiles[piecePosition.getxPos()][piecePosition.getyPos()].hasPiece();
    }
}


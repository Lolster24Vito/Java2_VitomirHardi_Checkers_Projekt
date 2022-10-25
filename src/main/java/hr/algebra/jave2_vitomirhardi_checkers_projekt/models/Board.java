package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class Board {
    //tile zamjeniti sa tileInfo
    public Board(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tile[][] tiles;

    private List<PieceData>eatenWhitePieces=new ArrayList<>();
    private List<PieceData>eatenBlackPieces=new ArrayList<>();



     public ArrayList<PlayerMove> getLegalJumpsFrom(PlayerColor playerTurn,PieceData piece) {
          //    private boolean canJump(PlayerColor playerTurn, PieceData piece, Position p1, Position p2) {
            ArrayList<PlayerMove> moves = new ArrayList<PlayerMove>();  // The legal jumps will be stored in this list.
         Position piecePos=piece.pos;
         if (canJump(playerTurn, piece, piecePos.getTopRight(), piecePos.getTopRight(2)))
             moves.add(new PlayerMove(piece, piecePos.getTopRight(2),true));

         if (canJump(playerTurn, piece, piecePos.getTopLeft(), piecePos.getTopLeft(2)))
             moves.add(new PlayerMove(piece, piecePos.getTopLeft(2),true));

         if (canJump(playerTurn, piece, piecePos.getBottomRight(), piecePos.getBottomRight(2)))
             moves.add(new PlayerMove(piece, piecePos.getBottomRight(2),true));

         if (canJump(playerTurn, piece, piecePos.getBottomLeft(), piecePos.getBottomLeft(2)))
             moves.add(new PlayerMove(piece, piecePos.getBottomLeft(2),true));

            return moves;
        }  // end getLegalJumpsFrom()
/*
        CheckersMove[] getLegalMoves(int player) {
 */



    public boolean checkIfEmptyTile(Position dir, Position piecePosition) {
       return tiles[piecePosition.getX()][piecePosition.getY()].hasPiece();
    }
    private boolean canJump(PlayerColor playerTurn, PieceData piece, Position p1, Position p2) {

        if (p2.getX() < 0 || p2.getX() >= 8 || p2.getY() < 0 || p2.getY() >= 8)
            return false;  // (r3,c3) is off the board.

        Tile tile2=tiles[p1.getX()][p1.getY()];
        Tile tile3=tiles[p2.getX()][p2.getY()];

        if (tile3.hasPiece())
            return false;  // (r3,c3) already contains a piece.
        //cannot kill another piece is in the way

        if (playerTurn == PlayerColor.black) {
            if (piece.getPieceColor()==PlayerColor.black && p2.getY() > piece.pos.getY())
                return false;  // Regular red piece can only move up.
            if(!tile2.hasPiece())return false;
            if (tile2.hasPiece()&&tile2.getPieceColor() == PlayerColor.black )
                return false;  // There is no black piece to jump.
            return true;  // The jump is legal.
        }
        else {
            if (piece.getPieceColor() == PlayerColor.white && p2.getY() < p2.getY())
                return false;  // Regular black piece can only move downn.
            if(!tile2.hasPiece()) return false;
            if (tile2.hasPiece()&&tile2.getPieceColor() == PlayerColor.white)
                return false;  // There is no red piece to jump.
            return true;  // The jump is legal.
        }

    }

    // end canJump()
    public boolean canMove(PlayerColor playerTurn, PieceData piece, Position pos2) {

        if (pos2.getX() < 0 || pos2.getX() >= 8 || pos2.getY() < 0 || pos2.getY() >= 8)
            return false;  // (r2,c2) is off the board.

        Tile tileAtPos=tiles[pos2.getX()][pos2.getY()];
        if (tileAtPos.hasPiece() == true)
            return false;  // (r2,c2) already contains a piece.

        if (playerTurn.equals(PlayerColor.white)) {
            if (piece.getPieceColor().equals(PlayerColor.white) && pos2.getY() < piece.pos.getY())
                return false;  // Regular red piece can only move down.
            return true;  // The move is legal.
        }
        else {
            if (piece.getPieceColor().equals(PlayerColor.black) && pos2.getY() > piece.pos.getY())
                return false;  // Regular black piece can only move up.
            return true;  // The move is legal.
        }

    }

    public ArrayList<PlayerMove> getLegalMovesFrom(PlayerColor playerTurn,PieceData piece) {
        ArrayList<PlayerMove> moves=new ArrayList<>();
        if (piece.getPieceColor().equals(playerTurn)) {
            Position piecePos=piece.getPos();
            if (canMove(playerTurn,piece,piecePos.getTopRight()))
                moves.add(new PlayerMove(piece,piecePos.getTopRight()));
            if (canMove(playerTurn,piece,piecePos.getTopLeft()))
                moves.add(new PlayerMove(piece,piecePos.getTopLeft()));
            if (canMove(playerTurn,piece,piecePos.getBottomRight()))
                moves.add(new PlayerMove(piece,piecePos.getBottomRight()));
            if (canMove(playerTurn,piece,piecePos.getBottomLeft()))
                moves.add(new PlayerMove(piece,piecePos.getBottomLeft()));
        }
        return moves;

    }


    public void addJump(PieceData piece) {
        if(piece.getPieceColor().equals(PlayerColor.black)){
            eatenBlackPieces.add(piece);
        }
        else {
            eatenWhitePieces.add(piece);
        }
    }
}


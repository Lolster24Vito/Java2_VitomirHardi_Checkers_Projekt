package hr.algebra.java2_vitomirhardi_checkers_projekt.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Board {
    //tile zamjeniti sa tileInfo
    public Board(int x_row_size, int y_column_size) {
        X_ROW_SIZE = x_row_size;
        Y_COLUMN_SIZE = y_column_size;
        this.tiles = new Tile[X_ROW_SIZE][Y_COLUMN_SIZE];
    }


    public final int X_ROW_SIZE;
    public final int Y_COLUMN_SIZE;
    public Tile[][] tiles;


    private int eatenWhitePieces = 0;
    private int eatenWhiteKings=0;
    private int eatenBlackPieces =0;
    private int eatenBlackKings =0;


    public int getEatenWhitePieces() {
        return eatenWhitePieces;
    }
    public int getEatenWhiteKings(){
        return eatenWhiteKings;
    }

    public int getEatenBlackPieces() {
        return eatenBlackPieces;
    }
    public int getEatenBlackKings() {
        return eatenBlackKings;
    }

    public void setEatenWhitePieces(int eatenWhitePieces,int eatenWhiteKings) {
        this.eatenWhitePieces = eatenWhitePieces;
        this.eatenWhiteKings=eatenWhiteKings;
    }

    public void setEatenBlackPieces(int eatenBlackPieces,int eatenBlackKings) {
        this.eatenBlackPieces = eatenBlackPieces;
        this.eatenBlackKings=eatenBlackKings;
    }



    public ArrayList<PlayerMove> getLegalJumpsFrom(PlayerColor playerTurn, PieceData piece) {
        //    private boolean canJump(PlayerColor playerTurn, PieceData piece, Position p1, Position p2) {
        ArrayList<PlayerMove> moves = new ArrayList<PlayerMove>();  // The legal jumps will be stored in this list.
        Position piecePos = piece.pos;


        if(piece.getIsKing()){
            Position direction= getDirectionFromNeighbours(piecePos, piecePos.getTopRight());

            moves.addAll(getKingJumps(playerTurn, piece, piecePos,getDirectionFromNeighbours(piecePos, piecePos.getTopRight())));
            moves.addAll(getKingJumps(playerTurn, piece, piecePos,getDirectionFromNeighbours(piecePos, piecePos.getTopLeft())));
            moves.addAll(getKingJumps(playerTurn, piece, piecePos,getDirectionFromNeighbours(piecePos, piecePos.getBottomLeft())));
            moves.addAll(getKingJumps(playerTurn, piece, piecePos,getDirectionFromNeighbours(piecePos, piecePos.getBottomRight())));


        }
        else {
            if (canJump(playerTurn, piece, piecePos.getTopRight(), piecePos.getTopRight(2)))
                moves.add(new PlayerMove(piece,piecePos.getTopRight(), piecePos.getTopRight(2), true));

            if (canJump(playerTurn, piece, piecePos.getTopLeft(), piecePos.getTopLeft(2)))
                moves.add(new PlayerMove(piece,piecePos.getTopLeft(), piecePos.getTopLeft(2), true));

            if (canJump(playerTurn, piece, piecePos.getBottomRight(), piecePos.getBottomRight(2)))
                moves.add(new PlayerMove(piece,piecePos.getBottomRight(), piecePos.getBottomRight(2), true));

            if (canJump(playerTurn, piece, piecePos.getBottomLeft(), piecePos.getBottomLeft(2)))
                moves.add(new PlayerMove(piece,piecePos.getBottomLeft(), piecePos.getBottomLeft(2), true));
        }
        return moves;
    }

    private static Position getDirectionFromNeighbours(Position piecePos, Position globalDirectionPosition) {
        return new Position(globalDirectionPosition.getX() - piecePos.getX(),
                globalDirectionPosition.getY() - piecePos.getY());
    }

    private Collection<PlayerMove> getKingJumps(PlayerColor playerTurn, PieceData piece, Position piecePos, Position direction) {
        Collection<PlayerMove> kingJumps=new ArrayList<>();
boolean ableToMove=false;
        for (int i = 1; i <X_ROW_SIZE ; i++) {

            Position checkPosition=piecePos.getPosInDirection(direction,i);
            Position check2Position=piecePos.getPosInDirection(direction,i+1);
            boolean isEmptyLine=true;
            for (int j = i-1; j >0 ; j--) {
                isEmptyLine=canMove(playerTurn,piece,piecePos.getPosInDirection(direction,j));
                if(!isEmptyLine)break;
            }
            ableToMove=canJump(playerTurn, piece, checkPosition,check2Position);
            if (ableToMove&&isEmptyLine) {
                Position canMovePos=Position.subtract(checkPosition,direction);
                kingJumps.add(new PlayerMove(piece,checkPosition,check2Position,true));
            }

        }


        return kingJumps;
    }


    public boolean checkIfEmptyTile(Position dir, Position piecePosition) {
        return tiles[piecePosition.getX()][piecePosition.getY()].hasPiece();
    }

    private boolean canJump(PlayerColor playerTurn, PieceData piece, Position p1, Position p2) {

        if (p2.getX() < 0 || p2.getX() >= 8 || p2.getY() < 0 || p2.getY() >= 8)
            return false;  // (p2,p2) is off the board.

        Tile tile2 = tiles[p1.getX()][p1.getY()];
        Tile tile3 = tiles[p2.getX()][p2.getY()];

        if (tile3.hasPiece()) return false;          //cannot kill another piece is in the way


        if (playerTurn == PlayerColor.black) {
            if (!piece.getIsKing()&&piece.getPieceColor() == PlayerColor.black && p2.getY() > piece.pos.getY())
                return false;  // Regular red piece can only move up.
            if (!tile2.hasPiece()) return false;
            if (tile2.hasPiece() && tile2.getPieceColor() == PlayerColor.black)
                return false;  // Cannot jump itself
            return true;  // The jump is legal.
        } else {
            if (!piece.getIsKing()&&piece.getPieceColor() == PlayerColor.white && p2.getY() < piece.pos.getY())
                return false;  // Regular black piece can only move down.
            if (!tile2.hasPiece()) return false;
            if (tile2.hasPiece() && tile2.getPieceColor() == PlayerColor.white)
                return false;
            return true;  // The jump is legal.
        }

    }

    private boolean canMove(PlayerColor playerTurn, PieceData piece, Position pos2) {

        if (pos2.getX() < 0 || pos2.getX() >= 8 || pos2.getY() < 0 || pos2.getY() >= 8)
            return false;  // pos2 is off the board

        Tile tileAtPos = tiles[pos2.getX()][pos2.getY()];
        if (tileAtPos.hasPiece() == true) return false;  // pos2 already contains a piece.

        if (playerTurn.equals(PlayerColor.white)) {
            if (piece.getPieceColor().equals(PlayerColor.white)) {
                if (!piece.getIsKing() && (pos2.getY() < piece.pos.getY())) {
                    return false;
                }
                if (piece.getIsKing()) return true;
                //if piece not king white can only move down

                return true;  // The move is legal.
            }
        }else {
            if (piece.getPieceColor().equals(PlayerColor.black)) {
                if (((!piece.getIsKing() && pos2.getY() > piece.pos.getY())))
                    return false;  // Regular black piece can only move up.
                if (piece.getIsKing()) return true;
                return true;  // The move is legal.
            }
        }
        return false;

    }

    public ArrayList<PlayerMove> getLegalMovesFrom(PlayerColor playerTurn, PieceData piece) {
        ArrayList<PlayerMove> moves = new ArrayList<>();
        if (piece.getPieceColor().equals(playerTurn)) {
            Position piecePos = piece.getPos();

            if (piece.getIsKing()) {
                Position directionTopRight = Position.subtract(piecePos.getTopRight(), piecePos);
                Position directionTopLEFT = Position.subtract(piecePos.getTopLeft(), piecePos);
                Position directionBotTomRight = Position.subtract(piecePos.getBottomRight(), piecePos);
                Position directionBottomLeft = Position.subtract(piecePos.getBottomLeft(), piecePos);

                moves.addAll(getKingMoves(playerTurn, piece, piecePos, directionTopRight));
                moves.addAll(getKingMoves(playerTurn, piece, piecePos, directionTopLEFT));
                moves.addAll(getKingMoves(playerTurn, piece, piecePos, directionBotTomRight));
                moves.addAll(getKingMoves(playerTurn, piece, piecePos, directionBottomLeft));

            }
            else {


                if (canMove(playerTurn, piece, piecePos.getTopRight())) {
                    moves.add(new PlayerMove(piece, piecePos.getTopRight()));
                }

                if (canMove(playerTurn, piece, piecePos.getTopLeft())) {

                    if (piece.getIsKing())
                        moves.addAll(getKingMoves(playerTurn, piece, piecePos, piecePos.getTopLeft()));
                    else
                        moves.add(new PlayerMove(piece, piecePos.getTopLeft()));
                }
                if (canMove(playerTurn, piece, piecePos.getBottomRight()))
                    moves.add(new PlayerMove(piece, piecePos.getBottomRight()));
                if (canMove(playerTurn, piece, piecePos.getBottomLeft()))
                    moves.add(new PlayerMove(piece, piecePos.getBottomLeft()));
            }
        }
        return moves;

    }

    private Collection<PlayerMove> getKingMoves(PlayerColor playerTurn, PieceData piece, Position piecePos, Position direction) {
      Collection<PlayerMove> kingMoves=new ArrayList<>();
        int multiplier=1;
        boolean ableToMove=true;
        while(ableToMove) {
            ableToMove=canMove(playerTurn, piece, piecePos.getPosInDirection(direction,multiplier));
            if (ableToMove) {
                kingMoves.add(new PlayerMove(piece, piecePos.getPosInDirection(direction,multiplier)));
            }
            multiplier++;
        }
        return kingMoves;
    }

    public int getWhiteScore() {
        return eatenWhitePieces+(eatenWhiteKings*2);
    }

    public int getBlackScore() {

        return eatenBlackPieces+(eatenBlackKings*2);
    }

    public void addJump(PieceData piece) {
        if (piece.getPieceColor().equals(PlayerColor.black)) {
            if(piece.getIsKing())eatenWhiteKings++;
            else eatenWhitePieces++;
        } else {
            if(piece.getIsKing())eatenBlackKings++;
            else eatenBlackPieces++;
        }
    }

    /*
  Returns all moves for a player
     */
    public List<PlayerMove> getPlayerLegalMoves(PlayerColor playerColor) {

        List<PlayerMove> moves = new ArrayList<>();

        /*  First check and add all valid jumps, if no jumps are found search for valid moves
         */
        for (int i = 0; i < X_ROW_SIZE; i++) {
            for (int j = 0; j < Y_COLUMN_SIZE; j++) {
                if (tiles[j][i].hasPiece()&&tiles[j][i].getPieceColor()==playerColor) {
                    PieceData pieceData = tiles[j][i].getPiece().getPieceData();
                    moves.addAll(getLegalJumpsFrom(playerColor, pieceData));
                }

            }
        }
        //if any jumps are found the user must jump so any regular moves aren't permitted
        if (moves.size() == 0) {

            for (int i = 0; i < X_ROW_SIZE; i++) {
                for (int j = 0; j < Y_COLUMN_SIZE; j++) {
                    if (tiles[j][i].hasPiece()) {
                        PieceData pieceData = tiles[j][i].getPiece().getPieceData();
                        moves.addAll(getLegalMovesFrom(playerColor, pieceData));
                    }

                }
            }

        }


        return moves;
    }


}


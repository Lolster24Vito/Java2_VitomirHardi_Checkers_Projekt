package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import java.util.ArrayList;
import java.util.List;

public class PlayerMove {

    PieceData pieceToMove;
    Position position;
    Boolean isMoveJump=false;

    public Boolean isJump() {
        return isMoveJump;
    }

    public Position getPosition() {
        return position;
    }

    public PlayerMove(PieceData pieceToMove, Position position) {
        this.pieceToMove = pieceToMove;
        this.position = position;
    }

    public PlayerMove(PieceData pieceToMove, Position position, Boolean isJump) {
        this.pieceToMove = pieceToMove;
        this.position = position;
        this.isMoveJump = isJump;
    }

    @Override
    public String toString() {
        return "fromTile=" + pieceToMove.pos +
                ", toTile=" + position;
    }






}

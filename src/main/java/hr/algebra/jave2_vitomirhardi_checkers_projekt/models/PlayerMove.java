package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

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
        this.pieceToMove = new PieceData(pieceToMove.pos,pieceToMove.getIsKing(),pieceToMove.getPieceColor());
        this.position = position;
        this.isMoveJump = isJump;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(pieceToMove.pieceColor==PlayerColor.black?"Black ":"White ");
        if(this.isMoveJump)sb.append("jump from:");
        else sb.append("move from:");
        sb.append(pieceToMove.pos.toString());
        sb.append(", to:");
        sb.append(position.toString());
        return sb.toString();
    }
public Position getPiecePosition(){
    return pieceToMove.getPos();
}





}

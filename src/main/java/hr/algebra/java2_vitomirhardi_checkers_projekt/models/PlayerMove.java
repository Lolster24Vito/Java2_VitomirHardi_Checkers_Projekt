package hr.algebra.java2_vitomirhardi_checkers_projekt.models;

public class PlayerMove {

    private  PieceData pieceToMove;
    private  Position position;
    private  Boolean isMoveJump=false;
   private Position takenPiecePosition;


    public PlayerMove() {
    }

    public Position getTakenPiecePosition() {
        return takenPiecePosition;
    }

    public Boolean isJump() {
        return isMoveJump;
    }

    public Position getPosition() {
        return position;
    }

    public PieceData getPieceToMove() {
        return pieceToMove;
    }

    public Boolean getMoveJump() {
        return isMoveJump;
    }

    public PlayerMove(PieceData pieceToMove, Position position) {
        this.pieceToMove = new PieceData(pieceToMove);
        this.position = new Position(position.getX(), position.getY());
    }

    public PlayerMove(PieceData pieceToMove,Position takenPiecePosition,Position position, Boolean isJump) {
        this.pieceToMove = new PieceData(pieceToMove);
        this.position = position;
        this.isMoveJump = isJump;
        this.takenPiecePosition=takenPiecePosition;
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

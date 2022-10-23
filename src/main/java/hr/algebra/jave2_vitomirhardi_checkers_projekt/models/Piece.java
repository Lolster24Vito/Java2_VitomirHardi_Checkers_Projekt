package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Piece extends Circle {
    //todo refactor
    Position pos;

    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    Boolean isKing=false;

    public Boolean getKing() {
        return isKing;
    }

    PlayerColor pieceColor;
    Boolean isAlive=true;





    public PlayerColor getPieceColor() {
        return pieceColor;
    }
//                    Circle circle=new Circle(PIECE_SIZE,Color.RED);

    public Piece(double v,Paint paint,Position pos, PlayerColor pieceColor,Boolean isKing) {
        super(v,paint);
        this.pos = pos;
        this.isKing = isKing;
        this.pieceColor = pieceColor;
    }

    public Piece(double v, Paint paint, Position pos, PlayerColor pieceColor) {
        super(v, paint);
        this.pos = pos;
        this.isKing = false;
        this.pieceColor = pieceColor;
    }



}

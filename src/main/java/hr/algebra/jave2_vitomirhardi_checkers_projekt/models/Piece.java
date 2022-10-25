package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import javafx.geometry.Pos;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Piece extends Circle {
    //todo refactor
    //PieceInfo
    //Piece info ima sve ove podatke
    PieceData pieceData;

    public void setPosition(Position pos) {
        pieceData.pos = pos;
    }
    public Position getPos() {
        return pieceData.pos;
    }
    public PlayerColor getPieceColor() {
        return pieceData.pieceColor;
    }
    public Boolean getKing() {
        return pieceData.isKing;
    }


    public PieceData getPieceData() {
        return pieceData;
    }

    public Piece(double v, Paint paint, Position pos, PlayerColor pieceColor, Boolean isKing) {
        super(v,paint);
        this.pieceData=new PieceData(pos,isKing,pieceColor);

    }

    public Piece(double v, Paint paint, PieceData pieceData,Position position) {
        super(v, paint);
        this.pieceData = pieceData;
        this.pieceData.setPosition(position);
    }

    public Piece(double v, Paint paint, Position pos, PlayerColor pieceColor) {
        super(v, paint);
        this.pieceData=new PieceData(pos,false,pieceColor);

    }




}

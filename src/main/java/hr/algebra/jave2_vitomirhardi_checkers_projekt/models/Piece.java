package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

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
        if(isKing){
            turnPieceIntoKing();
        }

        this.pieceData=new PieceData(pos,isKing,pieceColor);

    }

    public Piece(double v, Paint paint, PieceData pieceData,Position position) {
        super(v, paint);
        if(pieceData.isKing)turnPieceIntoKing();
        this.pieceData = pieceData;
        this.pieceData.setPosition(position);
    }

    public Piece(double v, Paint paint, Position pos, PlayerColor pieceColor) {
        super(v, paint);
        //todo debug isking changed to true
        this.pieceData=new PieceData(pos,false,pieceColor);
        //turnPieceIntoKing();

    }

    private void turnPieceIntoKing() {
        this.setStrokeWidth(4);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStroke(Color.rgb(255,179,2));
    }


}

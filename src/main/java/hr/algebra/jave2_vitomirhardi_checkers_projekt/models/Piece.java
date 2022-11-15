package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Piece extends Circle {

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
        return pieceData.getIsKing();
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
        if(pieceData.getIsKing())turnPieceIntoKing();
        this.pieceData = pieceData;
        this.pieceData.setPosition(position);
    }

    public Piece(double v, Paint paint, Position pos, PlayerColor pieceColor) {
        super(v, paint);
        this.pieceData=new PieceData(pos,false,pieceColor);

    }

    public Piece(double v, Paint paint, PieceData pieceData) {
        super(v, paint);
        this.pieceData = pieceData;
        if(this.pieceData.getIsKing()){
            turnPieceIntoKing();
        }
    }

    private void turnPieceIntoKing() {
        this.setStrokeWidth(4);
        this.setStrokeType(StrokeType.INSIDE);
        this.setStroke(Color.rgb(255,179,2));
    }


}

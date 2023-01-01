package hr.algebra.java2_vitomirhardi_checkers_projekt.models;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {
    private int xPos;
    private int yPos;


    public Position(int xPos, int yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public int getX() {
        return xPos;
    }

    public void setX(int xPos) {
        this.xPos = xPos;
    }

    public int getY() {
        return yPos;
    }
    public Position getTopRight(){
        return new Position(xPos+1,yPos+1);
    }
    public Position getTopRight(int multiplier){
        return new Position(xPos+(1*multiplier),yPos+(1*multiplier));
    }
    public Position getTopLeft(){
        return new Position(xPos-1,yPos+1);
    }
    public Position getTopLeft(int multiplier){
        return new Position(xPos-(1*multiplier),yPos+(1*multiplier));
    }
    public Position getBottomRight(int multiplier){
        return new Position(xPos+(1*multiplier),yPos-(1*multiplier));
    }
    public Position getBottomRight(){
        return new Position(xPos+1,yPos-1);
    }
    public Position getBottomLeft(){
        return new Position(xPos-1,yPos-1);
    }
    public Position getBottomLeft(int multiplier){
        return new Position(xPos-(1*multiplier),yPos-(1*multiplier));
    }

    public static Position subtract(Position pos1,Position pos2){
        return new  Position(pos1.getX()- pos2.getX(),
                pos1.getY()-pos2.getY());
    }

    public Position getPosInDirection(int x,int y){
        return new Position(xPos+x,yPos+y);
    }

    public void setY(int yPos) {
        this.yPos = yPos;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return xPos == position.xPos && yPos == position.yPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xPos, yPos);
    }

    @Override
    public String toString() {
        return (xPos+","+yPos);
    }

    public Position getPosInDirection(Position direction, int multiplier) {
        return new Position(xPos+(direction.getX()*multiplier),yPos+(direction.getY()*multiplier));
    }
}

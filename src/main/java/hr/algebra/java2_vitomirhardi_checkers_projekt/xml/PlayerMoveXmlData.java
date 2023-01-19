package hr.algebra.java2_vitomirhardi_checkers_projekt.xml;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;

public class PlayerMoveXmlData {
    String id;



    private int xPos;
    private   int yPos;
    private   boolean isKing;
    private   PlayerColor playerColor;
    private  int takenPieceX;
    private  int takenPieceY;
    private  int fromPosX;
    private int fromPosY;
    private boolean isJump;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getFromPosX() {
        return fromPosX;
    }

    public void setFromPosX(int fromPosX) {
        this.fromPosX = fromPosX;
    }

    public int getFromPosY() {
        return fromPosY;
    }

    public void setFromPosY(int fromPosY) {
        this.fromPosY = fromPosY;
    }

    public boolean isJump() {
        return isJump;
    }

    public void setJump(boolean jump) {
        isJump = jump;
    }

    public int getxPos() {
        return xPos;
    }

    public void setX(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setY(int yPos) {
        this.yPos = yPos;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(PlayerColor playerColor) {
        this.playerColor = playerColor;
    }

    public int getTakenPieceX() {
        return takenPieceX;
    }

    public void setTakenPieceX(int takenPieceX) {
        this.takenPieceX = takenPieceX;
    }

    public int getTakenPieceY() {
        return takenPieceY;
    }

    public void setTakenPieceY(int takenPieceY) {
        this.takenPieceY = takenPieceY;
    }
}

package hr.algebra.jave2_vitomirhardi_checkers_projekt.models;

import java.io.Serializable;
import java.util.List;

public class SerializableBoard implements Serializable {
    private List<PieceData> serializablePieces;
    private PlayerColor playerTurn;
    private String whitePlayerName;
    private String blackPlayerName;


    private long whiteTimerSeconds;
    private long blackTimerSeconds;



    private int eatenWhitePieces = 0;
    private int eatenWhiteKings=0;
    private int eatenBlackPieces =0;
    private int eatenBlackKings =0;


    public int getEatenWhitePieces() {
        return eatenWhitePieces;
    }

    public int getEatenBlackPieces() {
        return eatenBlackPieces;
    }

    public int getEatenWhiteKings() {
        return eatenWhiteKings;
    }

    public int getEatenBlackKings() {
        return eatenBlackKings;
    }

    public SerializableBoard(List<PieceData> serializablePieces, PlayerColor playerTurn, String whitePlayerName, String blackPlayerName, long whiteTimerSeconds, long blackTimerSeconds
    , int eatenWhitePieces, int eatenWhiteKings, int eatenBlackPieces, int eatenBlackKings) {
        this.serializablePieces = serializablePieces;
        this.playerTurn = playerTurn;
        this.whitePlayerName = whitePlayerName;
        this.blackPlayerName = blackPlayerName;
        this.whiteTimerSeconds=whiteTimerSeconds;
        this.blackTimerSeconds=blackTimerSeconds;
        this.eatenBlackPieces=eatenBlackPieces;
        this.eatenBlackKings=eatenBlackKings;
        this.eatenWhitePieces=eatenBlackPieces;
        this.eatenWhiteKings=eatenWhiteKings;
    }
    public long getWhiteTimerSeconds() {
        return whiteTimerSeconds;
    }

    public long getBlackTimerSeconds() {
        return blackTimerSeconds;
    }

    public List<PieceData> getSerializablePieces() {
        return serializablePieces;
    }

    public void setSerializablePieces(List<PieceData> serializablePieces) {
        this.serializablePieces = serializablePieces;
    }

    public PlayerColor getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(PlayerColor playerTurn) {
        this.playerTurn = playerTurn;
    }

    public String getWhitePlayerName() {
        return whitePlayerName;
    }

    public void setWhitePlayerName(String whitePlayerName) {
        this.whitePlayerName = whitePlayerName;
    }

    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public void setBlackPlayerName(String blackPlayerName) {
        this.blackPlayerName = blackPlayerName;
    }
}

package hr.algebra.java2_vitomirhardi_checkers_projekt.models;

public class PlayerInfo {
    String playerName;
    PlayerColor color;
//s
    public PlayerColor getColor() {
        return color;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Integer getNumberOfWins() {
        return numberOfWins;
    }

    public PlayerInfo(String playerName, PlayerColor color) {
        this.playerName = playerName;
        this.color = color;
    }

    public void recordWin() {
        this.numberOfWins++;
    }

    Integer numberOfWins;
}

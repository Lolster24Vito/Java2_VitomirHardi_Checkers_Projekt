package hr.algebra.java2_vitomirhardi_checkers_projekt;

import hr.algebra.Utils.TimerUtils;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;

import java.io.Serializable;

public class LeaderboardResult implements Serializable {
    private static final long serialVersionUID = 7547777592160616069L;
    //this is not in models because tableview throws Illegal Access exception in the GameLeadeboardController
    private  String winnerName="";
    private long playerMatchTime;
    private String playerMatchTimeFormatted;



    private long score;
    private PlayerColor color;
    //private string color
public  LeaderboardResult(){}



    public LeaderboardResult(String winnerName, long playerMatchTime, int score,PlayerColor color) {
        this.winnerName = winnerName;
        this.playerMatchTime = playerMatchTime;
        playerMatchTimeFormatted=TimerUtils.secondsToFormat(playerMatchTime);
        this.score = score;
        this.color=color;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public long getPlayerMatchTime() {
        return playerMatchTime;
    }

    public long getScore() {
        return score;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public void setPlayerMatchTime(int playerMatchTime) {
        this.playerMatchTime = playerMatchTime;
        playerMatchTimeFormatted=TimerUtils.secondsToFormat(playerMatchTime);
    }


    public void setScore(int score) {
        this.score = score;
    }
    public String getPlayerMatchTimeFormatted() {
        return playerMatchTimeFormatted;
    }

    public PlayerColor getColor() {
        return color;
    }
}

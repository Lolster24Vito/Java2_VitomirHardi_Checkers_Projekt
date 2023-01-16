package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.Utils.TimerUtils;
import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.LeaderboardResult;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerMove;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameWinController implements Initializable {


    public LeaderboardResult winner;

    public  List<PlayerMove> moves;

@FXML private Label labelWinnerName;
@FXML private Label labelWinnerTime;
    @FXML private Label labelScore;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
if(winner!=null){
    labelWinnerName.setText(winner.getWinnerName());
    labelWinnerTime.setText(TimerUtils.secondsToFormat(winner.getPlayerMatchTime()));
  //  labelWinnerColor.setText();
}
    }

    public void playAgain(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LocalGameStartScreen.fxml"));

        try {
           Scene scene = new Scene(fxmlLoader.load());
            HelloApplication.getMainStage().setTitle("Hello!");
            HelloApplication.getMainStage().setScene(scene);
            HelloApplication.getMainStage().show();
            HelloApplication.getPopupStage().hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void ViewMatchMoves(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("GameMovesScreen.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 1200, 768);


        GameMovesController gameMovesController=fxmlLoader.getController();
       gameMovesController.setMoves(FXCollections.observableArrayList(moves));
        gameMovesController.setWinScreenLeaderboardResult(winner);
        HelloApplication.getPopupStage().setTitle("Game moves");
        HelloApplication.getPopupStage().setScene(scene);
        HelloApplication.getPopupStage().show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void setWinner(LeaderboardResult winner) {
        this.winner = winner;
        labelWinnerName.setText(winner.getWinnerName());
        labelWinnerTime.setText(TimerUtils.secondsToFormat(winner.getPlayerMatchTime()));
        labelScore.setText(String.valueOf(winner.getScore()));
    }

    public  void setMoves(List<PlayerMove> moves) {
        this.moves = moves;
    }

    public void btnReplayMatchMovesAction(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MatchReplayScreen.fxml"));
        try {


        Scene scene = new Scene(fxmlLoader.load(), 1200, 768);

        HelloApplication.getPopupStage().setTitle("Game replay moves");
        HelloApplication.getPopupStage().setScene(scene);
        HelloApplication.getPopupStage().show();
        }
         catch (IOException e) {
            e.printStackTrace();

        }
    }

}

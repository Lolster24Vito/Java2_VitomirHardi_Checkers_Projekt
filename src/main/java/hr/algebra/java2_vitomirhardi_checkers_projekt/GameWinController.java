package hr.algebra.java2_vitomirhardi_checkers_projekt;

import hr.algebra.Utils.TimerUtils;
import hr.algebra.jave2_vitomirhardi_checkers_projekt.models.PlayerMove;
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


    public  LeaderboardResult winner;

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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("GameStartScreen.fxml"));

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
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1200, 768);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GameMovesController gameMovesController=fxmlLoader.getController();
       gameMovesController.setMoves(FXCollections.observableArrayList(moves));
gameMovesController.setWinScreenLeaderboardResult(winner);
        HelloApplication.getPopupStage().setTitle("Game moves");
        HelloApplication.getPopupStage().setScene(scene);
        HelloApplication.getPopupStage().show();
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


}

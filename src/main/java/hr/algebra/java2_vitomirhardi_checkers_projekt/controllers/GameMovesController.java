package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.LeaderboardResult;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerMove;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameMovesController implements Initializable {

    @FXML
    private ListView listViewMoves;
    private LeaderboardResult winScreenLeaderboardResult;
    private ObservableList<PlayerMove> moves;

    public void setWinScreenLeaderboardResult(LeaderboardResult winScreenLeaderboardResult) {
        this.winScreenLeaderboardResult = winScreenLeaderboardResult;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
System.out.println("bla");
    }

    public void setMoves(ObservableList<PlayerMove> moves) {
        this.moves = moves;
        listViewMoves.setItems(this.moves);
    }

    public void goToGameStartScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("GameWinScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1200, 768);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameWinController gameWinController=fxmlLoader.getController();
        gameWinController.setWinner(winScreenLeaderboardResult);
        gameWinController.setMoves(moves);


        HelloApplication.getPopupStage().setTitle("GameWinScreen.fxml");
        HelloApplication.getPopupStage().setScene(scene);
        HelloApplication.getPopupStage().show();
    }


}

package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;

public class GameStartController {
    @FXML
    private TextField tfPlayer1;

    @FXML
    private TextField tfPlayer2;

    @FXML
    private RadioButton rbWhite;

    @FXML
    private RadioButton rbBlack;

    private static PlayerInfo blackPlayer;
    private static PlayerInfo whitePlayer;


    public static PlayerInfo getBlackPlayer() {
        return blackPlayer;
    }

    public static PlayerInfo getWhitePlayer() {
        return whitePlayer;
    }
    public static void setWhitePlayerName(String newName){
        whitePlayer=new PlayerInfo(newName,PlayerColor.white);
    }
    public static void setBlackPlayerName(String newName){
        blackPlayer=new PlayerInfo(newName,PlayerColor.white);
    }
    private GameBoardController boardController;

    public void showLeaderBoard() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Gameleaderboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 768);
        HelloApplication.getMainStage().setTitle("Checkers Leaderboard");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();

    }
    public void StartGame() throws IOException {
        String playerOneName = tfPlayer1.getText();
        String playerTwoName = tfPlayer2.getText();

         blackPlayer =new PlayerInfo(rbBlack.isSelected()?playerOneName:playerTwoName,PlayerColor.black);
        whitePlayer =new PlayerInfo(rbBlack.isSelected()?playerTwoName:playerOneName,PlayerColor.white);




        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersBoard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 768);
        boardController=fxmlLoader.getController();
        boardController.setPlayers(whitePlayer,blackPlayer);
        HelloApplication.getMainStage().setTitle("Checkers");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
        HelloApplication.getMainStage().setOnCloseRequest(event->{
            boardController.dispose();
            Platform.exit();
            System.exit(0);
        });
    }


}

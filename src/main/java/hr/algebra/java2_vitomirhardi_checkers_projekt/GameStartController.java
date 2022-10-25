package hr.algebra.java2_vitomirhardi_checkers_projekt;

import hr.algebra.jave2_vitomirhardi_checkers_projekt.models.PlayerColor;
import hr.algebra.jave2_vitomirhardi_checkers_projekt.models.PlayerInfo;
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

    public void StartGame() throws IOException {
        String playerOneName = tfPlayer1.getText();
        String playerTwoName = tfPlayer2.getText();

         blackPlayer =new PlayerInfo(rbBlack.isSelected()?playerOneName:playerTwoName,PlayerColor.black);
        whitePlayer =new PlayerInfo(rbBlack.isSelected()?playerTwoName:playerOneName,PlayerColor.white);
      
       /* player1Info = new PlayerInfo(playerOneName,
                rbBlack.isSelected() ? PlayerColor.black : PlayerColor.white);*/

       /* player2Info = new PlayerInfo(playerTwoName,
                rbWhite.isSelected() ? PlayerColor.white : PlayerColor.black);*/



        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersBoard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 768);
        HelloApplication.getMainStage().setTitle("Checkers");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

}

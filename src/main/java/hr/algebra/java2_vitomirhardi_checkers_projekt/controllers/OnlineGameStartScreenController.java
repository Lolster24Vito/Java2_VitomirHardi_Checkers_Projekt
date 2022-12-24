package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.server.Server;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class OnlineGameStartScreenController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private Label lbDebugStatus;
    @FXML
    private TextField tfRoomCode;
    @FXML
    private AnchorPane joinRoomPane;
    @FXML
    private AnchorPane paneEnterUsername;

    private String playerName;

    private GameBoardController boardController;

    public void makeRoomBtnAction(ActionEvent actionEvent) {
    }

    public void btnJoinAction(ActionEvent actionEvent) {
    }

    public void btnMakeRoomAction(ActionEvent actionEvent) {
        try(Socket clientSocket=new Socket(Server.HOST,Server.PORT);
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        ){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());

            System.out.println("Sending messages to the Server");
            LoginMessage loginMessage = new LoginMessage(tfUsername.getText(),tfRoomCode.getText());
            oos.writeObject(loginMessage);
            MatchmakingRoom matchmakingRoom;
            while(true){

                try {
                    System.out.println("reading the matchmakingRoom:");
                    matchmakingRoom=(MatchmakingRoom) ois.readObject();
                    System.out.println("got the matchmakingRoom:"+matchmakingRoom.getRoomCode());
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            loadOnlineMatch(matchmakingRoom);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadOnlineMatch(MatchmakingRoom matchmakingRoom) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersBoard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 768);
        boardController=fxmlLoader.getController();
        boardController.setOnlineMatch(matchmakingRoom);
        HelloApplication.getMainStage().setTitle("Checkers");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
        HelloApplication.getMainStage().setOnCloseRequest(event->{
            boardController.dispose();
            Platform.exit();
            System.exit(0);
        });
    }

    public void btnEnterUsernameAction(ActionEvent actionEvent) {
        if(!tfUsername.getText().trim().isBlank()){
            joinRoomPane.setDisable(false);
            paneEnterUsername.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

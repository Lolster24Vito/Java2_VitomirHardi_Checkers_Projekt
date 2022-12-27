package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoom;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.RoomState;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerInfo;
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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.*;

public class OnlineGameStartScreenController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private Label lbRoomStatus;
    @FXML
    private TextField tfRoomCode;
    @FXML
    private AnchorPane joinRoomPane;
    @FXML
    private AnchorPane paneEnterUsername;

    private String playerName;

    private GameBoardController boardController;
    private MatchmakingRoom matchmakingRoom;

    private PlayerInfo playerInfo;


    public void btnJoinAction(ActionEvent actionEvent) {
        String enteredRoomCode = tfRoomCode.getText().trim();
        String enteredUsername = tfUsername.getText().trim();

        if(enteredRoomCode.isBlank()&&enteredUsername.isBlank())return;

        RoomState roomState=RoomState.NotExists;

        LoginMessage loginMessage=new LoginMessage(enteredUsername,enteredRoomCode);

        roomState = getRoomState(loginMessage);


        if(roomState==RoomState.NotExists){
             lbRoomStatus.setText("not exists");
         }
         if(roomState==RoomState.ExistsAndEnoughPlayers){
             lbRoomStatus.setText("Joining");
         }
         if(roomState==RoomState.ExistsAndWaitingForPlayers){
             lbRoomStatus.setText("not enough players");
         }
        }

    private  RoomState getRoomState(LoginMessage enteredRoomCode) {
        RoomState roomState;
        try (Socket clientSocket = new Socket(Server.HOST, Server.LISTEN_ROOM_PORT);
     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
     ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        ) {

                System.err.println("Client is listening for changes from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                System.out.println("Sending messages to the Server");

                oos.writeObject(enteredRoomCode);
                System.out.println("recieved message from the Server");

                roomState = (RoomState) ois.readObject();
                System.out.println(roomState);


        } catch (IOException e) {
            throw new RuntimeException(e);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return roomState;
    }


    public void btnMakeRoomAction(ActionEvent actionEvent) {


        makeNewRoom();
        listenForPlayersJoin();





    }

    private void makeNewRoom() {

        if(tfRoomCode.getText().trim().isBlank()&&tfUsername.getText().trim().isBlank())return;

        try (Socket clientSocket = new Socket(Server.HOST, Server.MAKE_ROOM_PORT);
             ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        ) {
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

            System.out.println("Sending messages to the Server");
            LoginMessage loginMessage = getLoginMessage();
            oos.writeObject(loginMessage);
            matchmakingRoom =(MatchmakingRoom) ois.readObject();
            System.out.println("Recived messages from the Server");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private LoginMessage getLoginMessage() {
        return new LoginMessage(tfUsername.getText().trim(), tfRoomCode.getText().trim());
    }


    private void listenForPlayersJoin()  {

        LoginMessage loginMessage=getLoginMessage();
        ExecutorService executorService=Executors.newSingleThreadExecutor();
        Runnable checkForRoomChange=new Runnable() {
            @Override
            public void run() {
                RoomState roomState = RoomState.ExistsAndWaitingForPlayers;
                while (roomState != RoomState.ExistsAndEnoughPlayers && roomState != RoomState.NotExists) {
                    System.out.println("checking message");
                    roomState = getRoomState(loginMessage);

                    if (roomState == RoomState.ExistsAndWaitingForPlayers) {
                       Platform.runLater(()->lbRoomStatus.setText("Waiting game"));
                    }
                    if (roomState == RoomState.ExistsAndEnoughPlayers) {ž
                        Platform.runLater(() -> lbRoomStatus.setText("Joining game"));

                    }
                    if(roomState==RoomState.NotExists) {
                        Platform.runLater(() -> lbRoomStatus.setText("Failed to join game"));
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }



                }

            }
        };
        executorService.execute(checkForRoomChange);

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

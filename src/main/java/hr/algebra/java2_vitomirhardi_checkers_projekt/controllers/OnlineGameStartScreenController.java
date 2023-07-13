package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.MatchmakingRoomInfo;
import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.RoomPing;
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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
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
    private MatchmakingRoomInfo matchmakingRoomInfo;

    private PlayerInfo playerInfo;

    private ExecutorService executorService=Executors.newSingleThreadExecutor();

    public void btnJoinAction(ActionEvent actionEvent) {
        String enteredRoomCode = tfRoomCode.getText().trim();
        String enteredUsername = tfUsername.getText().trim();

        if(enteredRoomCode.isBlank()&&enteredUsername.isBlank())return;

        RoomPing roomPing=new RoomPing(RoomState.NotExists);

        LoginMessage loginMessage=new LoginMessage(enteredUsername,enteredRoomCode);

        roomPing = getRoomState(loginMessage);


        if(roomPing.getRoomState()==RoomState.NotExists){
             lbRoomStatus.setText("not exists");
         }
         if(roomPing.getRoomState()==RoomState.ExistsAndEnoughPlayers){
             lbRoomStatus.setText("Joining");
             try {
                 loadOnlineMatch(roomPing.getMatchmakingRoom());
             } catch (IOException e) {
                 e.printStackTrace();
                 throw new RuntimeException(e);
             }
             //ping server and get all values
         }
         if(roomPing.getRoomState()==RoomState.ExistsAndWaitingForPlayers){
             lbRoomStatus.setText("not enough players");
         }
        }

    private RoomPing getRoomState(LoginMessage loginMessage) {
        RoomPing roomState;
        try (Socket clientSocket = new Socket(Server.HOST, Server.LISTEN_ROOM_PORT);
     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
     ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
        ) {

                System.err.println("Client is listening for changes from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                System.out.println("Sending messages to the Server");

                oos.writeObject(loginMessage);
                System.out.println("recieved message from the Server");

                roomState = (RoomPing) ois.readObject();
            System.out.println("Read object from the Server");


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
            matchmakingRoomInfo =(MatchmakingRoomInfo) ois.readObject();
            System.out.println("Recived messages from the Server");
        } catch (IOException|ClassNotFoundException e) {
            serverErrorMessage();
        }
    }
    private void serverErrorMessage(){
        System.err.println("Error couldn't connect to server");
        lbRoomStatus.setText("Error couldn't connect to server ");
    }

    private LoginMessage getLoginMessage() {
        return new LoginMessage(tfUsername.getText().trim(), tfRoomCode.getText().trim());
    }


    private void listenForPlayersJoin()  {

        LoginMessage loginMessage=getLoginMessage();
        executorService=Executors.newSingleThreadExecutor();

        Runnable checkForRoomChange=new Runnable() {
            @Override
            public void run() {
                RoomState roomState=RoomState.ExistsAndWaitingForPlayers;
                try (Socket clientSocket = new Socket(Server.HOST, Server.LISTEN_ROOM_PORT);
                     ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                     ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                ) {
                    while (roomState != RoomState.ExistsAndEnoughPlayers && roomState != RoomState.NotExists) {

                    System.err.println("Client is listening for changes from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                    System.out.println("Sending messages to the Server");

                    oos.writeObject(loginMessage);
                    System.out.println("recieved message from the Server");

                        RoomPing roomPing = (RoomPing) ois.readObject();
                    System.out.println("Read object from the Server");
                        roomState = roomPing.getRoomState();

                    if (roomState == RoomState.ExistsAndWaitingForPlayers) {
                        Platform.runLater(()->lbRoomStatus.setText("Waiting game"));
                    }
                    if (roomState == RoomState.ExistsAndEnoughPlayers) {
                        Platform.runLater(() -> lbRoomStatus.setText("Joining game"));
                        Platform.runLater(()-> {
                            try {
                                loadOnlineMatch(roomPing.getMatchmakingRoom());
                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    if(roomState==RoomState.NotExists) {
                        Platform.runLater(() -> lbRoomStatus.setText("Failed to join game"));
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("Server listener has been terminated");
                        Thread.currentThread().isInterrupted();
                        return;
                    }



                }

                    System.out.println("checking message");


                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }
        };
        executorService.execute(checkForRoomChange);

    }

    private void loadOnlineMatch(MatchmakingRoomInfo matchmakingRoomInfo) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersBoard.fxml"));

        fxmlLoader.setControllerFactory(c ->{return  new GameBoardController(matchmakingRoomInfo,getLoginMessage());} );
        Scene scene = new Scene(fxmlLoader.load(), 1200, 768);
        boardController=fxmlLoader.getController();
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

    public void closeThreads() {
        if(!executorService.isShutdown()){
            executorService.shutdownNow();
        }
    }
}

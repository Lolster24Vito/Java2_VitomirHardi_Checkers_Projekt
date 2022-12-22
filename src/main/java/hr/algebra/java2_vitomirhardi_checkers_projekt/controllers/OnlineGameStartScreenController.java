package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.Online.LoginMessage;
import hr.algebra.server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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


    public void makeRoomBtnAction(ActionEvent actionEvent) {
    }

    public void btnJoinAction(ActionEvent actionEvent) {
    }

    public void btnMakeRoomAction(ActionEvent actionEvent) {
        try(Socket clientSocket=new Socket(Server.HOST,Server.PORT)){
            System.err.println("Client is connecting to " + clientSocket.getInetAddress() + ":" +clientSocket.getPort());

            ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            OutputStream outputStream = clientSocket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outputStream);
            System.out.println("Sending messages to the Server");

            LoginMessage loginMessage = new LoginMessage(tfUsername.getText(),tfRoomCode.getText());
            oos.writeObject(loginMessage);

            System.out.println("Client sent message back to the server!");
            LoginMessage loginMessage1=(LoginMessage) ois.readObject();
            System.out.println("recieved message back from server:"+loginMessage1.getUsername());
            System.out.println("Closing socket and terminating program.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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

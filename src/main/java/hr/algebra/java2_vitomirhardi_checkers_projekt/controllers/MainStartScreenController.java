package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainStartScreenController implements Initializable {

    private OnlineGameStartScreenController onlineGameStartScreenController;
    public void btnGetOnlineScreenAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("OnlineGameStartScreen.fxml"));

            Scene scene = new Scene(fxmlLoader.load());

        onlineGameStartScreenController=fxmlLoader.getController();
        HelloApplication.getMainStage().setTitle("Hello!");
        HelloApplication.getMainStage().setOnCloseRequest(e->onlineGameStartScreenController.closeThreads());
        HelloApplication.getMainStage().setScene(scene);
    }
    public  void btnOpenLocalAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LocalGameStartScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HelloApplication.getMainStage().setScene(scene);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

package hr.algebra.java2_vitomirhardi_checkers_projekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage mainStage;
    private static Stage popupStage;

    public static void setPopupStage(Stage popupStage) {
        HelloApplication.popupStage = popupStage;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.mainStage = stage;
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CheckersBoard.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LocalGameStartScreen.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("OnlineGameStartScreen.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    public static Stage getMainStage() {
        return mainStage;
    }

    public static Stage getPopupStage() {
        return popupStage;
    }

    public static void main(String[] args) {
        launch();
    }
}
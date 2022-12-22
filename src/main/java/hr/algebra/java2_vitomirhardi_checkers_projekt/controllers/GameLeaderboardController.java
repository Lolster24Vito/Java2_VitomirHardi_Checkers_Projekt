package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.LeaderboardResult;
import hr.algebra.java2_vitomirhardi_checkers_projekt.dal.RepositoryFactory;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.PlayerColor;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameLeaderboardController implements Initializable {
//https://jenkov.com/tutorials/javafx/tableview.html#:~:text=The%20JavaFX%20TableView%20enables%20you%20to%20specify%20the%20default%20sort,an%20ObservableList%20of%20TableColumn%20instances.
private TableView<LeaderboardResult> tableLeaderboard;

@FXML
    private BorderPane root;

@FXML
private TableView table;


@FXML
private TableColumn<LeaderboardResult,String> nameColumn;
@FXML
TableColumn<LeaderboardResult,Integer> columnTime;
@FXML TableColumn<LeaderboardResult,Integer> columnScore;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    TableView table=new TableView<LeaderboardResult>();
    TableColumn nameColumn=new TableColumn<LeaderboardResult,String>("winner name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<LeaderboardResult,String>("winnerName"));

        TableColumn columnTime = new TableColumn<LeaderboardResult,String>("player match time");
        columnTime.setCellValueFactory(new PropertyValueFactory<LeaderboardResult,String>("playerMatchTimeFormatted"));
        TableColumn columnScore = new TableColumn<LeaderboardResult,Integer>("Score");
        columnScore.setCellValueFactory(new PropertyValueFactory<LeaderboardResult,Integer>("score"));

        table.getColumns().add(nameColumn);
        table.getColumns().add(columnTime);
        table.getColumns().add(columnScore);

        root.setCenter(table);
        tableLeaderboard=table;
        loadData();

    }


    public void loadData(){
        try {
            List<LeaderboardResult> leaderboardResults = RepositoryFactory.getLeaderboardRepository().getLeaderboardResults();
            tableLeaderboard.setItems(FXCollections.observableArrayList(leaderboardResults));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
    public void goToGameStartScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LocalGameStartScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 1200, 768);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HelloApplication.getMainStage().setTitle("Game start");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();

    }

}

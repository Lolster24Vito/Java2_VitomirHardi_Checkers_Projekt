package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloController;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.*;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.MoveXmlElementTypes;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.PlayerMoveXmlData;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlConfiguration;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatchReplayController  implements Initializable
{

    @FXML private GameBoardController gameBoardController;




    private int moveCounter=0;
    ExecutorService executorService= Executors.newSingleThreadExecutor();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void btnPreviousMoveAction(ActionEvent actionEvent) {
        moveCounter--;

        XmlParser.readPlayerMove(moveCounter);
        //XmlParser.readPlayerMove(moveCounter);
    }

    public void btnNextMoveAction(ActionEvent actionEvent) {
        moveCounter++;
        Optional<PlayerMove> playerMove=Optional.empty();
        try {

            playerMove = XmlParser.readNextPlayerMove();
            if(playerMove.isPresent()){
                gameBoardController.replayNextMove(playerMove.get());

            }
        } catch (FileNotFoundException |XMLStreamException e) {
            e.printStackTrace();
            //todo error
        }
        if(playerMove.isEmpty()){
            moveCounter--;
            return;
        }
       // if(playerMove.isEmpty())return;
        //handlePiece(playerMove.get());


        //   XmlParser.readNextPlayerMove(moveCounter);
        //XmlParser.readPlayerMoves(moveCounter);
    }




}

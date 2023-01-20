package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.*;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlParser;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatchReplayController   implements Initializable
{
    @FXML
    private GridPane gridBoard;

    private Board board;

    private final int X_COLUMN_SIZE=GameBoardController.X_COLUMN_SIZE;
    private final int Y_ROW_SIZE=GameBoardController.Y_ROW_SIZE;
    private final double SIDE_SIZE=GameBoardController.SIDE_SIZE;
    private final double PIECE_SIZE=SIDE_SIZE/2;

    private final Color WHITE_COLOR = Color.rgb(150, 111, 51);
    private final Color BLACK_COLOR = Color.rgb(30, 0, 0);

    private final Color WHITE_PIECE_COLOR = Color.rgb(244, 245, 202);
    private final Color WHITE_PIECE_SELECTED_COLOR = Color.GREENYELLOW;

    private final Color BLACK_PIECE_COLOR = Color.rgb(71, 71, 64);
    private final Color BLACK_PIECE_SELECTED_COLOR = Color.rgb(110, 0, 0);

    private int moveCounter=0;

    ExecutorService executorService= Executors.newSingleThreadExecutor();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPane();
    }

    private void initPane() {
        board = new Board(GameBoardController.X_COLUMN_SIZE, GameBoardController.Y_ROW_SIZE);
        // Create 64 rectangles and add to pane
        boolean debugBool=true;

        int count = 0;
        for (int i = 0; i < X_COLUMN_SIZE; i++) {
            for (int j = 0; j < Y_ROW_SIZE; j++) {
                //rectangle
                Tile tile = new Tile(SIDE_SIZE, SIDE_SIZE, SIDE_SIZE, SIDE_SIZE, new Position(j, i), count);


                if (count % 2 == 0) {
                    tile.setFill(WHITE_COLOR);
                } else {
                    tile.setFill(BLACK_COLOR);
                }
               // tile.setOnMouseClicked(eventOnTileClick(tile.getTileData()));


                gridBoard.add(tile, j, i);
                //circle
                if ((i <= 2) && count % 2 == 1) {
                    Piece piece = new Piece(PIECE_SIZE, WHITE_PIECE_COLOR, new Position(j, i), PlayerColor.white);
                    tile.setPiece(piece);
                    gridBoard.add(piece, j, i);

                }
                //4,5
                // if(i==5&&j==4){
                if(debugBool) {
                    if ((i >= 5) && count % 2 == 1) {
                        debugBool=false;

                        Piece piece = new Piece(PIECE_SIZE, BLACK_PIECE_COLOR, new Position(j, i), PlayerColor.black);

                        tile.setPiece(piece);
                        gridBoard.add(piece, j, i);


                    }
                }
                board.tiles[j][i] = tile;

                count++;
            }
            count++;
        }

    }

    public void btnPreviousMoveAction(ActionEvent actionEvent) {

        Optional<PlayerMove> playerMove=Optional.empty();
        playerMove = XmlParser.readPlayerMove(moveCounter);


        removePieceFromTile(new Position(0,1));

        // moveCounter--;

        //XmlParser.readPlayerMove(moveCounter);
        //XmlParser.readPlayerMove(moveCounter);
    }

    public void btnNextMoveAction(ActionEvent actionEvent) {
        moveCounter++;
        Optional<PlayerMove> playerMove=Optional.empty();
        try {

            playerMove = XmlParser.readNextPlayerMove();
        } catch (FileNotFoundException |XMLStreamException e) {
            e.printStackTrace();
            //todo error
        }
        if(playerMove.isEmpty()){
            moveCounter--;
            return;
        }
        if(playerMove.isEmpty())return;
        handleNextMovePiece(playerMove.get());


        //   XmlParser.readNextPlayerMove(moveCounter);
        //XmlParser.readPlayerMoves(moveCounter);
    }

    private void handleNextMovePiece(PlayerMove playerMove) {
        Color color;
        if(playerMove.getPieceToMove().getPieceColor().equals(PlayerColor.black)){
            color=BLACK_PIECE_COLOR;
        }
        else{
            color=WHITE_PIECE_COLOR;
        }

        if(playerMove.isJump()){

            Piece piece = new Piece(PIECE_SIZE, color,new PieceData(playerMove.getPieceToMove()),playerMove.getPosition());
            addPiece(piece,playerMove);
            removePieceFromTile(playerMove.getPiecePosition());
            removePieceFromTile(playerMove.getTakenPiecePosition());


        }else {
            Piece piece = new Piece(PIECE_SIZE, color,new PieceData(playerMove.getPieceToMove()),playerMove.getPosition());
            addPiece(piece,playerMove);
           // gridBoard.add(piece, playerMove.getPosition().getX(), playerMove.getPosition().getY());
           // board.tiles[playerMove.getPosition().getX()][playerMove.getPosition().getY()].setPiece(piece);
            removePieceFromTile(playerMove.getPiecePosition());
        }


    }
    private void addPiece(Piece piece, PlayerMove playerMove){
        ObservableList<Node> childrens = gridBoard.getChildren();

            for (Node node : childrens) {
                if (node instanceof Tile &&
                        GridPane.getRowIndex(node) == playerMove.getPosition().getY() &&
                        GridPane.getColumnIndex(node) == playerMove.getPosition().getX()
                ) {
                    Platform.runLater(() -> {
                    Tile fromTile = (Tile) node;

                    piece.setPosition(playerMove.getPosition());
                    fromTile.setPiece(piece);

                    gridBoard.add(piece, playerMove.getPosition().getX(), playerMove.getPosition().getY());

                    // Piece piece = fromTile.getPiece();

                    //fromTile.setPiece(null);
                    // moveToTile.setPiece(piece);
                    //gridBoard.getChildren().remove(piece);
                    });
                    break;
                }
            }
            board.tiles[playerMove.getPosition().getX()][playerMove.getPosition().getY()].setPiece(piece);
          //  board.tiles[ playerMove.getPiecePosition().getX()][playerMove.getPiecePosition().getY()].setPiece(null);


    }
    private void removePieceFromTile(Position position) {
        Platform.runLater(() -> {
                    ObservableList<Node> childrens = gridBoard.getChildren();
            for (Node node : childrens) {
                if (node instanceof Tile &&
                        GridPane.getRowIndex(node) == position.getY() &&
                        GridPane.getColumnIndex(node) == position.getX()) {

                        Tile fromTile = (Tile) node;
                    Piece piece = fromTile.getPiece();

                    boolean removed=gridBoard.getChildren().remove(piece);
                    System.out.println(removed+" removed");

                    break;
                }
            }
            board.tiles[position.getX()][position.getY()].setPiece(null);
        });


    }



    /*private void movePiece(PlayerMove playerMove) {
    //    Piece movedPiece = new Piece(PIECE_SIZE, pieceColor, movePieceData, moveToPos);

      //  gridBoard.add(movedPiece, moveToPos.getX(), moveToPos.getY());

    }
    private void addPieceToTile(Position moveToPos, Piece movedPiece) {
        //add
        gridBoard.add(movedPiece, moveToPos.getX(), moveToPos.getY());
        board.tiles[moveToPos.getX()][moveToPos.getY()].setPiece(movedPiece);
    }
    private void loadPieceToTile(PieceData pieceD,Position position) {
        Color pieceColor = pieceD.getPieceColor() == PlayerColor.white ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR;
        pieceD.setPosition(position);
        Piece piece = new Piece(PIECE_SIZE, pieceColor, pieceD);
        Position piecePos = pieceD.getPos();

        board.tiles[piecePos.getX()][piecePos.getY()].setPiece(piece);
        gridBoard.add(piece, piecePos.getX(), piecePos.getY());
    }*/
    //gridpane klasik sa svim pokretima
    //start reading
    //mice se

}

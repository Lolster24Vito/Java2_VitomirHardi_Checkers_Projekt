package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.java2_vitomirhardi_checkers_projekt.models.*;
import hr.algebra.java2_vitomirhardi_checkers_projekt.xml.XmlParser;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
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
    @FXML private ListView lvMoves;

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

    private Stack <PieceData> jumpRemovedPieces =new Stack<>();

    ExecutorService executorService= Executors.newSingleThreadExecutor();
    private ObservableList<String> movesObservableList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movesObservableList= FXCollections.observableArrayList();
        lvMoves.setItems(movesObservableList);
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
              //  if(debugBool) {
                    if ((i >= 5) && count % 2 == 1) {
                //        debugBool=false;

                        Piece piece = new Piece(PIECE_SIZE, BLACK_PIECE_COLOR, new Position(j, i), PlayerColor.black);

                        tile.setPiece(piece);
                        gridBoard.add(piece, j, i);


                    }
                //}
                board.tiles[j][i] = tile;

                count++;
            }
            count++;
        }

    }

    public void btnPreviousMoveAction(ActionEvent actionEvent) {
        Optional<PlayerMove> playerMove=Optional.empty();
        System.out.println(moveCounter+"Prev");
        try {
            moveCounter--;
            playerMove = XmlParser.readPlayerMove(moveCounter);
            movesObservableList.remove(movesObservableList.size()-1);
        } catch (XMLStreamException | FileNotFoundException e) {
            moveCounter++;
            e.printStackTrace();
        }
        if(playerMove.isPresent()){
            handlePreviousMovePiece(playerMove.get());
        }


    }



    public void btnNextMoveAction(ActionEvent actionEvent) {


        Optional<PlayerMove> playerMove=Optional.empty();
        System.out.println(moveCounter+"Next");
        try {
            playerMove = XmlParser.readPlayerMove(moveCounter);
            moveCounter++;
            movesObservableList.add(playerMove.get().toString());
           // movesObservableList.add(playerMove.get());

        } catch (FileNotFoundException |XMLStreamException e) {
            e.printStackTrace();
        }
        if(playerMove.isPresent()){
            handleNextMovePiece(playerMove.get());
        }




        //   XmlParser.readNextPlayerMove(moveCounter);
        //XmlParser.readPlayerMoves(moveCounter);
    }

    private void handleNextMovePiece(PlayerMove playerMove) {
        Color color = getColorFromPlayerColor(playerMove.getPieceToMove().getPieceColor());

        if(playerMove.isJump()){
            Piece piece = new Piece(PIECE_SIZE, color,new PieceData(playerMove.getPieceToMove()),playerMove.getPosition());
           addPiece(piece,playerMove.getPosition().getX(),playerMove.getPosition().getY());
            //addPiece(piece,playerMove);
           //decouple
            removePieceFromTile(playerMove.getPiecePosition());
            Optional<PieceData> takenPieceData = getPiece(playerMove.getTakenPiecePosition().getX(),
                    playerMove.getTakenPiecePosition().getY());
            if(takenPieceData.isPresent())
            jumpRemovedPieces.push(takenPieceData.get());
            removePieceFromTile(playerMove.getTakenPiecePosition());


        }else {
            Piece piece = new Piece(PIECE_SIZE, color,new PieceData(playerMove.getPieceToMove()),playerMove.getPosition());

            addPiece(piece,playerMove.getPosition().getX(),playerMove.getPosition().getY());

           // gridBoard.add(piece, playerMove.getPosition().getX(), playerMove.getPosition().getY());
           // board.tiles[playerMove.getPosition().getX()][playerMove.getPosition().getY()].setPiece(piece);
            removePieceFromTile(playerMove.getPiecePosition());
        }


    }
    private void handlePreviousMovePiece(PlayerMove playerMove) {
        Color colorMovedPiece = getColorFromPlayerColor(playerMove.getPieceToMove().getPieceColor());

        if(playerMove.isJump()){
            PieceData removedPieceData =jumpRemovedPieces.pop();
            Color colorRemovedPiece= getColorFromPlayerColor(removedPieceData.getPieceColor());

            Piece piece = new Piece(PIECE_SIZE, colorMovedPiece,new PieceData(playerMove.getPieceToMove()),playerMove.getPosition());

            Piece removedPiece= new Piece(PIECE_SIZE, colorRemovedPiece,new PieceData(removedPieceData),removedPieceData.getPos());

             addPiece(removedPiece,playerMove.getTakenPiecePosition().getX(),
                     playerMove.getTakenPiecePosition().getY());
            addPiece(piece,playerMove.getPiecePosition().getX(),playerMove.getPiecePosition().getY());
            removePieceFromTile(playerMove.getPosition());


        }else {
            Piece piece = new Piece(PIECE_SIZE, colorMovedPiece,new PieceData(playerMove.getPieceToMove()),playerMove.getPosition());
            addPiece(piece,playerMove.getPiecePosition().getX(),playerMove.getPiecePosition().getY());

            removePieceFromTile(playerMove.getPosition());


            //  addPiece(piece,playerMove);
            // gridBoard.add(piece, playerMove.getPosition().getX(), playerMove.getPosition().getY());
            // board.tiles[playerMove.getPosition().getX()][playerMove.getPosition().getY()].setPiece(piece);
           // removePieceFromTile(playerMove.getPiecePosition());
        }
    }

    private Color getColorFromPlayerColor(PlayerColor pieceColor) {
        Color color;
        if(pieceColor.equals(PlayerColor.black)){
            color=BLACK_PIECE_COLOR;
        }
        else{
            color=WHITE_PIECE_COLOR;
        }
        return color;
    }



    private void addPiece(Piece piece, int xPos,int yPos){
        ObservableList<Node> childrens = gridBoard.getChildren();

            for (Node node : childrens) {
                if (node instanceof Tile &&
                        GridPane.getRowIndex(node) == yPos &&
                        GridPane.getColumnIndex(node) == xPos
                ) {
                    Platform.runLater(() -> {
                    Tile fromTile = (Tile) node;

                    piece.setPosition(new Position(xPos,yPos));
                    fromTile.setPiece(piece);

                    gridBoard.add(piece, xPos, yPos);

                    });
                    break;
                }
            }
            board.tiles[xPos][yPos].setPiece(piece);
          //  board.tiles[ playerMove.getPiecePosition().getX()][playerMove.getPiecePosition().getY()].setPiece(null);


    }
    private Optional<PieceData> getPiece(int xPos,int yPos){
            ObservableList<Node> childrens = gridBoard.getChildren();
            for (Node node : childrens) {
                if (node instanceof Tile &&
                        GridPane.getRowIndex(node) == yPos &&
                        GridPane.getColumnIndex(node) == xPos) {

                    Tile fromTile = (Tile) node;
                    PieceData pieceData=new PieceData(fromTile.getPiece().getPieceData());
                    return  Optional.of(pieceData);

                    //boolean removed=gridBoard.getChildren().remove(piece);
                    //System.out.println(removed+" removed");
                }
            }
            return Optional.empty();
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

package hr.algebra.java2_vitomirhardi_checkers_projekt;

import hr.algebra.jave2_vitomirhardi_checkers_projekt.models.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    @FXML
    private GridPane gridBoard;

    @FXML
    private Label labelPlayer1Name;
    @FXML
    private Label labelPlayer1Score;
    @FXML
    private Label labelPlayer2Name;
    @FXML
    private Label labelPlayer2Score;
    @FXML
    private Label labelPlayerTurn;

    @FXML
    private ListView listViewMovesHistory;


    public GameBoardController() {

    }

    private final int X_ROW_SIZE = 8;
    private final int Y_COLUMN_SIZE = 8;
    private final double SIDE_SIZE = 50;
    private final double PIECE_SIZE = SIDE_SIZE / 2;

    private final Color WHITE_COLOR = Color.rgb(150, 111, 51);
    private final Color BLACK_COLOR = Color.rgb(30, 0, 0);

    private final Color WHITE_PIECE_COLOR = Color.YELLOWGREEN;
    private final Color BLACK_PIECE_COLOR = Color.RED;

    private Board board;

    private Position selectedPieceLocation;
    private List<Position> selectedAvailablePositions;

    TurnManager turnManager = new TurnManager();

    private PlayerColor colorTurn = PlayerColor.white;

    public void InitPane() {

        board = new Board(new Tile[X_ROW_SIZE][Y_COLUMN_SIZE]);
        // Create 64 rectangles and add to pane
        int count = 0;
        for (int i = 0; i < X_ROW_SIZE; i++) {
            for (int j = 0; j < Y_COLUMN_SIZE; j++) {
                //rectangle
                Tile tile = new Tile(SIDE_SIZE, SIDE_SIZE, SIDE_SIZE, SIDE_SIZE, new Position(i, j), count);


                if (count % 2 == 0) {
                    tile.setFill(WHITE_COLOR);
                } else {
                    tile.setFill(BLACK_COLOR);
                }
                tile.setOnMouseClicked(eventOnTileClick(tile));


                gridBoard.add(tile, j, i);
                //circle
                if ((i <= 2) && count % 2 == 1) {
                    Piece piece = new Piece(PIECE_SIZE, WHITE_PIECE_COLOR, new Position(j, i), PlayerColor.white);
                    piece.setOnMouseClicked(eventPieceClicked(piece));
                    board.addWhitePiece(piece);
                    tile.setPiece(piece);
                    gridBoard.add(piece, j, i);
                }
                if ((i >= 5) && count % 2 == 1) {
                    //PIECE_SIZE,Color.RED,i,j, PlayerColor.white);
                    Piece piece = new Piece(PIECE_SIZE, BLACK_PIECE_COLOR, new Position(j, i), PlayerColor.black);
                    piece.setOnMouseClicked(eventPieceClicked(piece));
                    board.addBlackPiece(piece);
                    tile.setPiece(piece);
                    gridBoard.add(piece, j, i);
                }
                board.tiles[j][i] = tile;
                count++;
            }
            count++;
        }
        //move single piece from starting zone

    }

    private EventHandler<MouseEvent> eventOnTileClick(Tile tile) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //Function moveToPosition
                if (board.tiles[selectedPieceLocation.getxPos()][selectedPieceLocation.getyPos()].hasPiece() &&
                        board.tiles[selectedPieceLocation.getxPos()][selectedPieceLocation.getyPos()].getPiece().getPieceColor().equals(colorTurn)
                //&&selectedAvailablePositions.contains(tile)
                )
                //        tile.getPiece().getPieceColor().equals(colorTurn))

                    {
                    movePiece(board.tiles[selectedPieceLocation.getxPos()][selectedPieceLocation.getyPos()]
                            , board.tiles[tile.getPosition().getxPos()][tile.getPosition().getyPos()]);
                    colorTurn = colorTurn.equals(PlayerColor.white) ? PlayerColor.black : PlayerColor.white;
                    String playerName;
                    PlayerInfo pl1=GameStartController.getPlayer1Info();
                    PlayerInfo pl2=GameStartController.getPlayer2Info();
if(pl1.getColor().equals(colorTurn)){
//    labelPlayerTurn.setText(pl1.getPlayerName());
}
if(pl2.getColor().equals(colorTurn)){
  //  labelPlayerTurn.setText(pl2.getPlayerName());
}
                }
            }
        };
    }

    private EventHandler<MouseEvent> eventPieceClicked(Piece piece) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //selectTileToMove,showAvailableMoves
                if (piece.getPieceColor().equals(colorTurn)) {
                    clickOnPiece(piece);
                }
                //piece.setFill(Color.PINK);
            }
        };
    }

    private void clickOnPiece(Piece piece) {
        //Available Moves load
        selectedPieceLocation = piece.getPos();
        clearSelectedPieceColor();
        selectedAvailablePositions = new ArrayList<>();
        //selectedPiece=piece;
        Position piecePos = piece.getPos();
        Position dirTopRight = new Position(1, 1);
        Position dirTopLeft = new Position(-1, 1);
        Position dirDownRight = new Position(1, -1);


        //if piece is white
        if (piece.getPieceColor().equals(PlayerColor.white)) {
            if (piecePos.getxPos() + 1 >= 0 && piecePos.getxPos() + 1 < X_ROW_SIZE) {
                //topright enemy piece
                if (board.tiles[piecePos.getxPos() + 1][piecePos.getyPos() + 1].hasPiece()) {
                    Position piecePosition = new Position(piecePos.getxPos() + 1, piecePos.getyPos() + 1);
                    if (board.checkIfEmptyTile(dirTopRight, piecePosition)) {
                        //EAT
                        System.out.println("eatable");
                        board.tiles[piecePos.getxPos() + 1][piecePos.getyPos() + 1].setFill(Color.ORANGERED);
                        selectedAvailablePositions.add(new Position(piecePos.getxPos() + 1, piecePos.getyPos() + 1));
                    }
//selectedAvailablePositions.addAll(board.getMovesForKill(piecePosition,+1));
                } else {
                    selectedAvailablePositions.add(new Position(piecePos.getxPos() + 1, piecePos.getyPos() + 1));
                    board.tiles[piecePos.getxPos() + 1][piecePos.getyPos() + 1].setFill(Color.BEIGE);
                }
                // availableSpots.add(new Position(piecePos.getxPos()+1, piecePos.getyPos()+1));
                // board.tiles[piecePos.getxPos()+1][piecePos.getyPos()+1].setFill(Color.BEIGE);
            }
            if (piecePos.getxPos() - 1 >= 0 && piecePos.getxPos() - 1 < X_ROW_SIZE) {
                //topleft
                if (board.tiles[piecePos.getxPos() - 1][piecePos.getyPos() + 1].hasPiece()) {
                    Position piecePosition = new Position(piecePos.getxPos() - 1, piecePos.getyPos() + 1);
                    if (board.checkIfEmptyTile(dirTopLeft, piecePosition)) {
                        //EAT
                        System.out.println("eatable");
                        board.tiles[piecePos.getxPos() + dirTopLeft.getxPos() + dirTopLeft.getxPos()][piecePos.getyPos() + dirTopLeft.getyPos() + dirTopLeft.getyPos()].setFill(Color.ORANGERED);
                        selectedAvailablePositions.add(new Position(piecePos.getxPos() + dirTopLeft.getxPos(), piecePos.getyPos() + dirTopLeft.getyPos()));
                    }
                    //selectedAvailablePositions.addAll(board.getMovesForKill(piecePosition, +1));
                } else {
                    selectedAvailablePositions.add(new Position(piecePos.getxPos() - 1, piecePos.getyPos() + 1));
                    board.tiles[piecePos.getxPos() - 1][piecePos.getyPos() + 1].setFill(Color.BEIGE);
                }
            }
        } else {
            if (piecePos.getxPos() + 1 > 0 && piecePos.getxPos() + 1 < X_ROW_SIZE) {
                //downright
                selectedAvailablePositions.add(new Position(piecePos.getxPos() + 1, piecePos.getyPos() - 1));
                board.tiles[piecePos.getxPos() + 1][piecePos.getyPos() - 1].setFill(Color.BEIGE);
            }
            if (piecePos.getxPos() - 1 > 0 && piecePos.getxPos() - 1 < X_ROW_SIZE) {
                //downleft
                board.tiles[piecePos.getxPos() - 1][piecePos.getyPos() - 1].setFill(Color.BEIGE);
                selectedAvailablePositions.add(new Position(piecePos.getxPos() - 1, piecePos.getyPos() - 1));
            }
        }


        //board.tiles[piecePos.getxPos()][piecePos.getyPos()].;
    }

    private void clearSelectedPieceColor() {
        if (selectedAvailablePositions != null && selectedAvailablePositions.stream().count() > 0)
            for (Position pos : selectedAvailablePositions
            ) {
                int tileLocation = board.tiles[pos.getxPos()][pos.getyPos()].getTileLocation();
                Color fillColor = tileLocation % 2 == 0 ? WHITE_COLOR : BLACK_COLOR;
                board.tiles[pos.getxPos()][pos.getyPos()].setFill(fillColor);
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PlayerInfo pl1 = GameStartController.getPlayer1Info();
        PlayerInfo pl2 = GameStartController.getPlayer2Info();

        String whitePlayerName;
        String blackPlayerName;
        if (pl1.getColor().equals(PlayerColor.white)) {
            whitePlayerName = pl1.getPlayerName();
            blackPlayerName = pl2.getPlayerName();
        } else {
            blackPlayerName = pl1.getPlayerName();
            whitePlayerName = pl2.getPlayerName();
        }
        labelPlayer1Name.setText(whitePlayerName);
        labelPlayer2Name.setText(blackPlayerName);
       // labelPlayerTurn.setText(whitePlayerName);
        //labelPlayer1Score.setText();
        InitPane();
//movePiece(board.tiles[5][2],board.tiles[3][4]);

    }

    private void movePiece(Tile selectedTile, Tile moveToTile) {


        Position fromPos = selectedTile.getPosition();
        Position moveToPos = moveToTile.getPosition();

        PlayerColor piecePlayerColor = selectedTile.getPiece().getPieceColor();
        Color pieceColor;
        Boolean isKing = selectedTile.getPiece().getKing();
        if (piecePlayerColor.equals(PlayerColor.white)) pieceColor = WHITE_PIECE_COLOR;
        else pieceColor = BLACK_PIECE_COLOR;

        Piece movedPiece = new Piece(PIECE_SIZE, pieceColor, moveToPos, piecePlayerColor, isKing);

        movedPiece.setOnMouseClicked(eventPieceClicked(movedPiece));
        removePieceFromTile(fromPos.getxPos(), fromPos.getyPos());
        selectedTile.setPiece(null);
        addPieceToTile(moveToPos, movedPiece);
        moveToTile.setPiece(movedPiece);
        PlayerMove move = new PlayerMove(selectedTile, moveToTile);
        turnManager.AddMove(move);
        listViewMovesHistory.getItems().add(move.toString());

//add
    }

    private void removePieceFromTile(int xRow, int yColumn) {
        ObservableList<Node> childrens = gridBoard.getChildren();
        for (Node node : childrens) {
            if (node instanceof Tile &&
                    GridPane.getRowIndex(node) == xRow &&
                    GridPane.getColumnIndex(node) == yColumn) {
                Tile fromTile = (Tile) node;
                Piece piece = fromTile.getPiece();
                fromTile.setPiece(null);
                // moveToTile.setPiece(piece);
                fromTile.setPiece(null);
                gridBoard.getChildren().remove(piece);

                break;
            }
        }
        board.tiles[xRow][yColumn].setPiece(null);
    }

    private void addPieceToTile(Position moveToPos, Piece movedPiece) {
        //add
        gridBoard.add(movedPiece, moveToPos.getxPos(), moveToPos.getyPos());
        board.tiles[moveToPos.getxPos()][moveToPos.getyPos()].setPiece(movedPiece);

    }


}

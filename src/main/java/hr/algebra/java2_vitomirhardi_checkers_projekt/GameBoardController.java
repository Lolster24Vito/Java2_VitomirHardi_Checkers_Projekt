package hr.algebra.java2_vitomirhardi_checkers_projekt;

import hr.algebra.jave2_vitomirhardi_checkers_projekt.models.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameBoardController implements Initializable {
    @FXML
    private GridPane gridBoard;

    @FXML
    private Label labelPlayer1Name;
    @FXML
    private Label labelStatus;
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
    private final double SIDE_SIZE = 70;
    private final double PIECE_SIZE = SIDE_SIZE / 2;

    private final Color WHITE_COLOR = Color.rgb(150, 111, 51);
    private final Color BLACK_COLOR = Color.rgb(30, 0, 0);

    private final Color WHITE_PIECE_COLOR = Color.rgb(244, 245, 202);
    private final Color WHITE_PIECE_SELECTED_COLOR = Color.GREENYELLOW;

    private final Color BLACK_PIECE_COLOR = Color.rgb(71, 71, 64);
    private final Color BLACK_PIECE_SELECTED_COLOR = Color.ORANGE;

    private final Color AVAILABLE_MOVE_COLOR = Color.GREEN;
    private final Color AVAILABLE_MOVE_EAT = Color.RED;

    private Board board;

    private Position selectedPieceLocation;
    private List<PlayerMove> selectedAvailablePositions;

    TurnManager turnManager = new TurnManager();

    private PlayerColor colorTurn = PlayerColor.white;

    //sets up pieces board,and gridPanel
    public void InitPane() {

        board = new Board(new Tile[X_ROW_SIZE][Y_COLUMN_SIZE]);
        // Create 64 rectangles and add to pane
        int count = 0;
        for (int i = 0; i < X_ROW_SIZE; i++) {
            for (int j = 0; j < Y_COLUMN_SIZE; j++) {
                //rectangle
//TODO check this
// Tile tile = new Tile(SIDE_SIZE, SIDE_SIZE, SIDE_SIZE, SIDE_SIZE, new Position(i, j), count);
                Tile tile = new Tile(SIDE_SIZE, SIDE_SIZE, SIDE_SIZE, SIDE_SIZE, new Position(j, i), count);


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
                    tile.setPiece(piece);
                    gridBoard.add(piece, j, i);
                }
                if ((i >= 5) && count % 2 == 1) {
                    //PIECE_SIZE,Color.RED,i,j, PlayerColor.white);
                    Piece piece = new Piece(PIECE_SIZE, BLACK_PIECE_COLOR, new Position(j, i), PlayerColor.black);
                    piece.setOnMouseClicked(eventPieceClicked(piece));
                    tile.setPiece(piece);
                    gridBoard.add(piece, j, i);
                }
                board.tiles[j][i] = tile;
                count++;
            }
            count++;
        }
        //move single piece from starting zone
        //getAllAvailablePositionsForPlayer();
    }

    private EventHandler<MouseEvent> eventOnTileClick(Tile tile) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //Function moveToPosition
                //getFreeMovesOfSelectedPiece
                Tile selectedPiece=board.tiles[selectedPieceLocation.getX()][selectedPieceLocation.getY()];
                if (selectedPieceLocation != null && selectedPiece.hasPiece() &&
                        selectedPiece.getPiece().getPieceColor().equals(colorTurn)
                        && checkIfValidMove(tile.getTileData()))
                {
                    movePiece(selectedPiece
                            , board.tiles[tile.getPosition().getX()][tile.getPosition().getY()]);

                    //if there's more legal moves change
                    colorTurn = colorTurn.equals(PlayerColor.white) ? PlayerColor.black : PlayerColor.white;
                    clearSelectedPieceColor();
                    String playerName;

                    if (colorTurn.equals(PlayerColor.white)) {
                        labelPlayerTurn.setText(GameStartController.getWhitePlayer().getPlayerName());
                    }
                    if (colorTurn.equals(PlayerColor.black)) {
                        labelPlayerTurn.setText(GameStartController.getBlackPlayer().getPlayerName());
                    }
                }
            }
        };
    }

    private boolean checkIfValidMove(TileData tileData) {
        for (PlayerMove potentialMove : selectedAvailablePositions
        ) {
            if (potentialMove.getPosition().equals(tileData.getPosition()))
                return true;
        }
        return false;

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
        //show piece moves
        selectedPieceLocation = piece.getPos();
        clearSelectedPieceColor();
        selectedAvailablePositions = new ArrayList<>();
        selectedAvailablePositions = board.getLegalJumpsFrom(colorTurn, piece.getPieceData());
        //if find any only show these as legal moves
        if (selectedAvailablePositions.size() > 0) {
            //have to make jump
            for (PlayerMove move : selectedAvailablePositions
            ) {
                board.tiles[move.getPosition().getX()][move.getPosition().getY()].setFill(AVAILABLE_MOVE_EAT);
            }
        } else {
            selectedAvailablePositions = board.getLegalMovesFrom(colorTurn, piece.getPieceData());
            for (PlayerMove move : selectedAvailablePositions
            ) {
                board.tiles[move.getPosition().getX()][move.getPosition().getY()].setFill(AVAILABLE_MOVE_COLOR);
            }
        }
    }

    private void clearSelectedPieceColor() {
        if (selectedAvailablePositions != null && selectedAvailablePositions.stream().count() > 0)
            for (PlayerMove move : selectedAvailablePositions
            ) {

                int tileLocation = board.tiles[move.getPosition().getX()][move.getPosition().getY()].getTileLocation();
                Color fillColor = tileLocation % 2 == 0 ? WHITE_COLOR : BLACK_COLOR;
                board.tiles[move.getPosition().getX()][move.getPosition().getY()].setFill(fillColor);
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String whitePlayerName = GameStartController.getWhitePlayer().getPlayerName();
        labelPlayer1Name.setText(whitePlayerName);
        labelPlayer2Name.setText(GameStartController.getBlackPlayer().getPlayerName());
        labelPlayerTurn.setText(whitePlayerName);
        labelStatus.setText("");
        InitPane();
        initMoves();
//movePiece(board.tiles[5][2],board.tiles[3][4]);

    }

    private void initMoves() {
        //legalMoves = board.getLegalMoves(Checkers.CheckersData.RED);  // Get RED's legal moves.

    }

    private void movePiece(Tile selectedTile, Tile moveToTile) {
        Position fromPos = selectedTile.getPosition();
        Position moveToPos = moveToTile.getPosition();
        //todo addkill
        Optional<PlayerMove> move =selectedAvailablePositions.stream().filter(m->m.getPosition().equals(moveToPos)).findFirst();
        if(!move.isPresent())return;//move is invalid
                //new PlayerMove(selectedTile.getTileData().getPiece(), moveToTile.getPosition());


        TileData tileData = selectedTile.getTileData();
        Color pieceColor;
        if (tileData.getPiece().getPieceColor().equals(PlayerColor.white)) pieceColor = WHITE_PIECE_COLOR;
        else pieceColor = BLACK_PIECE_COLOR;

        Piece movedPiece = new Piece(PIECE_SIZE, pieceColor, tileData.getPiece(), moveToPos);

        movedPiece.setOnMouseClicked(eventPieceClicked(movedPiece));
        removePieceFromTile(fromPos.getX(), fromPos.getY());
        selectedTile.setPiece(null);
        addPieceToTile(moveToPos, movedPiece);

        moveToTile.setPiece(movedPiece);
        if(move.get().isJump()){
            //toPos-moveToPos/2  = direction
            //origin+direction=middle
            Position direction=new Position(
                    (moveToPos.getX()- fromPos.getX())/2,
                    ((moveToPos.getY())-fromPos.getY())/2);
           Position middlePosition=new Position(fromPos.getX()+direction.getY(),fromPos.getY()+direction.getY());
                board.addJump(board.tiles[middlePosition.getX()][middlePosition.getY()].getPiece().getPieceData());
                removePieceFromTile(middlePosition.getX(), middlePosition.getY());

        }

        turnManager.AddMove(move.get());
        listViewMovesHistory.getItems().add(move.toString());

//add
    }

    private void removePieceFromTile(int xRow, int yColumn) {
        ObservableList<Node> childrens = gridBoard.getChildren();
        for (Node node : childrens) {
            if (node instanceof Tile &&
                    GridPane.getRowIndex(node) == yColumn &&
                    GridPane.getColumnIndex(node) == xRow) {
                Tile fromTile = (Tile) node;
                Piece piece = fromTile.getPiece();
                //fromTile.setPiece(null);
                // moveToTile.setPiece(piece);
                gridBoard.getChildren().remove(piece);

                break;
            }
        }
        board.tiles[xRow][yColumn].setPiece(null);
    }

    private void addPieceToTile(Position moveToPos, Piece movedPiece) {
        //add
        gridBoard.add(movedPiece, moveToPos.getX(), moveToPos.getY());
        board.tiles[moveToPos.getX()][moveToPos.getY()].setPiece(movedPiece);
    }


}

package hr.algebra.java2_vitomirhardi_checkers_projekt.controllers;

import hr.algebra.Utils.ReflectionUtils;
import hr.algebra.Utils.TimerUtils;
import hr.algebra.java2_vitomirhardi_checkers_projekt.HelloApplication;
import hr.algebra.java2_vitomirhardi_checkers_projekt.LeaderboardResult;
import hr.algebra.java2_vitomirhardi_checkers_projekt.TurnManager;
import hr.algebra.java2_vitomirhardi_checkers_projekt.dal.RepositoryFactory;
import hr.algebra.java2_vitomirhardi_checkers_projekt.models.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GameBoardController implements Initializable {
    @FXML
    private GridPane gridBoard;

    @FXML
    private Label labelPlayerWhiteName;
    @FXML
    private Label labelStatus;
    @FXML
    private Label labelPlayerWhiteScore;
    @FXML
    private Label labelPlayerWhiteTime;
    @FXML
    private Label labelPlayerBlackName;
    @FXML
    private Label labelPlayerBlackScore;
    @FXML
    private Label labelPlayerBlackTime;

    @FXML
    private Label labelPlayerTurn;

    @FXML
    private ListView listViewMovesHistory;


    public GameBoardController() {

    }

    private final double SIDE_SIZE = 70;
    private final double PIECE_SIZE = SIDE_SIZE / 2;

    private final Color WHITE_COLOR = Color.rgb(150, 111, 51);
    private final Color BLACK_COLOR = Color.rgb(30, 0, 0);

    private final Color WHITE_PIECE_COLOR = Color.rgb(244, 245, 202);
    private final Color WHITE_PIECE_SELECTED_COLOR = Color.GREENYELLOW;

    private final Color BLACK_PIECE_COLOR = Color.rgb(71, 71, 64);
    private final Color BLACK_PIECE_SELECTED_COLOR = Color.rgb(110, 0, 0);

    private final Color AVAILABLE_MOVE_COLOR = Color.GREEN;
    private final Color AVAILABLE_MOVE_EAT = Color.RED;

    private Board board;

    private Position selectedPieceLocation;
    private List<PlayerMove> selectedAvailablePositions;

    private List<PlayerMove> allPlayerAvailablePositions;
    private boolean isJumpInTurn = false;


    TurnManager turnManager = new TurnManager();

    private PlayerColor colorTurn = PlayerColor.white;

    //sets up pieces board,and gridPanel
    private final int X_COLUMN_SIZE = 8;
    private final int Y_ROW_SIZE = 8;
    private Timer whiteTimer = new Timer();
    private Timer blackTimer = new Timer();
    private long whiteTimerSeconds = 0;
    private long blackTimerSeconds = 0;

    TimerTask whiteTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (colorTurn.equals(PlayerColor.white)) {


                whiteTimerSeconds++;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        labelPlayerWhiteTime.setText(TimerUtils.secondsToFormat(whiteTimerSeconds));
                    }
                });
            }

        }
    };
    TimerTask blackTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (colorTurn.equals(PlayerColor.black)) {

                blackTimerSeconds++;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        labelPlayerBlackTime.setText(TimerUtils.secondsToFormat(blackTimerSeconds));
                    }
                });
            }
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLabels();
        InitPane();
        allPlayerAvailablePositions = board.getPlayerLegalMoves(colorTurn);
        isJumpInTurn = allPlayerAvailablePositions.stream().anyMatch(p -> p.isJump());
        refreshScore();
        whiteTimer.scheduleAtFixedRate(whiteTimerTask, 0, 1000);
        blackTimer.scheduleAtFixedRate(blackTimerTask, 0, 1000);


    }

    public void InitPane() {


        board = new Board(X_COLUMN_SIZE, Y_ROW_SIZE);
        // Create 64 rectangles and add to pane
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


    }


    private void highlightJumps() {
        if (allPlayerAvailablePositions != null && allPlayerAvailablePositions.size() > 0 && isJumpInTurn) {
            for (PlayerMove possibleMove : allPlayerAvailablePositions) {
                if (possibleMove.isJump()) {
                    Position piecePos = possibleMove.getPiecePosition();
                    board.tiles[piecePos.getX()][piecePos.getY()].setFill(BLACK_PIECE_SELECTED_COLOR);
                }
            }
        }
    }

    private EventHandler<MouseEvent> eventOnTileClick(Tile tile) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                //Function moveToPosition
                //getFreeMovesOfSelectedPiece
                if (selectedPieceLocation != null) {
                    Tile selectedPiece = board.tiles[selectedPieceLocation.getX()][selectedPieceLocation.getY()];
                    if (selectedPiece.hasPiece()
                            && selectedPiece.getPiece().getPieceColor().equals(colorTurn)
                            && checkIfValidMove(tile.getTileData())
                    ) {

                        Position tilePos = tile.getPosition();
                        movePiece(selectedPiece
                                , board.tiles[tilePos.getX()][tilePos.getY()]);
                        Optional<PlayerMove> doneMove = selectedAvailablePositions.stream().filter(m -> m.getPosition().equals(tilePos)).findFirst();


                        //if there's more legal jumps for player dont change
                        Boolean anotherJump = false;
                        if (doneMove.get().isJump()) {
                            if (board.tiles[tilePos.getX()][tilePos.getY()].hasPiece()) {
                                ArrayList<PlayerMove> legalJumpsFromNewPos = board.getLegalJumpsFrom(colorTurn, board.tiles[tilePos.getX()][tilePos.getY()]
                                        .getTileData().getPiece());
                                if (legalJumpsFromNewPos.size() > 0) {
                                    clearJumpHighlights();
                                    clearSelectedPieceColor();
                                    allPlayerAvailablePositions = legalJumpsFromNewPos;
                                    anotherJump = true;
                                }

                            }
                        }
                        if (!anotherJump)
                            changePlayerTurn();


                    }
                }
            }
        };
    }

    private void changePlayerTurn() {
        colorTurn = colorTurn.equals(PlayerColor.white) ? PlayerColor.black : PlayerColor.white;
        clearSelectedPieceColor();
        clearJumpHighlights();
        String playerName;

        if (colorTurn.equals(PlayerColor.white)) {
            labelPlayerTurn.setText(GameStartController.getWhitePlayer().getPlayerName());

        }
        if (colorTurn.equals(PlayerColor.black)) {
            labelPlayerTurn.setText(GameStartController.getBlackPlayer().getPlayerName());
        }
        allPlayerAvailablePositions = board.getPlayerLegalMoves(colorTurn);


        if (allPlayerAvailablePositions.isEmpty()) {
            try {
                playerWin();
            } catch (Exception e) {
            }
        }
        isJumpInTurn = allPlayerAvailablePositions.stream().anyMatch(p -> p.isJump());
        highlightJumps();

    }

    private void playerWin() throws IOException {

        LeaderboardResult leaderboardResult;
        if (colorTurn == PlayerColor.black) {
            leaderboardResult = new LeaderboardResult(GameStartController.getWhitePlayer().getPlayerName(), whiteTimerSeconds, board.getWhiteScore(), PlayerColor.white);

        } else {
            //black is winner
            leaderboardResult = new LeaderboardResult(GameStartController.getBlackPlayer().getPlayerName(), blackTimerSeconds, board.getBlackScore(), PlayerColor.black);

        }


        whiteTimerTask.cancel();
        blackTimerTask.cancel();

        try {
            RepositoryFactory.getLeaderboardRepository().setLeaderboardResult(leaderboardResult);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("GameWinScreen.fxml"));
        Scene scene = null;
        scene = new Scene(fxmlLoader.load(), 1200, 768);
        GameWinController gameWinController = fxmlLoader.getController();
        gameWinController.setWinner(leaderboardResult);
        gameWinController.setMoves(turnManager.getMoves());

        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(HelloApplication.getMainStage());

        HelloApplication.setPopupStage(stage);

        HelloApplication.getPopupStage().setTitle("GameWinScreen.fxml");
        HelloApplication.getPopupStage().setScene(scene);
        HelloApplication.getPopupStage().showAndWait();
//showandwait()
        // GameWinController gameWinController=loader.gerController();
/*
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(colorTurn==PlayerColor.black?
                String.format("white player %s is winner",GameStartController.getWhitePlayer().getPlayerName())
                :String.format("black player %s is winner",GameStartController.getBlackPlayer().getPlayerName())
        );
a.show();*/


    }

    private void clearJumpHighlights() {
        for (PlayerMove move : allPlayerAvailablePositions) {
            if (move.isJump()) {
                Position piecePos = move.getPiecePosition();
                board.tiles[piecePos.getX()][piecePos.getY()].setFill(BLACK_COLOR);
            }
        }
    }

    private boolean checkIfValidMove(TileData tileData) {
        //if there's a jump in selected,it has to do it

        //if there is a jump in another piece it has to jump
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
            if (!isJumpInTurn) {
                selectedAvailablePositions = board.getLegalMovesFrom(colorTurn, piece.getPieceData());
                for (PlayerMove move : selectedAvailablePositions
                ) {
                    board.tiles[move.getPosition().getX()][move.getPosition().getY()].setFill(AVAILABLE_MOVE_COLOR);
                }
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


    private void initLabels() {
        String whitePlayerName = GameStartController.getWhitePlayer().getPlayerName();
        labelPlayerWhiteName.setText(whitePlayerName);
        labelPlayerBlackName.setText(GameStartController.getBlackPlayer().getPlayerName());
        labelPlayerTurn.setText(whitePlayerName);
        labelStatus.setText("");
    }


    private void movePiece(Tile selectedTile, Tile moveToTile) {
        Position fromPos = selectedTile.getPosition();
        Position moveToPos = moveToTile.getPosition();
        Optional<PlayerMove> move = selectedAvailablePositions.stream().filter(m -> m.getPosition().equals(moveToPos)).findFirst();
        if (!move.isPresent()) return;//move is invalid
        //new PlayerMove(selectedTile.getTileData().getPiece(), moveToTile.getPosition());

        //if is jump remove tile
        if (move.get().isJump()) {
            //toPos-moveToPos/2  = direction
            //origin+direction=middle
            Position direction = new Position(
                    (moveToPos.getX() - fromPos.getX()) / 2,
                    ((moveToPos.getY()) - fromPos.getY()) / 2);
            Position middlePosition = new Position(fromPos.getX() + direction.getX(), fromPos.getY() + direction.getY());
            board.addJump(board.tiles[middlePosition.getX()][middlePosition.getY()].getPiece().getPieceData());
            removePieceFromTile(middlePosition.getX(), middlePosition.getY());
            refreshScore();
        }

        PlayerMove turnMove = new PlayerMove(move.get().getPieceToMove(), move.get().getPosition(), move.get().getMoveJump());
        turnManager.AddMove(turnMove);
        listViewMovesHistory.getItems().add(move.get().toString());


        PieceData movePieceData = selectedTile.getTileData().getPiece();
        Color pieceColor;
        if (movePieceData.getPieceColor().equals(PlayerColor.white)) {
            pieceColor = WHITE_PIECE_COLOR;
            //check if should add king
            if (moveToPos.getY() == Y_ROW_SIZE - 1) {
                movePieceData.setKing();
            }
        } else {
            pieceColor = BLACK_PIECE_COLOR;
            if (moveToPos.getY() == 0) {
                movePieceData.setKing();
            }
        }

        Piece movedPiece = new Piece(PIECE_SIZE, pieceColor, movePieceData, moveToPos);

        movedPiece.setOnMouseClicked(eventPieceClicked(movedPiece));
        removePieceFromTile(fromPos.getX(), fromPos.getY());
        selectedTile.setPiece(null);
        addPieceToTile(moveToPos, movedPiece);

        moveToTile.setPiece(movedPiece);


//add
    }

    private void refreshScore() {
        labelPlayerBlackScore.setText(Integer.toString(board.getBlackScore()));
        labelPlayerWhiteScore.setText(Integer.toString(board.getWhiteScore()));

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

    public void saveGame() throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose where to save your game");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Save files", "*.ser"));
        File selectedDirectory = fileChooser.showSaveDialog(HelloApplication.getMainStage());

        String name = selectedDirectory.getAbsolutePath();

        //todo add saveTo window
        List<PieceData> pieces = new ArrayList<>();

        for (int i = 0; i < X_COLUMN_SIZE; i++) {
            for (int j = 0; j < Y_ROW_SIZE; j++) {
                if (board.tiles[j][i].hasPiece()) {
                    pieces.add(board.tiles[j][i].getTileData().getPiece());
                }

            }
        }
        SerializableBoard serializableBoard = new SerializableBoard(
                pieces, colorTurn,
                GameStartController.getWhitePlayer().getPlayerName(),
                GameStartController.getBlackPlayer().getPlayerName(),
                whiteTimerSeconds, blackTimerSeconds
                , board.getEatenWhitePieces(), board.getEatenWhiteKings(), board.getEatenBlackPieces(), board.getEatenBlackKings());

        try (ObjectOutputStream serializator = new ObjectOutputStream(
                new FileOutputStream(name))) {
            serializator.writeObject(serializableBoard);
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("File has not been saved due to an error");
            a.show();

        }

    }

    public void loadGame() throws IOException, ClassNotFoundException {
        //todo add load window
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your save file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Save files", "*.ser"));
        File selectedDirectory = fileChooser.showOpenDialog(HelloApplication.getMainStage());
        if (selectedDirectory == null) return;
        String name = selectedDirectory.getAbsolutePath();
        if (!selectedDirectory.exists()) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("No load files have been selected");
            a.show();
            return;
        }
        try (ObjectInputStream deserializator = new ObjectInputStream((
                new FileInputStream(name)
        ))) {
            SerializableBoard serializableBoard = (SerializableBoard) deserializator.readObject();


//remove everything from board
            clearBoard();
            //load pieces
            for (PieceData pieceD : serializableBoard.getSerializablePieces()) {
                loadPieceToTile(pieceD);
            }
            //load match data
            whiteTimerSeconds = serializableBoard.getWhiteTimerSeconds();
            blackTimerSeconds = serializableBoard.getBlackTimerSeconds();
            labelPlayerWhiteTime.setText(TimerUtils.secondsToFormat(whiteTimerSeconds));
            labelPlayerBlackTime.setText(TimerUtils.secondsToFormat(blackTimerSeconds));


            GameStartController.setWhitePlayerName(serializableBoard.getWhitePlayerName());
            GameStartController.setBlackPlayerName(serializableBoard.getBlackPlayerName());
            labelPlayerBlackName.setText(serializableBoard.getBlackPlayerName());
            labelPlayerWhiteName.setText(serializableBoard.getWhitePlayerName());
            colorTurn = serializableBoard.getPlayerTurn();
            board.setEatenWhitePieces(serializableBoard.getEatenWhitePieces(), serializableBoard.getEatenWhiteKings());
            board.setEatenBlackPieces(serializableBoard.getEatenBlackPieces(), serializableBoard.getEatenBlackKings());

            allPlayerAvailablePositions = board.getPlayerLegalMoves(colorTurn);
            if (allPlayerAvailablePositions.isEmpty()) {
                playerWin();
            }
            isJumpInTurn = allPlayerAvailablePositions.stream().anyMatch(p -> p.isJump());
            highlightJumps();
            refreshScore();

        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("File has not been loaded due to an error");
            a.show();
        }

    }

    private void loadPieceToTile(PieceData pieceD) {
        Color pieceColor = pieceD.getPieceColor() == PlayerColor.white ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR;
        Piece piece = new Piece(PIECE_SIZE, pieceColor, pieceD);
        piece.setOnMouseClicked(eventPieceClicked(piece));
        Position piecePos = pieceD.getPos();

        board.tiles[piecePos.getX()][piecePos.getY()].setPiece(piece);
        gridBoard.add(piece, piecePos.getX(), piecePos.getY());
    }

    private void clearBoard() {
        for (int i = 0; i < X_COLUMN_SIZE; i++) {
            for (int j = 0; j < Y_ROW_SIZE; j++) {
                removePieceFromTile(i, j);
            }
        }
    }


    public void dispose() {
        blackTimerTask.cancel();
        whiteTimerTask.cancel();
    }


    public void generateDocumentation() {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>");
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<title>Naša dokumentacija</title>");
        builder.append("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\" crossorigin=\"anonymous\">\n");
        builder.append("</head>");
        builder.append("<body> <div class=\"container\">");
        builder.append("<h1>HTML dokumentacija projektnog zadatka</h1>");
        builder.append("<h1>Popis klasa:</h1></br>");

        try {
            List<Path> pathsList = Files.walk(Paths.get("."))
                    .filter(path -> path.getFileName().toString().endsWith(".class"))
                    .collect(Collectors.toList());
            for (Path path : pathsList) {
                String fileName = path.getFileName().toString();

                String fullQualifiedName = getFullQualifiedName(path);


                if ("module-info".equals(fullQualifiedName)) {
                    continue;
                }

//Klas name
                Class<?> clazz = Class.forName(fullQualifiedName);
                if (!clazz.isAnonymousClass()) {
                    ReflectionUtils.readClassInfo(clazz, builder);
                }


            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        builder.append("</div>");

        builder.append("</body>");
        builder.append("</html>");

        try (FileWriter fw = new FileWriter("documentation.html")) {
            fw.write(builder.toString());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Documentation generated successfuly!");
            alert.setContentText("The file \"documentation.html\" has been generated!");

            alert.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while creating documentation file");
            alert.setHeaderText("Cannot generate documentation");
            alert.setContentText("Details: " + e.getMessage());

            alert.showAndWait();
        }

    }

    private static String getFullQualifiedName(Path path) {
        StringTokenizer tokenizer = new StringTokenizer(path.toString(), "\\");

        String fullQualifiedName = "";

        Boolean packageStart = false;

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            if ("classes".equals(token)) {
                packageStart = true;
                continue;
            }

            if (packageStart == false) {
                continue;
            }

            if (token.endsWith(".class")) {
                token = token.substring(0, token.indexOf("."));
                fullQualifiedName += token;
                break;
            }

            fullQualifiedName += token;
            fullQualifiedName += ".";

            //System.out.println("Token: " + token);
        }
        return fullQualifiedName;
    }
}

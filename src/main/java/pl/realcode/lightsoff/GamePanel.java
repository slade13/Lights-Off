package pl.realcode.lightsoff;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/** Reusable JavaFX game view for every supported board size. */
public final class GamePanel extends BorderPane {
    private static final double BOARD_SIZE = 400;

    private final LightsOutBoard board;
    private final Button hintButton = new Button("Show hints");
    private final BoardView boardView;

    private boolean hintsVisible;
    private boolean hintsUsed;
    private int[] solution;

    public GamePanel(int size, String difficulty, Runnable onBack) {
        board = new LightsOutBoard(size);
        boardView = new BoardView(size, BOARD_SIZE, this::pressCell);
        getStyleClass().add("game-screen");
        setPadding(new Insets(24));
        setTop(createHeading(difficulty));
        setCenter(createBoard());
        setBottom(createControls(onBack));
        renderBoard();
    }

    private Label createHeading(String difficulty) {
        Label heading = new Label(difficulty + " — " + board.getSize() + "×" + board.getSize());
        heading.getStyleClass().add("screen-title");
        BorderPane.setAlignment(heading, Pos.CENTER);
        BorderPane.setMargin(heading, new Insets(0, 0, 18, 0));
        return heading;
    }

    private BoardView createBoard() {
        boardView.setAlignment(Pos.CENTER);
        return boardView;
    }

    private HBox createControls(Runnable onBack) {
        hintButton.getStyleClass().add("secondary-button");
        hintButton.setOnAction(event -> toggleHints());
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("secondary-button");
        backButton.setOnAction(event -> onBack.run());
        HBox controls = new HBox(16, hintButton, backButton);
        controls.setAlignment(Pos.CENTER);
        BorderPane.setMargin(controls, new Insets(20, 0, 0, 0));
        return controls;
    }

    private void pressCell(int row, int column) {
        board.press(row, column);
        solution = hintsVisible ? board.findSolution() : null;
        renderBoard();
        if (board.isSolved()) {
            PauseTransition pause = new PauseTransition(Duration.millis(180));
            pause.setOnFinished(event -> Platform.runLater(this::finishGame));
            pause.play();
        }
    }

    private void toggleHints() {
        hintsVisible = !hintsVisible;
        hintsUsed |= hintsVisible;
        solution = hintsVisible ? board.findSolution() : null;
        hintButton.setText(hintsVisible ? "Hide hints" : "Show hints");
        renderBoard();
    }

    private void renderBoard() {
        boardView.render(board, hintsVisible ? solution : null);
    }

    private void finishGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lights Off");
        alert.setHeaderText("You won!");
        alert.setContentText(hintsUsed ? "Puzzle solved with hints." : "Puzzle solved without hints.");
        alert.showAndWait();
        board.reset();
        hintsVisible = false;
        hintsUsed = false;
        solution = null;
        hintButton.setText("Show hints");
        renderBoard();
    }
}

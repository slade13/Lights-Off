package pl.realcode.lightsoff;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

/** Reusable JavaFX game view for every supported board size. */
public final class GamePanel extends BorderPane {
    private static final double BOARD_SIZE = 400;

    private final LightsOutBoard board;
    private final Button[][] cells;
    private final Button hintButton = new Button("Show hints");
    private final GridPane grid = new GridPane();

    private boolean hintsVisible;
    private boolean hintsUsed;
    private int[] solution;

    public GamePanel(int size, String difficulty, Runnable onBack) {
        board = new LightsOutBoard(size);
        cells = new Button[size][size];
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

    private GridPane createBoard() {
        grid.setAlignment(Pos.CENTER);
        grid.setMaxSize(BOARD_SIZE, BOARD_SIZE);
        grid.setPrefSize(BOARD_SIZE, BOARD_SIZE);
        grid.getStyleClass().add("board");
        for (int index = 0; index < board.getSize(); index++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / board.getSize());
            column.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(column);
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / board.getSize());
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
        }
        for (int row = 0; row < board.getSize(); row++) {
            for (int column = 0; column < board.getSize(); column++) {
                Button cell = new Button();
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cell.getStyleClass().add("board-cell");
                int selectedRow = row;
                int selectedColumn = column;
                cell.setOnAction(event -> pressCell(selectedRow, selectedColumn));
                cells[row][column] = cell;
                grid.add(cell, column, row);
            }
        }
        return grid;
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
        for (int row = 0; row < board.getSize(); row++) {
            for (int column = 0; column < board.getSize(); column++) {
                Button cell = cells[row][column];
                cell.getStyleClass().removeAll("light-on", "light-off", "hint");
                cell.getStyleClass().add(board.isLightOn(row, column) ? "light-on" : "light-off");
                int index = row * board.getSize() + column;
                if (hintsVisible && solution != null && solution[index] == 1) {
                    cell.getStyleClass().add("hint");
                }
            }
        }
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

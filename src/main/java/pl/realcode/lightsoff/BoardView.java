package pl.realcode.lightsoff;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

import java.util.function.BiConsumer;

/** Shared visual representation used by both gameplay and tutorial previews. */
final class BoardView extends GridPane {
    private final int size;
    private final Button[][] cells;

    BoardView(int size, double preferredSize, BiConsumer<Integer, Integer> onCellPressed) {
        this.size = size;
        this.cells = new Button[size][size];
        setMaxSize(preferredSize, preferredSize);
        setPrefSize(preferredSize, preferredSize);
        getStyleClass().add("board");

        for (int index = 0; index < size; index++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / size);
            column.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(column);

            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / size);
            row.setVgrow(Priority.ALWAYS);
            getRowConstraints().add(row);
        }

        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                Button cell = new Button();
                cell.setFocusTraversable(false);
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cell.getStyleClass().add("board-cell");
                if (onCellPressed != null) {
                    int selectedRow = row;
                    int selectedColumn = column;
                    cell.setOnAction(event -> onCellPressed.accept(selectedRow, selectedColumn));
                    cell.setOnMouseEntered(event -> animateScale(cell, 1.04, 110));
                    cell.setOnMouseExited(event -> animateScale(cell, 1.0, 110));
                } else {
                    cell.setMouseTransparent(true);
                }
                cells[row][column] = cell;
                add(cell, column, row);
            }
        }
    }

    void render(LightsOutBoard board, int[] highlightedCells) {
        render((row, column) -> board.isLightOn(row, column), highlightedCells);
    }

    void render(int[][] state, int[] highlightedCells) {
        render((row, column) -> state[row][column] == 1, highlightedCells);
    }

    void animateMove(int pressedRow, int pressedColumn) {
        int[][] offsets = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        ParallelTransition animation = new ParallelTransition();
        for (int[] offset : offsets) {
            int row = pressedRow + offset[0];
            int column = pressedColumn + offset[1];
            if (row >= 0 && row < size && column >= 0 && column < size) {
                ScaleTransition pulse = new ScaleTransition(Duration.millis(140), cells[row][column]);
                pulse.setFromX(0.82);
                pulse.setFromY(0.82);
                pulse.setToX(1.0);
                pulse.setToY(1.0);
                animation.getChildren().add(pulse);
            }
        }
        animation.play();
    }

    private static void animateScale(Button cell, double scale, double durationMillis) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(durationMillis), cell);
        transition.setToX(scale);
        transition.setToY(scale);
        transition.play();
    }

    private void render(CellState state, int[] highlightedCells) {
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                Button cell = cells[row][column];
                cell.getStyleClass().removeAll("light-on", "light-off", "hint");
                cell.getStyleClass().add(state.isOn(row, column) ? "light-on" : "light-off");
                int index = row * size + column;
                if (highlightedCells != null && highlightedCells[index] == 1) {
                    cell.getStyleClass().add("hint");
                }
            }
        }
    }

    @FunctionalInterface
    private interface CellState {
        boolean isOn(int row, int column);
    }
}

package pl.realcode.lightsoff;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LightsOutBoardTest {

    @Test
    void pressingCornerTogglesOnlyCornerAndItsTwoNeighbours() {
        LightsOutBoard board = new LightsOutBoard(3, new Random(7));
        int[][] before = board.copyCells();

        board.press(0, 0);

        int[][] after = board.copyCells();
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                boolean shouldChange = row + column <= 1;
                if (shouldChange == (before[row][column] == after[row][column])) {
                    throw new AssertionError("Unexpected state at " + row + "," + column);
                }
            }
        }
    }

    @Test
    void resetCreatesNonSolvedBoardWithSolution() {
        LightsOutBoard board = new LightsOutBoard(5, new Random(11));

        assertFalse(board.isSolved());
        assertNotNull(board.findSolution());
    }

    @Test
    void rejectsCoordinatesOutsideBoard() {
        LightsOutBoard board = new LightsOutBoard(3, new Random(3));

        assertThrows(IndexOutOfBoundsException.class, () -> board.press(3, 0));
    }
}

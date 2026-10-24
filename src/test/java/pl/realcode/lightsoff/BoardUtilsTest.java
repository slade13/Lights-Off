package pl.realcode.lightsoff;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardUtilsTest {

    @Test
    void testIsSolved() {
        int[][] testArray = new int[5][5];

        boolean result = BoardUtils.isSolved(testArray);

        assertTrue(result);
    }

    @Test
    void testIsNotSolved() {
        int[][] testArray = {{1,0,0},{0,0,0},{0,1,0}};

        boolean result = BoardUtils.isSolved(testArray);

        assertFalse(result);
    }

    @Test
    void testMatrixToVector() {
        int[][] testArray = {{1, 0, 1}, {1, 1, 1}, {0, 1, 0}};
        int[] expectedVector = {1, 0, 1, 1, 1, 1, 0, 1, 0};

        int[] result = BoardUtils.matrixToVector(testArray);

        assertArrayEquals(expectedVector, result);
    }

    @Test
    void generatedBoardsAreSolvable() {
        for (int size = 3; size <= 5; size++) {
            for (int attempt = 0; attempt < 100; attempt++) {
                int[][] board = BoardUtils.randBoard(size);

                assertFalse(BoardUtils.isSolved(board));
                assertNotNull(BoardUtils.solve(board));
            }
        }
    }

    @Test
    void solutionTurnsEveryLightOff() {
        int[][] board = {{1, 0, 1}, {1, 1, 0}, {0, 1, 1}};
        int[] solution = BoardUtils.solve(board);

        assertNotNull(solution);
        for (int press = 0; press < solution.length; press++) {
            if (solution[press] == 1) {
                toggle(board, press / board.length, press % board.length);
            }
        }
        assertTrue(BoardUtils.isSolved(board));
    }

    private static void toggle(int[][] board, int row, int column) {
        int[][] offsets = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] offset : offsets) {
            int targetRow = row + offset[0];
            int targetColumn = column + offset[1];
            if (targetRow >= 0 && targetRow < board.length
                    && targetColumn >= 0 && targetColumn < board.length) {
                board[targetRow][targetColumn] ^= 1;
            }
        }
    }

}

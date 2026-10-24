package pl.realcode.lightsoff;

import java.util.Random;

public abstract class BoardUtils {

    public static int[][] randBoard(int size) {
        Random rand = new Random();
        int[][] randomizedBoard = new int[size][size];
        do {
            randomizedBoard = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (rand.nextBoolean()) {
                        toggle(randomizedBoard, i, j);
                    }
                }
            }
        } while (isSolved(randomizedBoard));

        return randomizedBoard;
    }

    private static void toggle(int[][] board, int row, int column) {
        int[][] offsets = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] offset : offsets) {
            int targetRow = row + offset[0];
            int targetColumn = column + offset[1];
            if (targetRow >= 0 && targetRow < board.length
                    && targetColumn >= 0 && targetColumn < board[targetRow].length) {
                board[targetRow][targetColumn] ^= 1;
            }
        }
    }

    /**
     * Returns one set of button presses that solves the board, or {@code null}
     * when the supplied state is not reachable.
     */
    public static int[] solve(int[][] board) {
        int rows = board.length;
        int columns = board[0].length;
        int variables = rows * columns;
        int[][] matrix = new int[variables][variables + 1];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                int equation = row * columns + column;
                matrix[equation][variables] = board[row][column];
                for (int pressRow = 0; pressRow < rows; pressRow++) {
                    for (int pressColumn = 0; pressColumn < columns; pressColumn++) {
                        int distance = Math.abs(row - pressRow) + Math.abs(column - pressColumn);
                        if (distance <= 1) {
                            matrix[equation][pressRow * columns + pressColumn] = 1;
                        }
                    }
                }
            }
        }

        int pivotRow = 0;
        int[] pivotColumns = new int[variables];
        for (int column = 0; column < variables && pivotRow < variables; column++) {
            int candidate = pivotRow;
            while (candidate < variables && matrix[candidate][column] == 0) {
                candidate++;
            }
            if (candidate == variables) {
                continue;
            }

            int[] temporary = matrix[pivotRow];
            matrix[pivotRow] = matrix[candidate];
            matrix[candidate] = temporary;

            for (int row = 0; row < variables; row++) {
                if (row != pivotRow && matrix[row][column] == 1) {
                    for (int value = column; value <= variables; value++) {
                        matrix[row][value] ^= matrix[pivotRow][value];
                    }
                }
            }
            pivotColumns[pivotRow] = column;
            pivotRow++;
        }

        for (int row = pivotRow; row < variables; row++) {
            if (matrix[row][variables] == 1) {
                return null;
            }
        }

        int[] solution = new int[variables];
        for (int row = 0; row < pivotRow; row++) {
            solution[pivotColumns[row]] = matrix[row][variables];
        }
        return solution;
    }

    public static boolean isSolved(int[][] board) {
        for (int[] row : board) {
            for (int value : row) {
                if (value == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[] matrixToVector(int[][] matrix) {
        int sizeM = matrix.length;
        int sizeN = matrix[0].length;
        int[] vector = new int[sizeM * sizeN];
        int index = 0;
        for (int[] row : matrix) {
            for (int value : row) {
                vector[index] = value;
                index++;
            }
        }
        return vector;
    }

    public static String toString(int[][] board) {
        StringBuilder boardString = new StringBuilder();
        for (int[] row : board) {
            for (int value : row) {
                boardString.append(value).append(" ");
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }
}

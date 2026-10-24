package pl.realcode.lightsoff;

import java.util.Random;

/** Utility algorithms shared by the game model and its tests. */
public final class BoardUtils {

    private static final int[][] NEIGHBOUR_OFFSETS = {
            {0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    private BoardUtils() {
    }

    public static int[][] randomSolvableBoard(int size) {
        return randomSolvableBoard(size, new Random());
    }

    static int[][] randomSolvableBoard(int size, Random random) {
        if (size < 1) {
            throw new IllegalArgumentException("Board size must be positive");
        }

        int[][] board;
        do {
            board = new int[size][size];
            for (int row = 0; row < size; row++) {
                for (int column = 0; column < size; column++) {
                    if (random.nextBoolean()) {
                        toggle(board, row, column);
                    }
                }
            }
        } while (isSolved(board));
        return board;
    }

    public static boolean isSolved(int[][] board) {
        validateBoard(board);
        for (int[] row : board) {
            for (int value : row) {
                if (value != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[] matrixToVector(int[][] matrix) {
        validateBoard(matrix);
        int[] vector = new int[matrix.length * matrix[0].length];
        int index = 0;
        for (int[] row : matrix) {
            for (int value : row) {
                vector[index++] = value;
            }
        }
        return vector;
    }

    /**
     * Solves the board over GF(2). Free variables are set to zero. Returns
     * {@code null} when the supplied state is not reachable.
     */
    public static int[] solve(int[][] board) {
        validateBoard(board);
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

        int rank = reduce(matrix, variables);
        for (int row = rank; row < variables; row++) {
            if (matrix[row][variables] == 1) {
                return null;
            }
        }

        int[] solution = new int[variables];
        for (int row = 0; row < rank; row++) {
            int pivotColumn = firstOne(matrix[row], variables);
            solution[pivotColumn] = matrix[row][variables];
        }
        return solution;
    }

    static void toggle(int[][] board, int row, int column) {
        validateBoard(board);
        if (row < 0 || row >= board.length || column < 0 || column >= board[0].length) {
            throw new IndexOutOfBoundsException("Cell outside board");
        }
        for (int[] offset : NEIGHBOUR_OFFSETS) {
            int targetRow = row + offset[0];
            int targetColumn = column + offset[1];
            if (targetRow >= 0 && targetRow < board.length
                    && targetColumn >= 0 && targetColumn < board[0].length) {
                board[targetRow][targetColumn] ^= 1;
            }
        }
    }

    private static int reduce(int[][] matrix, int variableCount) {
        int pivotRow = 0;
        for (int column = 0; column < variableCount && pivotRow < matrix.length; column++) {
            int candidate = pivotRow;
            while (candidate < matrix.length && matrix[candidate][column] == 0) {
                candidate++;
            }
            if (candidate == matrix.length) {
                continue;
            }

            int[] temporary = matrix[pivotRow];
            matrix[pivotRow] = matrix[candidate];
            matrix[candidate] = temporary;

            for (int row = 0; row < matrix.length; row++) {
                if (row != pivotRow && matrix[row][column] == 1) {
                    for (int value = column; value <= variableCount; value++) {
                        matrix[row][value] ^= matrix[pivotRow][value];
                    }
                }
            }
            pivotRow++;
        }
        return pivotRow;
    }

    private static int firstOne(int[] row, int limit) {
        for (int column = 0; column < limit; column++) {
            if (row[column] == 1) {
                return column;
            }
        }
        throw new IllegalStateException("Reduced row has no pivot");
    }

    private static void validateBoard(int[][] board) {
        if (board == null || board.length == 0 || board[0] == null || board[0].length == 0) {
            throw new IllegalArgumentException("Board cannot be empty");
        }
        int columns = board[0].length;
        for (int[] row : board) {
            if (row == null || row.length != columns) {
                throw new IllegalArgumentException("Board must be rectangular");
            }
            for (int value : row) {
                if (value != 0 && value != 1) {
                    throw new IllegalArgumentException("Board values must be zero or one");
                }
            }
        }
    }
}

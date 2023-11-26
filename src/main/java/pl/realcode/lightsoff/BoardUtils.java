package pl.realcode.lightsoff;

import java.util.Random;

public abstract class BoardUtils {

    public static int[][] randBoard(int size) {
        Random rand = new Random();
        int[][] randomizedBoard = new int[size][size];
        do {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    randomizedBoard[i][j] = rand.nextInt(2);
                }
            }
        } while (isSolved(randomizedBoard));

        return randomizedBoard;
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

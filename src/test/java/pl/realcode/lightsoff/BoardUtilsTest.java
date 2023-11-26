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

}

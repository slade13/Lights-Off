package pl.realcode.lightsoff;

import java.util.Random;

/** Mutable game model independent from Swing. */
public final class LightsOutBoard {

    private final int size;
    private final Random random;
    private int[][] cells;

    public LightsOutBoard(int size) {
        this(size, new Random());
    }

    LightsOutBoard(int size, Random random) {
        if (size < 1) {
            throw new IllegalArgumentException("Board size must be positive");
        }
        this.size = size;
        this.random = random;
        reset();
    }

    public int getSize() {
        return size;
    }

    public boolean isLightOn(int row, int column) {
        checkCoordinates(row, column);
        return cells[row][column] == 1;
    }

    public void press(int row, int column) {
        checkCoordinates(row, column);
        BoardUtils.toggle(cells, row, column);
    }

    public boolean isSolved() {
        return BoardUtils.isSolved(cells);
    }

    public int[] findSolution() {
        return BoardUtils.solve(cells);
    }

    public void reset() {
        cells = BoardUtils.randomSolvableBoard(size, random);
    }

    int[][] copyCells() {
        int[][] copy = new int[size][size];
        for (int row = 0; row < size; row++) {
            System.arraycopy(cells[row], 0, copy[row], 0, size);
        }
        return copy;
    }

    private void checkCoordinates(int row, int column) {
        if (row < 0 || row >= size || column < 0 || column >= size) {
            throw new IndexOutOfBoundsException("Cell outside board");
        }
    }
}

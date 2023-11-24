package com.rc.lightsoff;

public class Utilities {
	// swap()
	// swap row i with row k
	// pre: A[i][q]==A[k][q]==0 for 1<=q<j
	private static void swap(int[][] A, int i, int k, int j) {
		int m = A[0].length;
		int temp;
		for (int q = j; q < m; q++) {
			temp = A[i][q];
			A[i][q] = A[k][q];
			A[k][q] = temp;
		}
	}

	// divide()
	// divide row i by A[i][j]
	// pre: A[i][j]!=0, A[i][q]==0 for 1<=q<j
	// post: A[i][j]==1;
	private static void divide(int[][] A, int i, int j) {
		int m = A[0].length;
		for (int q = j + 1; q < m; q++)
			A[i][q] /= A[i][j];
		A[i][j] = 1;
	}

	// eliminate()
	// subtract an appropriate multiple of row i from every other row
	// pre: A[i][j]==1, A[i][q]==0 for 1<=q<j
	// post: A[p][j]==0 for p!=i
	private static void eliminate(int[][] A, int i, int j) {
		int n = A.length;
		int m = A[0].length;
		for (int p = 0; p < n; p++) {
			if (p != i && A[p][j] != 0) {
				for (int q = j + 1; q < m; q++) {
					A[p][q] = Math.abs((A[p][q] - A[p][j] * A[i][q]) % 2);
				}
				A[p][j] = 0;
			}
		}
	}

	// printMatrix()
	// print the present state of Matrix A to file out
	private static void printMatrix(int[][] A) {
		int n = A.length;
		int m = A[0].length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++)
				System.out.print(A[i][j] + "  ");
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}

	public static int[][] GaussElimination(int[][] A, int max_row, int max_column) {
		int row = 0;
		int column = 0;
		//int n = 9;
		//int m = 10;
		int temp_row;
		printMatrix(A);

		// perform Gauss-Jordan Elimination algorithm
		while (row < max_row && column < max_column) {

			// look for a non-zero entry in col j at or below row i
			temp_row = row;
			while (temp_row < max_row && A[temp_row][column] == 0)
				temp_row++;

			// if such an entry is found at row k
			if (temp_row < max_row) {

				// if k is not i, then swap row i with row k
				if (temp_row != row) {
					swap(A, row, temp_row, column);
					printMatrix(A);
				}

				// eliminate all other non-zero entries from col j by
				// subtracting from each
				// row (other than i) an appropriate multiple of row i
				eliminate(A, row, column);
				printMatrix(A);
				row++;
			}
			column++;
		}
		return A;

	}
}
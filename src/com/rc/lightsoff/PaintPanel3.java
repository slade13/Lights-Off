package com.rc.lightsoff;

import static com.rc.lightsoff.Utilities.GaussElimination;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PaintPanel3 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4619705722834246507L;
	private Point mouseLocation;
	Board3x3 desk = new Board3x3();
	int[][] board = desk.randBoard(3);
	// int[][] board = {{0,1,0},{1,1,0},{0,1,1}};
	int[] vector = new int[9];
	Rectangle[] rect = new Rectangle[9];
	Dimension wymiar = new Dimension(100, 100);
	Point punkt = new Point((getWidth() - wymiar.width * 3) / 2, 5);
	Point mouseSubtractor = new Point(0, 0);

	JButton testBoard;

	// additional variables
	int[][] arrayA = new int[9][9];
	int[] vectorB = new int[9];
	double[] vectorX = new double[9];
	double[][] solvedArray;
	int[] helpVector = new int[9];
	int n = 9;
	int m = 10;
	int[][] A = new int[n + 1][m + 1];
	
	String helpWasUsed = "";
	boolean helpStatus = false;

	public PaintPanel3() {
		BorderFactory.createLineBorder(Color.RED, 5);
		testBoard = new JButton("help");
		testBoard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				helpStatus = !helpStatus;
				if (helpStatus)
					testBoard.setBackground(Color.BLUE);
				else
					testBoard.setBackground(Color.GRAY);
				update();
			}
		});
		testBoard.setBounds(10, 327, 89, 23);
		add(testBoard);

		MouseAdapter listener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseLocation = new Point(e.getPoint().x - mouseSubtractor.x,
						e.getPoint().y - mouseSubtractor.y);
				/*
				 * System.out.println(e.getPoint().x + " " + e.getPoint().y);
				 * System.out.println(mouseSubtractor.x + " " +
				 * mouseSubtractor.y); System.out.println(mouseLocation.x + " "
				 * + mouseLocation.y);
				 */
				System.out.println(checkClick());
				if (checkClick() != -1) {
					board = lightsOff(checkClick());
					update();
				}
			}
		};
		addMouseListener(listener);
	}

	@Override
	protected void paintComponent(Graphics g) {
		paintAll(g);
		checkIfWon(g, desk.isSolved(board));
	}

	private int checkClick() {
		for (int i = 0; i < 9; i++) {
			if (rect[i].x < mouseLocation.x
					&& rect[i].x + rect[i].width > mouseLocation.x
					&& rect[i].y < mouseLocation.y
					&& rect[i].y + rect[i].height > mouseLocation.y) {
				return i;
			}
		}
		return -1;
	}

	private int[][] lightsOff(int clicked) {
		int[][] temp = board;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (desk.boardCombination[clicked][i][j] == 1) {
					if (temp[i][j] == 0) {
						temp[i][j] = 1;
					} else {
						temp[i][j] = 0;
					}
				}
			}
		}
		return temp;
	}

	public int[] MatrixToVector(int matrix[][]) {
		int[] tempVector = new int[9];
		int index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				tempVector[index] = matrix[i][j];
				index++;
			}
		}
		return tempVector;
	}

	public double[][] VectorToTwoDim(double vector[]) {
		double[][] tempVector = new double[9][1];
		for (int i = 0; i < 9; i++) {
			tempVector[i][0] = vector[i];
		}
		return tempVector;
	}

	public void update() {
		vectorB = MatrixToVector(board);
		for (int i = 0; i < 9; i++) {
			arrayA[i] = MatrixToVector(desk.boardCombination[i]);
		}
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				if (j != 9) {
					A[i][j] = arrayA[i][j];
				} else {
					A[i][j] = vectorB[i];
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(A[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		A = GaussElimination(A, 9, 10);
		int helpIndex = 0;
		for (int i = 1; i <= 9; i++) {
			helpVector[helpIndex++] = A[i - 1][9];
			System.out.print(A[i - 1][9] + " ");
			if (i % 3 == 0)
				System.out.println();
		}
		repaint();
	}

	public void paintAll(Graphics g) {
		super.paintComponent(g);
		punkt = new Point((getWidth() - wymiar.width * 3) / 2, 5);
		int index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				rect[index] = new Rectangle(punkt, wymiar);
				vector[index] = board[i][j];
				punkt.x += wymiar.width;
				index++;
			}
			punkt.x -= 3 * wymiar.width;
			punkt.y += wymiar.height;
		}

		for (int i = 0; i < 9; i++) {
			g.setColor(Color.RED);
			g.drawRect(rect[i].x, rect[i].y, rect[i].width, rect[i].height);
			if (vector[i] == 0) {
				g.setColor(Color.BLACK);
				g.fillRect(rect[i].x, rect[i].y, rect[i].width, rect[i].height);
			}
			if ((helpVector[i] == 1) && helpStatus) {
				if (vector[i] == 0)
					g.setColor(Color.WHITE);
				else
					g.setColor(Color.BLACK);
				g.fillOval(rect[i].x + 35, rect[i].y + 35, rect[i].width - 70,
						rect[i].height - 70);

			}
		}
	}

	public void checkIfWon(Graphics g, boolean solved) {
		if (solved) {
			// board = desk.randBoard(3);
			helpWasUsed = helpStatus ? " But you was cheating!" : "";
			JOptionPane.showMessageDialog(Game.contentPane,
					"You won this game!" + helpWasUsed);
			helpStatus = false;
			helpWasUsed = "";
			board = desk.randBoard(3);
			paintAll(g);
		}
	}
}

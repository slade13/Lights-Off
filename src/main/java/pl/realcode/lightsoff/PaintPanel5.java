package pl.realcode.lightsoff;

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

public class PaintPanel5 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4619705722834246507L;
	private Point mouseLocation;
	Board5x5 desk = new Board5x5();
	int[][] board = desk.randBoard(5);
	// int[][] board = {{0,1,0},{1,1,0},{0,1,1}};
	int[] vector = new int[25];
	Rectangle[] rect = new Rectangle[25];
	Dimension wymiar = new Dimension(60, 60);
	Point punkt = new Point((getWidth() - wymiar.width * 5) / 2, 5);
	Point mouseSubtractor = new Point(0, 0);

	JButton testBoard;

	// additional variables
	int[][] arrayA = new int[25][25];
	int[] vectorB = new int[25];
	double[] vectorX = new double[25];
	double[][] solvedArray;
	int[] helpVector = new int[25];
	int n = 25;
	int m = 26;
	int[][] A = new int[n][m];
	
	String helpWasUsed = "";
	boolean helpStatus = false;

	public PaintPanel5() {
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
		for (int i = 0; i < 25; i++) {
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
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
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
	
	public void update() {
		vectorB = MatrixToVector(board);
		for (int i = 0; i < 25; i++) {
			arrayA[i] = MatrixToVector(desk.boardCombination[i]);
		}
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j <26; j++) {
				if (j != 25) {
					A[i][j] = arrayA[i][j];
				} else {
					A[i][j] = vectorB[i];
				}
			}
		}
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 26; j++) {
				System.out.print(A[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		A = Utilities.GaussElimination(A, 25, 26);
		int helpIndex = 0;
		for (int i = 1; i <= 25; i++) {
			helpVector[helpIndex++] = A[i - 1][25];
			System.out.print(A[i - 1][25] + " ");
			if (i % 5 == 0)
				System.out.println();
		}
		repaint();
	}
	
	public int[] MatrixToVector(int matrix[][]) {
		int[] tempVector = new int[25];
		int index = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				tempVector[index] = matrix[i][j];
				index++;
			}
		}
		return tempVector;
	}
	
	public void paintAll(Graphics g) {
		super.paintComponent(g);
		punkt = new Point((getWidth() - wymiar.width * 5) / 2, 5);
		int index = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				rect[index] = new Rectangle(punkt, wymiar);
				vector[index] = board[i][j];
				punkt.x += wymiar.width;
				index++;
			}
			punkt.x -= 5 * wymiar.width;
			punkt.y += wymiar.height;
		}

		for (int i = 0; i < 25; i++) {
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
				g.fillOval(rect[i].x + 20, rect[i].y + 20, rect[i].width - 40,
						rect[i].height - 40);
				
			}
			//System.out.println(helpVector[i] + " ");
		}
		//System.out.println();
	}

	public void checkIfWon(Graphics g, boolean solved) {
		if (solved) {
			// board = desk.randBoard(3);
			helpWasUsed = helpStatus ? " But you was cheating!" : "";
			JOptionPane.showMessageDialog(Game.contentPane,
					"You won this game!" + helpWasUsed);
			helpStatus = false;
			helpWasUsed = "";
			board = desk.randBoard(5);
			paintAll(g);
		}
	}
}

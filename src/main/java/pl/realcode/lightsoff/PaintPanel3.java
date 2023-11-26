package pl.realcode.lightsoff;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PaintPanel3 extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4619705722834246507L;
	private Point mouseLocation;
	int[][] board = Board3x3.randBoard(3);
	int[] vector = new int[9];
	Rectangle[] rect = new Rectangle[9];
	Dimension dimension = new Dimension(100, 100);
	Point point = new Point((getWidth() - dimension.width * 3) / 2, 5);
	Point mouseSubtract = new Point(0, 0);

	JButton helpJButton;

	// additional variables
	int[][] arrayA = new int[9][9];
	int[] vectorB = new int[9];
	int[] helpVector = new int[9];
	int n = 10;
	int m = 9;
	int[][] A = new int[m][n];

	String helpWasUsed = "";
	boolean helpStatus = false;

	public PaintPanel3() {
		helpJButton = new JButton("help");
		helpJButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				helpStatus = !helpStatus;
				update();
			}
		});
		helpJButton.setBounds(10, 327, 89, 23);
		add(helpJButton);

		MouseAdapter listener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseLocation = new Point(e.getPoint().x - mouseSubtract.x,
						e.getPoint().y - mouseSubtract.y);

				System.out.println(checkClick());
				if (checkClick() != -1) {
					board = lightsOff(checkClick());
					update();
					paintAll(getGraphics());
				}
			}
		};
		addMouseListener(listener);
	}

	@Override
	protected void paintComponent(Graphics g) {
		checkIfWon(BoardUtils.isSolved(board));
		paintAll(g);
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
				if (Board3x3.boardCombination[clicked][i][j] == 1) {
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
		vectorB = BoardUtils.matrixToVector(board);
		for (int i = 0; i < 9; i++) {
			arrayA[i] = BoardUtils.matrixToVector(Board3x3.boardCombination[i]);
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

		Utilities.printMatrix(A);

		Utilities.GaussElimination(A, 9, 10);
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
		point = new Point((getWidth() - dimension.width * 3) / 2, 5);
		int index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				rect[index] = new Rectangle(point, dimension);
				vector[index] = board[i][j];
				point.x += dimension.width;
				index++;
			}
			point.x -= 3 * dimension.width;
			point.y += dimension.height;
		}

		for (int i = 0; i < 9; i++) {

			if (vector[i] == 0) {
				g.setColor(Color.BLACK);
				g.fillRect(rect[i].x, rect[i].y, rect[i].width, rect[i].height);
			}
			if ((helpVector[i] == 1) && helpStatus) {
				if (vector[i] == 0) {
					g.setColor(Color.WHITE);
				}
				else {
					g.setColor(Color.BLACK);
					g.fillOval(rect[i].x + 35, rect[i].y + 35, rect[i].width - 70,
						rect[i].height - 70);
				}

			}
			g.setColor(Color.GRAY);
			g.drawRect(rect[i].x, rect[i].y, rect[i].width, rect[i].height);
		}
	}

	public void checkIfWon(boolean solved) {
		if (solved) {
			helpWasUsed = helpStatus ? " But you were cheating!" : "";
			JOptionPane.showMessageDialog(Game.contentPane,
					"You won this game!" + helpWasUsed);
			helpStatus = false;
			helpWasUsed = "";
			board = BoardUtils.randBoard(3);
		}
	}
}

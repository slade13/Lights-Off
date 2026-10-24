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
import javax.swing.SwingUtilities;

public class PaintPanel3 extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4619705722834246507L;
	public static final double HELP_CIRCLE_RATIO = 2.85;
	private Point mouseLocation;

	private static final int n = 10;
	private static final int m = 9;
	int[][] board = Board3x3.randBoard(3);
	int[] vector = new int[m];
	Rectangle[] rect = new Rectangle[m];
	Dimension squareSize = new Dimension(100, 100);
	Point point = new Point((getWidth() - squareSize.width * 3) / 2, 5);
	Point mouseSubtract = new Point(0, 0);

	JButton helpJButton;

	// additional variables
	int[][] arrayA = new int[m][m];
	int[] vectorB = new int[m];
	int[] helpVector = new int[m];

	int[][] A = new int[m][n];

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

					int clicked = checkClick();
					if (clicked != -1) {
						board = lightsOff(clicked);
						update();
						checkIfWon();
					}
			}
		};
		addMouseListener(listener);
	}

	@Override
	protected void paintComponent(Graphics g) {
		paintAll(g);
	}

	private int checkClick() {
		for (int i = 0; i < rect.length; i++) {
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
		int[] solution = BoardUtils.solve(board);
		helpVector = solution == null ? new int[m] : solution;
		repaint();
	}

	public void paintAll(Graphics g) {
		super.paintComponent(g);
		point = new Point((super.getWidth() - squareSize.width * 3) / 2, 5);
		int index = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				rect[index] = new Rectangle(point, squareSize);
				vector[index] = board[i][j];
				point.x += squareSize.width;
				index++;
			}
			point.x -= 3 * squareSize.width;
			point.y += squareSize.height;
		}

		for (int i = 0; i < m; i++) {

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
				}
				g.fillOval(
					(int) (rect[i].x + rect[i].width/HELP_CIRCLE_RATIO),
                    (int) (rect[i].y + rect[i].height/HELP_CIRCLE_RATIO),
                    (int) (rect[i].width/HELP_CIRCLE_RATIO),
                    (int) (rect[i].height/HELP_CIRCLE_RATIO));

			}
			g.setColor(Color.GRAY);
			g.drawRect(rect[i].x, rect[i].y, rect[i].width, rect[i].height);
		}
	}

	public void checkIfWon() {
		if (BoardUtils.isSolved(board)) {
			SwingUtilities.invokeLater(() -> {
				String message = "You won!";
				message += helpStatus ? " But you were cheating!" : "";
				JOptionPane.showMessageDialog(Game.contentPane, message);
				helpStatus = false;
				board = BoardUtils.randBoard(3);
				update();
			});
		}
	}
}

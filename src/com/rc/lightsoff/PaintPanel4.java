package com.rc.lightsoff;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PaintPanel4 extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4619705722834246507L;
	private Point mouseLocation;
	Board4x4 desk = new Board4x4();
	int[][] board = desk.randBoard(4);
	//int[][] board = {{0,1,0},{1,1,0},{0,1,1}};
	int[] vector = new int[16];
	Rectangle[] rect = new Rectangle[16];
	Dimension wymiar = new Dimension(75, 75);
	Point punkt = new Point((getWidth() - wymiar.width * 4) / 2, 5);
	Point mouseSubtractor = new Point(0, 0);

	public PaintPanel4() {
		BorderFactory.createLineBorder(Color.RED, 5);
		JButton testBoard = new JButton("TEST");
		testBoard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				repaint();
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
				if(checkClick() != -1) {
					board = lightsOff(checkClick());
					repaint();
				}
			}
		};
		addMouseListener(listener);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		punkt = new Point((getWidth() - wymiar.width * 4) / 2, 5);

		int index = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				rect[index] = new Rectangle(punkt, wymiar);
				vector[index] = board[i][j];
				punkt.x += wymiar.width;
				index++;
			}
			punkt.x -= 4 * wymiar.width;
			punkt.y += wymiar.height;
		}

		g.setColor(Color.BLACK);
		for (int i = 0; i < 16; i++) {
			g.drawRect(rect[i].x, rect[i].y, rect[i].width, rect[i].height);
			if (vector[i] == 0) {
				g.fillRect(rect[i].x, rect[i].y, rect[i].width, rect[i].height);
			}
		}
	}

	private int checkClick() {
		for (int i = 0; i < 16; i++) {
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
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
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
}

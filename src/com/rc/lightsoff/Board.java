package com.rc.lightsoff;

import java.util.Random;

public abstract class Board {
	
	public int[][] randBoard(int size) {
		Random rand = new Random();
		int[][] randomizedBoard = new int[size][size];
		do{
			for(int i = 0; i<size; i++) {
				for(int j = 0; j<size; j++) {
					randomizedBoard[i][j]=rand.nextInt(2);
				}
			}
		}while(isSolved(randomizedBoard));
		
		return randomizedBoard;
	}
	
	public boolean isSolved(int board[][]) {
		
		int size = board[0].length;
		
		int[][] solvedBoard = new int[size][size];
		for(int i = 0; i<size; i++) {
			for(int j = 0; j<size; j++) {
				solvedBoard[i][j]=0;
			}
		}
		
		for(int i = 0; i<size; i++) {
			for(int j = 0; j<size; j++) {
				if(solvedBoard[i][j]!=board[i][j]) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public String toString(int board[][]) {
		int size=board[0].length;
		String boardString="";
		
		for(int i = 0; i<size; i++) {
			for(int j = 0; j<size; j++) {
				boardString += board[i][j] + " ";
			}
			boardString += "\n";
		}
		return boardString;
	}
	
	public abstract int[] returnVector(int[][] board);
}

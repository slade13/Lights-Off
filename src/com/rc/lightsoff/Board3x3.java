package com.rc.lightsoff;

public class Board3x3 extends Board {
	int boardCombination[][][] =
		   {{{1,1,0},
			 {1,0,0},
			 {0,0,0}},
			 
			{{1,1,1},
			 {0,1,0},
			 {0,0,0}},
			 
			{{0,1,1},
			 {0,0,1},
			 {0,0,0}},
			 
			{{1,0,0},
			 {1,1,0},
			 {1,0,0}},
			 
			{{0,1,0},
			 {1,1,1},
			 {0,1,0}},
			 
			{{0,0,1},
			 {0,1,1},
			 {0,0,1}},
			 
			{{0,0,0},
			 {1,0,0},
			 {1,1,0}},
			 
			{{0,0,0},
			 {0,1,0},
			 {1,1,1}},
			 
			{{0,0,0},
			 {0,0,1},
			 {0,1,1}}};

	@Override
	public int[] returnVector(int[][] board) {
		// TODO Auto-generated method stub
		return null;
	}
}

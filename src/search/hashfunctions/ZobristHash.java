package search.hashfunctions;

import java.util.Random;

import board.position.Position12x10;
import board.position.PositionBB;

public class ZobristHash {
	
	// constant indices
    /*white_pawn := 1
    white_rook := 2
    // etc.
    black_king := 12*/
	public static final int WROOK = 1;
	public static final int WKNIGHT = 2;
	public static final int WBISHOP = 3;
	public static final int WQUEEN = 4;
	public static final int WKING = 5;
	public static final int WPAWN = 6;
	public static final int BROOK = 7;
	public static final int BKNIGHT = 8;
	public static final int BBISHOP = 9;
	public static final int BQUEEN = 10;
	public static final int BKING = 11;
	public static final int BPAWN = 12;
	//public static final int EMPTY = '.';
	//public static final int INVALID = -1;
	
	static int[][] table;
	static Random randomGenerator;
    
	public static void init() {
		/* fill a table of random numbers/bitstrings */
		randomGenerator = new Random();
		table = new int[64][12]; // a 2-d array of size 64×12
	       for (int i = 0; i < 64; i++) {  // loop over the board, represented as a linear array
	           for(int j = 0; j < 12; j++) {      // loop over the pieces
	               table[i][j] = random_bitstring();
	           }
	       }
	}
	
	private static int random_bitstring() {
		return randomGenerator.nextInt();
	}
	               
	public static int hash(Position12x10 pos) {
		int[] board = pos.get12x10Board();
	       int h = 0;
	       for(int i = 0; i < board.length; i++) { //    # loop over the board positions
	           if(board[i] != Position12x10.EMPTY) {
	        	   int j = 0;
	        	   switch(board[i]) {
	        	   case Position12x10.WPAWN:
	        		   j = 0;
	        		   break;
	        	   case Position12x10.WROOK:
	        		   j = 1;
	        		   break;
	        	   case Position12x10.WKNIGHT:
	        		   j = 2;
	        		   break;
	        	   case Position12x10.WBISHOP:
	        		   j = 3;
	        		   break;
	        	   case Position12x10.WQUEEN:
	        		   j = 4;
	        		   break;
	        	   case Position12x10.WKING:
	        		   j = 5;
	        		   break;
	        	   case Position12x10.BPAWN:
	        		   j = 6;
	        		   break;
	        	   case Position12x10.BROOK:
	        		   j = 7;
	        		   break;
	        	   case Position12x10.BKNIGHT:
	        		   j = 8;
	        		   break;
	        	   case Position12x10.BBISHOP:
	        		   j = 9;
	        		   break;
	        	   case Position12x10.BQUEEN:
	        		   j = 10;
	        		   break;
	        	   case Position12x10.BKING:
	        		   j = 11;
	        		   break;
	        	   }
	        	   h = h ^ table[i][j];
	               //j := the piece at board[i], as listed in the constant indices, above
	               //h := h XOR table[i][j];
	           }
	       }
	           return h;
	}
	
	public static int hash(PositionBB pos) {
		int[] board = pos.getSquares();
	       int h = 0;
	       for(int i = 0; i < board.length; i++) { //    # loop over the board positions
	           if(board[i] != PositionBB.EMPTY) {
	        	   int j = 0;
	        	   switch(board[i]) {
	        	   case PositionBB.WPAWNS:
	        		   j = 0;
	        		   break;
	        	   case PositionBB.WROOKS:
	        		   j = 1;
	        		   break;
	        	   case PositionBB.WKNIGHTS:
	        		   j = 2;
	        		   break;
	        	   case PositionBB.WBISHOPS:
	        		   j = 3;
	        		   break;
	        	   case PositionBB.WQUEENS:
	        		   j = 4;
	        		   break;
	        	   case PositionBB.WKING:
	        		   j = 5;
	        		   break;
	        	   case PositionBB.BPAWNS:
	        		   j = 6;
	        		   break;
	        	   case PositionBB.BROOKS:
	        		   j = 7;
	        		   break;
	        	   case PositionBB.BKNIGHTS:
	        		   j = 8;
	        		   break;
	        	   case PositionBB.BBISHOPS:
	        		   j = 9;
	        		   break;
	        	   case PositionBB.BQUEENS:
	        		   j = 10;
	        		   break;
	        	   case PositionBB.BKING:
	        		   j = 11;
	        		   break;
	        	   }
	        	   h = h ^ table[i][j];
	               //j := the piece at board[i], as listed in the constant indices, above
	               //h := h XOR table[i][j];
	           }
	       }
	           return h;
	}
}

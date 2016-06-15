package search.hashfunctions;

import java.util.Random;

import board.Position12x10;

public class ZobristHash {
	
	static long[][] table;
	static Random randomGenerator;
    
	public static void init() {
		/* fill a table of random numbers/bitstrings */
		randomGenerator = new Random();
		table = new long[64][12]; // a 2-d array of size 64×12
	       for (int i = 0; i < 64; i++) {  // loop over the board, represented as a linear array
	           for(int j = 0; j < 12; j++) {      // loop over the pieces
	               table[i][j] = random_bitstring();
	           }
	       }
	}
	
	private static long random_bitstring() {
		return randomGenerator.nextLong();
	}
	
	/** fast 8x8 to 12x10 Conversion-Table */
	public static int[] raw8x8To12x10 = {
			21, 22, 23, 24, 25, 26, 27, 28, 
			31, 32, 33, 34, 35, 36, 37, 38, 
			41, 42, 43, 44, 45, 46, 47, 48, 
			51, 52, 53, 54, 55, 56, 57, 58, 
			61, 62, 63, 64, 65, 66, 67, 68, 
			71, 72, 73, 74, 75, 76, 77, 78, 
			81, 82, 83, 84, 85, 86, 87, 88, 
			91, 92, 93, 94, 95, 96, 97, 98, 
			101, 102, 103, 104, 105, 106, 107, 108 
			};
	
	/* Get 64-Bit Hash-Key by 12x10-Position */
	public static long hash(Position12x10 pos) {
		int[] board = pos.get12x10Board();
	       long h = 0;
	       for(int i = 0; i < 64; i++) { //    # loop over the board positions, start with valid squares
	           if(board[raw8x8To12x10[i]] != Position12x10.EMPTY) {   // skip empty squares
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
	           }
	       }
	           return h;
	}
}

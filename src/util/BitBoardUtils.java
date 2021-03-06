package util;

import board.pieces.Piece;
import board.position.Position12x10;
import board.position.bitboard.PositionBB;

public class BitBoardUtils {
	
	/** Convert char rep to BBindex */
	public static int charPieceToPieceType(int p) {
		final int piece;

		switch (p) {
		case 'K':
			piece = Piece.WKING;
			break;
		case 'R':
			piece = Piece.WROOK;
			break;
		case 'N':
			piece = Piece.WKNIGHT;
			break;
		case 'B':
			piece = Piece.WBISHOP;
			break;
		case 'Q':
			piece = Piece.WQUEEN;
			break;
		case 'P':
			piece = Piece.WPAWN;
			break;
		case 'k':
			piece = Piece.BKING;
			break;
		case 'r':
			piece = Piece.BROOK;
			break;
		case 'n':
			piece = Piece.BKNIGHT;
			break;
		case 'b':
			piece = Piece.BBISHOP;
			break;
		case 'q':
			piece = Piece.BQUEEN;
			break;
		case 'p':
			piece = Piece.BPAWN;
			break;
		default:
			piece = Piece.EMPTY;
		}
		return piece;
	}
	
	/** Convert a 12x10 index to an 8x8 */
	public static int index12x10ToBBSquare(int index) {
        int[] coord = Position12x10.indexToCoord(index);
        //System.out.println("coord: " + coord[0] + " " + coord[1]);
        return PositionBB.getSquare(coord[0], coord[1]);
	}
	
	/** Convert an 8x8 index to a 12x10 */
	public static int squareTo12x10(int square) {
		int[] coord = {PositionBB.getX(square), PositionBB.getY(square)};
		int index = Position12x10.coordToIndex(coord);
		//System.out.print("square: " + square + " --> ");
        //System.out.println("index: " + index);
        return index;
	}
	
	public static String bitboardToString(long bitboard, char label) {
		char[] board = new char[64];
		
		/* Bit-Masks for detecting pieces on squares */
		long mask = 0b0000000000000000000000000000000000000000000000000000000000000001L;
		long[] masks = new long[64];
		for(int i = 0; i < 64; i++) {
			masks[i] = mask;
			mask = mask << 1;
		}
		
		for(int sq = 0; sq < 64; sq++) {
			if((bitboard & masks[sq]) == masks[sq]) {
				board[63 - sq] = label;
			} else board[63 - sq] = '.';
		}
		
		StringBuilder result = new StringBuilder();
		String boardstr = new String(board);
		
		/* Add newlines after each rank */
		for(int j = 0; j < 64; j += 8) {
		result.append(boardstr.substring(j, j+8));
		result.append(System.lineSeparator());
		}
		return result.toString();
	}
}

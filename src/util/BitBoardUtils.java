package util;

import board.position.Position12x10;
import board.position.PositionBB;

public class BitBoardUtils {
	
	/** Convert char rep to BBindex */
	public static int charPieceToPieceType(int p) {
		final int piece;

		switch (p) {
		case 'K':
			piece = PositionBB.WKING;
			break;
		case 'R':
			piece = PositionBB.WROOKS;
			break;
		case 'N':
			piece = PositionBB.WKNIGHTS;
			break;
		case 'B':
			piece = PositionBB.WBISHOPS;
			break;
		case 'Q':
			piece = PositionBB.WQUEENS;
			break;
		case 'P':
			piece = PositionBB.WPAWNS;
			break;
		case 'k':
			piece = PositionBB.BKING;
			break;
		case 'r':
			piece = PositionBB.BROOKS;
			break;
		case 'n':
			piece = PositionBB.BKNIGHTS;
			break;
		case 'b':
			piece = PositionBB.BBISHOPS;
			break;
		case 'q':
			piece = PositionBB.BQUEENS;
			break;
		case 'p':
			piece = PositionBB.BPAWNS;
			break;
		default:
			piece = PositionBB.EMPTY;
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
	
}

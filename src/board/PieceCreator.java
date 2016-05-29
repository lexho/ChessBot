package board;

import board.pieces.Bishop;
import board.pieces.BlackPawn;
import board.pieces.King;
import board.pieces.Knight;
import board.pieces.Piece;
import board.pieces.Queen;
import board.pieces.Rook;
import board.pieces.WhitePawn;

/** Create a Piece by representation */
public class PieceCreator {
	
	public static Piece createPiece(String rep, int[] coord) {
		return createPiece(rep.charAt(0), Position12x10.coordToIndex(coord));
	}
	
	public static Piece createPiece(char rep, int index) {
		if(rep == 'P') return new WhitePawn(rep, index);
		else if(rep == 'p') return new BlackPawn(rep, index);
		switch(Character.toLowerCase(rep)) {
		case 'r':
			return new Rook(rep, index);
		case 'n':
			return new Knight(rep, index);
		case 'b':
			return new Bishop(rep, index);
		case 'k':
			return new King(rep, index);
		case 'q':
			return new Queen(rep, index);
		default:
			return new Piece(rep, index);
		}
	}
}

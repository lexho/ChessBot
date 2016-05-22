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
		if(rep.charAt(0) == 'P') return new WhitePawn(rep, coord);
		else if(rep.charAt(0) == 'p') return new BlackPawn(rep, coord);
		switch(Character.toLowerCase(rep.charAt(0))) {
		case 'r':
			return new Rook(rep, coord);
		case 'n':
			return new Knight(rep, coord);
		case 'b':
			return new Bishop(rep, coord);
		case 'k':
			return new King(rep, coord);
		case 'q':
			return new Queen(rep, coord);
		default:
			return new Piece(rep, coord);
		}
	}
}

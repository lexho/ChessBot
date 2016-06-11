package board.pieces;

import board.position.Position12x10;

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

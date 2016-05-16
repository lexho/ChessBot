package board.square;

import board.pieces.Piece;

public interface Square {
	boolean isValid();
	boolean isFree();
	public Piece getPiece();
	void setPiece(Piece p);
	void clear();
}

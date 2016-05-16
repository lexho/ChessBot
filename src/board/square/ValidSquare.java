package board.square;

import board.pieces.Piece;

public class ValidSquare implements Square {
	Piece piece;

	/* Empty Square */
	public ValidSquare() {
		
	}
	
	/* Occupied Square */
	public ValidSquare(Piece piece) {
		this.piece = piece;
	}
	
	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public boolean isFree() {
		if(piece == null) return true;
		else return false;
	}
	
	public String toString() {
		if(piece == null) return ".";
		return piece.toString();
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public void setPiece(Piece p) {
		this.piece = p;
	}
	
	public void clear() {
		this.piece = null;
	}

}

package board.square;

import board.pieces.Piece;

public class InvalidSquare implements Square {
	
	public boolean isValid() {
		return false;
	}
	
	@Override
	public boolean isFree() {
		return false;
	}
	
	public Piece getPiece() {
		return new Piece(" ", new int[]{-1,-1});
	}
	
	public void setPiece(Piece p) {
		
	}
	
	public void clear() {
		
	}
}

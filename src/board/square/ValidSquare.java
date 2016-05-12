package board.square;

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
	public boolean isFree() {
		if(piece == null) return true;
		else return false;
	}
	
	public String toString() {
		if(piece == null) return "X";
		return piece.toString();
	}

}

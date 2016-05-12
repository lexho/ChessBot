package board.square;

public class InvalidSquare implements Square {
	
	@Override
	public boolean isFree() {
		return false;
	}
}

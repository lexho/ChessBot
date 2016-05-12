package board;

public class Board {
	private Position currentPosition;
	private MoveValidator moveValidator;
	
	/* Create a Board by Position */
	public Board(Position position) {
		this.currentPosition = position;
	}
	
	/* Executes valid moves */
	public boolean makeMove(Move m) {
		boolean moveIsValid = moveValidator.validate(currentPosition, m);
		if(moveIsValid) return executeMove(m);
		return false;
	}
	
	/* Executes move m on the current board */
	private boolean executeMove(Move m) {
		return false;
	}
	
}

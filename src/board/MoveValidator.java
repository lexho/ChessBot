package board;

import board.actions.Action;
import board.pieces.Piece;
import board.square.Square;

public class MoveValidator {
	public static boolean validate(Position position, Move m) {	
		
		/* Where are we coming from? */
		Square start = position.getSquareAt(m.getSource());
		
		/* Where are we going to? */
		Square target = position.getSquareAt(m.getTarget());
		
		/* Compare the move to the moves the piece is able to make */
		//TODO validate castle
		Piece piece = start.getPiece();
		boolean valid = false;
		for(Action action : piece.getActions()) {
			int[] trg = action.apply(m.getSource());
			
			/* Action target is the same as move target */
			if(trg[0] == m.getTarget()[0] && trg[1] == m.getTarget()[1]) {
				boolean isAllowed = action.validate(m.getSource(), position);
				if(isAllowed) { 
					//System.out.println(piece + " " + m + " is valid");
					valid = true; 
					break; 
				}
				//System.out.println(allowed.toString() + " vs. " + m);
			}
		}
		
		if(!valid) {
			return false;
		}
		
		/*Action action = m.getAction();
		action.setTakes(!target.isFree());
		System.out.println(action);
		
		if(!action.validate(m.getSource(), position)) {
			System.err.println("invalid action");
			return false;
		}*/
		
		/* It's an invalid square */
		if(!position.getSquareAt(m.getSource()).isValid()) return false;
		
		/* Source is not our piece */
		if(position.getActiveColor() != start.getPiece().getColor()) return false; 
		
		return true;
	}
	
	public static boolean validateSquare(int[] coord) {
		int x = coord[0];
		int y = coord[1];
		if(x > 7 || y > 7 || x < 0 || y < 0) 
			return false;
		else
			return true;
	}
}

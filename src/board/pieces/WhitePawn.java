package board.pieces;

import board.Position12x10;
import board.actions.Action;

public class WhitePawn extends Pawn {
	
	public WhitePawn(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public WhitePawn(char rep, int index) {
		super(rep, index);
		ID = 3;
		actions.add(new Action(Action.UP, false));
		actions.add(new Action(Action.UP_LEFT, true));
		actions.add(new Action(Action.UP_RIGHT, true));
		
		/*actions.add(new Action(0, 1, false)); // allow move forward 		
		actions.add(new Action(1, 1, true)); // allow move forward and take on the right
		actions.add(new Action(-1, 1, true)); // allow move forward and take on the left
		*/
	}

}

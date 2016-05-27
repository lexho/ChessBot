package board.pieces;

import board.Position12x10;
import board.actions.Action;

public class BlackPawn extends Pawn {

	public BlackPawn(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public BlackPawn(char rep, int index) {
		super(rep, index);
		ID = 4;
		actions.add(new Action(Action.DOWN, false));
		actions.add(new Action(Action.DOWN_LEFT, true));
		actions.add(new Action(Action.DOWN_RIGHT, true));
		/*actions.add(new Action(0, -1, false)); // allow move forward 
		actions.add(new Action(1, -1, true)); // allow move forward and take on the right
		actions.add(new Action(-1, -1, true)); // allow move forward and take on the left
		*/
	}

}

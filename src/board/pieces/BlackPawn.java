package board.pieces;

import board.actions.Action;
import board.actions.PawnTakeAction;
import board.position.Position12x10;

public class BlackPawn extends Pawn {
	
	public BlackPawn(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public BlackPawn(char rep, int index) {
		super(rep, index);

		actions.add(new Action(Action.DOWN, false));
		actions.add(new PawnTakeAction(Action.DOWN_LEFT));
		actions.add(new PawnTakeAction(Action.DOWN_RIGHT));
		/*actions.add(new Action(0, -1, false)); // allow move forward 
		actions.add(new Action(1, -1, true)); // allow move forward and take on the right
		actions.add(new Action(-1, -1, true)); // allow move forward and take on the left
		*/
	}

}

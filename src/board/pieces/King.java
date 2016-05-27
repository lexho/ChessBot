package board.pieces;

import board.Position12x10;
import board.actions.Action;

public class King extends Piece {
	
	public King(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public King(char rep, int index) {
		super(rep, index);
		ID = 1;

		actions.add(new Action(Action.UP, false));
		actions.add(new Action(Action.DOWN, false));
		actions.add(new Action(Action.LEFT, false));
		actions.add(new Action(Action.RIGHT, false));
		
		actions.add(new Action(Action.UP, true));
		actions.add(new Action(Action.DOWN, true));
		actions.add(new Action(Action.LEFT, true));
		actions.add(new Action(Action.RIGHT, true));
		
		actions.add(new Action(Action.DOWN_LEFT, false));
		actions.add(new Action(Action.DOWN_RIGHT, false));
		actions.add(new Action(Action.UP_LEFT, false));
		actions.add(new Action(Action.UP_RIGHT, false));
		
		actions.add(new Action(Action.DOWN_LEFT, true));
		actions.add(new Action(Action.DOWN_RIGHT, true));
		actions.add(new Action(Action.UP_LEFT, true));
		actions.add(new Action(Action.UP_RIGHT, true));
		
		//TODO some actions are still missing
		
		/*actions.add(new Action(0, 1, false)); // forward 	
		actions.add(new Action(0, -1, false)); // backward
		actions.add(new Action(-1, 0, false)); // left 	
		actions.add(new Action(1, 0, false)); // right 
		
		/* move diagonally (without taking) 
		actions.add(new Action(1, 1, false));
		actions.add(new Action(-1, 1, false));
		actions.add(new Action(1, -1, false));
		actions.add(new Action(-1, -1, false));
		
		actions.add(new Action(0, 1, true)); // forward 	
		actions.add(new Action(0, -1, true)); // backward
		actions.add(new Action(-1, 0, true)); // left 	
		actions.add(new Action(1, 0, true)); // right 
		
		/* move diagonally and take 
		actions.add(new Action(1, 1, true));
		actions.add(new Action(-1, 1, true));
		actions.add(new Action(1, -1, true));
		actions.add(new Action(-1, -1, true)); */
	}

}

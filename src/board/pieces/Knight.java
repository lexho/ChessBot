package board.pieces;

import board.actions.Action;
import board.position.Position12x10;

public class Knight extends Piece {
	
	public Knight(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public Knight(char rep, int index) {
		super(rep, index);
		
		actions.add(new Action(-21, false));
		actions.add(new Action(21, false));
		actions.add(new Action(-19, false));
		actions.add(new Action(+19, false));
		actions.add(new Action(-12, false));
		actions.add(new Action(12, false));
		actions.add(new Action(-8, false));
		actions.add(new Action(8, false));
		
		
		actions.add(new Action(-21, true));
		actions.add(new Action(21, true));
		actions.add(new Action(-19, true));
		actions.add(new Action(+19, true));
		actions.add(new Action(-12, true));
		actions.add(new Action(12, true));
		actions.add(new Action(-8, true));
		actions.add(new Action(8, true));
		
		/* without taking 
		actions.add(new Action(1, 2, false)); // one step to the right and two steps forward
		actions.add(new Action(-1, 2, false)); // one step to the left and two steps forward
		actions.add(new Action(1, -2, false)); // ...
		actions.add(new Action(-1, -2, false));
		
		actions.add(new Action(2, 1, false)); // two steps to the right and one step forward
		actions.add(new Action(2, -1, false)); // two steps to the left and one step forward
		actions.add(new Action(-2, 1, false)); // ...
		actions.add(new Action(-2, -1, false));
		
		/* with taking 
		actions.add(new Action(1, 2, true)); // one step to the right and two steps forward
		actions.add(new Action(-1, 2, true)); // one step to the left and two steps forward
		actions.add(new Action(1, -2, true)); // ...
		actions.add(new Action(-1, -2, true));
		
		actions.add(new Action(2, 1, true)); // two steps to the right and one step forward
		actions.add(new Action(2, -1, true)); // two steps to the left and one step forward
		actions.add(new Action(-2, 1, true)); // ...
		actions.add(new Action(-2, -1, true)); */
		
		for(Action action : actions) {
			action.setBlockable(false);
		}

	}

	@Override
	public int getID() {
		return KNIGHT;
	}
	
	@Override
	public int getValue() {
		return KNIGHT_V;
	}
}

package board.pieces;

import board.actions.Action;

public class Knight extends Piece {
	
	public Knight(String rep, int[] coord) {
		super(rep, coord);
		actions.add(new Action(1, 2, true)); // one step to the right and two steps forward
		actions.add(new Action(-1, 2, true)); // one step to the left and two steps forward
		actions.add(new Action(1, -2, true)); // ...
		actions.add(new Action(-1, -2, true));
		
		actions.add(new Action(2, 1, true)); // two steps to the right and one step forward
		actions.add(new Action(2, -1, true)); // two steps to the left and one step forward
		actions.add(new Action(-2, 1, true)); // ...
		actions.add(new Action(-2, -1, true));
		for(Action action : actions) {
			action.setBlockable(false);
		}
		ID = 5;
	}

}

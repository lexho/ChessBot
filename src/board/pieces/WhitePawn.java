package board.pieces;

import board.actions.Action;

public class WhitePawn extends Pawn {
	
	public WhitePawn(String rep, int[] coord) {
		super(rep, coord);
		ID = 3;
		actions.add(new Action(0, 1, false)); // allow move forward 		
		actions.add(new Action(1, 1, true)); // allow move forward and take on the right
		actions.add(new Action(-1, 1, true)); // allow move forward and take on the left
	}

}

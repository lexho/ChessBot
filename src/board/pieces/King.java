package board.pieces;

import board.actions.Action;

public class King extends Piece {
	
	public King(String rep, int[] coord) {
		super(rep, coord);
		ID = 1;

		actions.add(new Action(0, 1, false)); // forward 	
		actions.add(new Action(0, -1, false)); // backward
		actions.add(new Action(-1, 0, false)); // left 	
		actions.add(new Action(1, 0, false)); // right 
		
		/* move diagonally (without taking) */
		actions.add(new Action(1, 1, false));
		actions.add(new Action(-1, 1, false));
		actions.add(new Action(1, -1, false));
		actions.add(new Action(-1, -1, false));
		
		actions.add(new Action(0, 1, true)); // forward 	
		actions.add(new Action(0, -1, true)); // backward
		actions.add(new Action(-1, 0, true)); // left 	
		actions.add(new Action(1, 0, true)); // right 
		
		/* move diagonally and take */
		actions.add(new Action(1, 1, true));
		actions.add(new Action(-1, 1, true));
		actions.add(new Action(1, -1, true));
		actions.add(new Action(-1, -1, true));
	}

}

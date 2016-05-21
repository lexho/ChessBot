package board.pieces;

import board.actions.Action;

public class Bishop extends Piece {
	
	public Bishop(String rep, int[] coord) {
		super(rep, coord);
		ID = 6;
		
		/* move diagonally (without taking) */
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(1 * d, 1 * d, false));
			actions.add(new Action(-1 * d, 1 * d, false));
			actions.add(new Action(1 * d, -1 * d, false));
			actions.add(new Action(-1 * d, -1 * d, false));
		}
		
		/* move diagonally and take */
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(1 * d, 1 * d, true));
			actions.add(new Action(-1 * d, 1 * d, true));
			actions.add(new Action(1 * d, -1 * d, true));
			actions.add(new Action(-1 * d, -1 * d, true));
		}
	}

}

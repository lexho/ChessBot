package board.pieces;

import board.actions.Action;

public class Queen extends Piece {

	public Queen(String rep, int[] coord) {
		super(rep, coord);
		ID = 2;
		
		/* Bishop moves */
		/* move diagonally (without taking) */
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(1 * d, 1 * d, false));
			actions.add(new Action(-1 * d, 1 * d, false));
			actions.add(new Action(1 * d, -1 * d, false));
			actions.add(new Action(1 * d, -1 * d, false));
		}
		
		/* move diagonally and take */
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(1 * d, 1 * d, true));
			actions.add(new Action(-1 * d, 1 * d, true));
			actions.add(new Action(1 * d, -1 * d, true));
			actions.add(new Action(1 * d, -1 * d, true));
		}
		
		/* Rook moves */
		/* move forward (without taking) */
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(0 * d, 1 * d, false));
			actions.add(new Action(-1 * d, 0 * d, false));
			actions.add(new Action(0 * d, -1 * d, false));
			actions.add(new Action(1 * d, 0 * d, false));
		}
		
		/* move forward and take */
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(0 * d, 1 * d, true));
			actions.add(new Action(-1 * d, 0 * d, true));
			actions.add(new Action(0 * d, -1 * d, true));
			actions.add(new Action(1 * d, 0 * d, true));
		}
		actions.clear(); // TODO remove that line
	}

}

package board.pieces;

import board.Position12x10;
import board.actions.Action;

public class Queen extends Piece {

	public Queen(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public Queen(char rep, int index) {
		super(rep, index);
		ID = 2;
		
		/* Bishop moves */
		actions.add(new Action(Action.DOWN_LEFT, false));
		actions.add(new Action(Action.DOWN_RIGHT, false));
		actions.add(new Action(Action.UP_LEFT, false));
		actions.add(new Action(Action.UP_RIGHT, false));
		
		actions.add(new Action(Action.DOWN_LEFT, true));
		actions.add(new Action(Action.DOWN_RIGHT, true));
		actions.add(new Action(Action.UP_LEFT, true));
		actions.add(new Action(Action.UP_RIGHT, true));
		
		//TODO some actions are still missing
		/* move diagonally (without taking) 
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(1 * d, 1 * d, false));
			actions.add(new Action(-1 * d, 1 * d, false));
			actions.add(new Action(1 * d, -1 * d, false));
			actions.add(new Action(1 * d, -1 * d, false));
		}
		
		/* move diagonally and take 
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(1 * d, 1 * d, true));
			actions.add(new Action(-1 * d, 1 * d, true));
			actions.add(new Action(1 * d, -1 * d, true));
			actions.add(new Action(1 * d, -1 * d, true));
		} */
		
		/* Rook moves */
		actions.add(new Action(Action.UP, false));
		actions.add(new Action(Action.DOWN, false));
		actions.add(new Action(Action.LEFT, false));
		actions.add(new Action(Action.RIGHT, false));
		
		actions.add(new Action(Action.UP, true));
		actions.add(new Action(Action.DOWN, true));
		actions.add(new Action(Action.LEFT, true));
		actions.add(new Action(Action.RIGHT, true));
		
		//TODO some actions are still missing
		/* move forward (without taking) 
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(0 * d, 1 * d, false));
			actions.add(new Action(-1 * d, 0 * d, false));
			actions.add(new Action(0 * d, -1 * d, false));
			actions.add(new Action(1 * d, 0 * d, false));
		}
		
		/* move forward and take 
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(0 * d, 1 * d, true));
			actions.add(new Action(-1 * d, 0 * d, true));
			actions.add(new Action(0 * d, -1 * d, true));
			actions.add(new Action(1 * d, 0 * d, true));
		}*/
	}

}

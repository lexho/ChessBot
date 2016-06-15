package board.pieces;

import board.actions.Action;
import board.position.Position12x10;

public class Bishop extends Piece {
	
	public Bishop(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public Bishop(char rep, int index) {
		super(rep, index);
		
		actions.add(new Action(Action.DOWN_LEFT, true, false));
		actions.add(new Action(Action.DOWN_RIGHT, true, false));
		actions.add(new Action(Action.UP_LEFT, true, false));
		actions.add(new Action(Action.UP_RIGHT, true, false));
		
		actions.add(new Action(Action.DOWN_LEFT, true, true));
		actions.add(new Action(Action.DOWN_RIGHT, true, true));
		actions.add(new Action(Action.UP_LEFT, true, true));
		actions.add(new Action(Action.UP_RIGHT, true, true));
		
		//TODO some actions are still missing
		
		/* move diagonally (without taking) 
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(1 * d, 1 * d, false));
			actions.add(new Action(-1 * d, 1 * d, false));
			actions.add(new Action(1 * d, -1 * d, false));
			actions.add(new Action(-1 * d, -1 * d, false));
		}*/
		
		/* move diagonally and take 
		for(int d = 0; d < 8; d++) {
			actions.add(new Action(1 * d, 1 * d, true));
			actions.add(new Action(-1 * d, 1 * d, true));
			actions.add(new Action(1 * d, -1 * d, true));
			actions.add(new Action(-1 * d, -1 * d, true));
		}*/
	}

	@Override
	public int getValue() {
		return BISHOP_V;
	}
}

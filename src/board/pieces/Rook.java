package board.pieces;

import board.Position12x10;
import board.actions.Action;

public class Rook extends Piece {

	public Rook(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public Rook(char rep, int index) {
		super(rep, index);
		
		actions.add(new Action(Action.UP, true, false));
		actions.add(new Action(Action.DOWN, true, false));
		actions.add(new Action(Action.LEFT, true, false));
		actions.add(new Action(Action.RIGHT, true, false));
		
		actions.add(new Action(Action.UP, true, true));
		actions.add(new Action(Action.DOWN, true, true));
		actions.add(new Action(Action.LEFT, true, true));
		actions.add(new Action(Action.RIGHT, true, true));
		
		//TODO some actions are still missing
		
		/* move forward (without taking) 
		for(int d = 1; d < 8; d++) {
			actions.add(new Action(0 * d, 1 * d, false));
			actions.add(new Action(-1 * d, 0 * d, false));
			actions.add(new Action(0 * d, -1 * d, false));
			actions.add(new Action(1 * d, 0 * d, false));
		}
		
		/* move forward and take 
		for(int d = 1; d < 8; d++) {
			actions.add(new Action(0 * d, 1 * d, true));
			actions.add(new Action(-1 * d, 0 * d, true));
			actions.add(new Action(0 * d, -1 * d, true));
			actions.add(new Action(1 * d, 0 * d, true));
		}*/
	}

	@Override
	public int getID() {
		return ROOK;
	}
	
	@Override
	public int getValue() {
		return ROOK_V;
	}
}

package board.pieces;

import board.Position12x10;
import board.actions.Action;

public class Bishop extends Piece {
	
	public Bishop(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public Bishop(char rep, int index) {
		super(rep, index);
		ID = 6;
		
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

}

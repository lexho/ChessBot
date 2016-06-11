package board.pieces;

import board.actions.Action;
import board.position.Position12x10;

public class Queen extends Piece {

	public Queen(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public Queen(char rep, int index) {
		super(rep, index);
		
		/* Bishop moves */
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
	
	@Override
	public int getID() {
		return QUEEN;
	}
	
	@Override
	public int getValue() {
		return QUEEN_V;
	}

}

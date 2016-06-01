package board.pieces;

import java.util.ArrayList;
import java.util.List;

import board.Move;
import board.Position12x10;
import board.actions.Action;

public class King extends Piece {
	
	public King(char rep, int[] coord) {
		this(rep, Position12x10.coordToIndex(coord));
	}
	
	public King(char rep, int index) {
		super(rep, index);
		ID = 1;

		actions.add(new Action(Action.UP, false));
		actions.add(new Action(Action.DOWN, false));
		actions.add(new Action(Action.LEFT, false));
		actions.add(new Action(Action.RIGHT, false));
		
		actions.add(new Action(Action.UP, true));
		actions.add(new Action(Action.DOWN, true));
		actions.add(new Action(Action.LEFT, true));
		actions.add(new Action(Action.RIGHT, true));
		
		actions.add(new Action(Action.DOWN_LEFT, false));
		actions.add(new Action(Action.DOWN_RIGHT, false));
		actions.add(new Action(Action.UP_LEFT, false));
		actions.add(new Action(Action.UP_RIGHT, false));
		
		actions.add(new Action(Action.DOWN_LEFT, true));
		actions.add(new Action(Action.DOWN_RIGHT, true));
		actions.add(new Action(Action.UP_LEFT, true));
		actions.add(new Action(Action.UP_RIGHT, true));
	}
	
	@Override
	public List<Move> getPossibleMoves(Position12x10 pos) {
		List<Move> moves = super.getPossibleMoves(pos);
		
		/* Castling Moves */
		//TODO test if king is in check 
		boolean[] castling = pos.getCastling();
		//System.out.println("king " + this.getPosIndex() + "color: " + getColor());
		if(getColor() == 'w' && this.getPosIndex() == 95) {
			if(castling[0] && pos.isFree(94) && pos.isFree(93) && pos.isFree(92)) moves.add(new Move(95, 93));
			if(castling[1] && pos.isFree(96) && pos.isFree(97)) moves.add(new Move(95, 97));
		} else if(getPosIndex() == 25) {
			if(castling[2] && pos.isFree(24) && pos.isFree(23) && pos.isFree(22)) moves.add(new Move(25, 23));
			if(castling[3] && pos.isFree(26) && pos.isFree(27)) moves.add(new Move(25, 27));
		}
		return moves;
	}

	
}

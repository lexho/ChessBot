package board.pieces;

import java.util.ArrayList;
import java.util.List;

import board.Move;
import board.MoveValidator;
import board.Position12x10;
import board.actions.Action;

abstract class Pawn extends Piece {

	public Pawn(char rep, int index) {
		super(rep, index);
	}
	
	public Pawn(char rep, int[] coord) {
		super(rep, coord);
	}
	
	public List<Move> getPossibleMoves(Position12x10 pos) {
		List<Move> moves = new ArrayList<Move>();
		
		// TODO switch to index-based system (not coordinate-based)
		coord = Position12x10.indexToCoord(index);
		
		// two-square initial move rule
		if(getColor() == 'w' )
			if(index > 80 && index < 89 && pos.isFree(index - 10) && pos.isFree(index - 20)) 
				moves.add(new Move(index,  index + 2*Action.UP));
		else 
			if(index > 20 && index < 29 && pos.isFree(index + 10) && pos.isFree(index + 20)) 
				moves.add(new Move(index,  index + 2*Action.DOWN));
		
		for(Action action : actions) {
			List<Integer> targets = action.apply(pos, index);
			for(Integer target : targets) {
				
				/* Validate target position */
				boolean isValid = MoveValidator.validateSquare(target);
	
				if(isValid) moves.add(new Move(index, target));
			}
		}
		//System.out.println(moves);
		return moves;
	}

}

package board.pieces;

import java.util.ArrayList;
import java.util.List;

import board.Move;
import board.MoveValidator;
import board.actions.Action;

abstract class Pawn extends Piece {

	public Pawn(String rep, int[] coord) {
		super(rep, coord);
	}
	
	public List<Move> getPossibleMoves() {
		List<Move> moves = new ArrayList<Move>();
		
		// two-square initial move rule
		if(coord[1] == 1) {
			if(getColor() == 'w') {
				//System.out.println("add move " + new Move(coord,  new int[]{coord[0], coord[1] + 2}));
				moves.add(new Move(coord,  new int[]{coord[0], coord[1] + 2}));
			} else
				moves.add(new Move(coord,  new int[]{coord[0], coord[1] - 2}));
		}
		
		for(Action action : actions) {
			int[] target = action.apply(coord);
			
			/* Validate target position */
			boolean isValid = MoveValidator.validateSquare(target);

			if(isValid) moves.add(new Move(coord, action.apply(coord)));
		}
		//System.out.println(moves);
		return moves;
	}

}

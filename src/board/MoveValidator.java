package board;

import java.util.List;

import board.actions.Action;
import board.pieces.Piece;
import board.square.Square;

public class MoveValidator {
	public static boolean validate(PositionInterface position, Move m) {	
		
		/* Where are we coming from? */
		//Square start = position.getSquareAt(m.getSource());
		
		/* Where are we going to? */
		//Square target = position.getSquareAt(m.getTarget());
		
		/* Compare the move to the moves the piece is able to make */
		Piece piece = position.getPieceAt(m.getSource()) ;
		boolean valid = false;
		for(Action action : piece.getActions()) {
			int[] trg = action.apply(m.getSource());
			
			/* Action target is the same as move target */
			if(trg[0] == m.getTarget()[0] && trg[1] == m.getTarget()[1]) {
				boolean isAllowed = action.validate(m.getSource(), position);
				if(isAllowed) { 
					//System.out.println(piece + " " + m + " is valid");
					valid = true; 
					break; 
				}
				//System.out.println(allowed.toString() + " vs. " + m);
			}
		}
		
		//TODO maybe this rule should be implemented in action context
		// Pawn: two-square initial move rule
		if(piece.getID() == Piece.WHITE_PAWN && m.getSource()[1] == 1 && m.getTarget()[1] == 3) {
			// don't jump over other pieces
			if(position.isFree(new int[]{ m.getSource()[0], 2 }) &&
			   position.isFree(new int[]{ m.getSource()[0], 3 })) {
				return true;
			}
		} else if(piece.getID() == Piece.BLACK_PAWN && m.getSource()[1] == 6 && m.getTarget()[1] == 4) {
			// don't jump over other pieces
			if(position.isFree(new int[]{ m.getSource()[0], 5 }) &&
			   position.isFree(new int[]{ m.getSource()[0], 4 })) {
				return true;
			}
		}
		
		if(!valid) {
			//System.out.println(piece + " " + m + " is invalid");
			return false;
		}
		
		/*Action action = m.getAction();
		action.setTakes(!target.isFree());
		System.out.println(action);
		
		if(!action.validate(m.getSource(), position)) {
			System.err.println("invalid action");
			return false;
		}*/
		
		/* It's an invalid square */
		if(!position.isValid(m.getSource())) return false;
		
		/* Source is not our piece */
		if(position.getActiveColor() != piece.getColor()) return false; 
		
		return true;
	}
	
	public static boolean validate(Position12x10 position, Move m) {
		
		boolean validSquares = position.isValid(m.getSourceIndex()) && position.isValid(m.getTargetIndex());
		
		Piece piece = position.getPieceAt(m.getSourceIndex());
		
		/* Source is not our piece */
		if(position.getActiveColor() != piece.getColor()) {
			//System.out.println("source is not our piece");
			return false;
		}
		
		/* Validate Castling */
		boolean[] castling = position.getCastling();
		/* Castle Black */
		if(m.getSourceIndex() == 25) {
			if((m.getTargetIndex() == 23 && castling[3]) || (m.getTargetIndex() == 27 && castling[2])) {
				return true;
			}
		}
		/* Castle White */
		else if(m.getSourceIndex() == 95) {
			if((m.getTargetIndex() == 93 && castling[1] && position.isFree(94) && position.isFree(93) && position.isFree(92)) || (m.getTargetIndex() == 97 && castling[0] && position.isFree(96) && position.isFree(97))) {
				return true;
			}
		}
		
		/* It's an invalid square */
		if(!position.isValid(m.getSourceIndex())) {
			//System.out.println("it's an invalid square");
			return false;
		}
		
		boolean valid = false;
		//System.out.println("test actions " + piece.getCharRep());
		for(Action action : piece.getActions()) {
			List<Integer> trgs = action.apply(position, m.getSourceIndex());
			//System.out.println("targets: " + trgs.size());
			for(Integer trg : trgs) {
				//System.out.println(new Move(m.getSourceIndex(), trg).toString() + " == " + m.getTargetIndex());
				/* Action target is the same as move target */
				if(trg == m.getTargetIndex()) {
					boolean isAllowed = action.validate(m.getSourceIndex(), (Position12x10)position);
					//System.out.print(piece);
					if(isAllowed) { 
						//System.out.println(" " + m + " is valid");
						valid = true; 
						break; 
					} //else System.out.println(" " + m + " is invalid");
					//System.out.println(allowed.toString() + " vs. " + m);
				}
			}
		}
		
		//TODO maybe this rule should be implemented in action context
		//TODO inefficient (?)
		// Pawn: two-square initial move rule
		if(piece.getID() == Piece.WHITE_PAWN && m.getSource()[1] == 1 && m.getTarget()[1] == 3) {
			// don't jump over other pieces
			if(position.isFree(new int[]{ m.getSource()[0], 2 }) &&
			   position.isFree(new int[]{ m.getSource()[0], 3 })) {
				return true;
			}
		} else if(piece.getID() == Piece.BLACK_PAWN && m.getSource()[1] == 6 && m.getTarget()[1] == 4) {
			// don't jump over other pieces
			if(position.isFree(new int[]{ m.getSource()[0], 5 }) &&
			   position.isFree(new int[]{ m.getSource()[0], 4 })) {
				return true;
			}
		}
		
		//if(!valid) return false;
		return valid;
		//if(!position.isValid(m.getSourceIndex())) System.out.println("source " + m.getSourceIndex() + " is not valid");
		//if(!position.isValid(m.getTargetIndex())) System.out.println("target " + m.getTargetIndex() + " is not valid");
		//return position.isValid(m.getSourceIndex()) && position.isValid(m.getTargetIndex());
	}
	
	public static boolean validateSquare(int index) {
		int [] org = {
				Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.BROOK,	Position12x10.BKNIGHT,	Position12x10.BBISHOP,	Position12x10.BQUEEN,	Position12x10.BKING, 	Position12x10.BBISHOP,	Position12x10.BKNIGHT,	Position12x10.BROOK,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.BPAWN,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.EMPTY,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.WPAWN,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.WROOK,	Position12x10.WKNIGHT,	Position12x10.WBISHOP,	Position12x10.WQUEEN,	Position12x10.WKING,	Position12x10.WBISHOP, 	Position12x10.WKNIGHT,	Position12x10.WROOK,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,
				Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID,	Position12x10.INVALID };
		return !(org[index] == Position12x10.INVALID);
	}
	
	public static boolean validateSquare(int[] coord) {
		int x = coord[0];
		int y = coord[1];
		if(x > 7 || y > 7 || x < 0 || y < 0) 
			return false;
		else
			return true;
	}
}

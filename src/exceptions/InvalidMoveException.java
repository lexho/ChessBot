package exceptions;

import board.Board;
import board.Move;
import board.Position;
import board.PositionInterface;
import board.pieces.Piece;

public class InvalidMoveException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String msg;
	
	public InvalidMoveException() {
		
	}
	
	public InvalidMoveException(String s) {
		super(s);
	}
	
	public InvalidMoveException(Move m, PositionInterface pos) {
		Piece p = pos.getPieces().getPieceAt(m.getSource());
		msg = "\n" + pos.toString();
		msg += "The piece " + p + " at (" + m.getSource()[0] + "/" + m.getSource()[1] + ") is not allowed to make the move " + m;
		//printErrorMessage(m, pos);
	}
	
	public InvalidMoveException(String s, Move m, Position pos) {
		super(s + " " + m);
		//printErrorMessage(m, pos);
	}
	
	@Override
	public String getMessage() {
		return this.getClass().getName().toString() + ": " + msg;
	}
	
	private void printErrorMessage(Move m, Position pos) {
		System.err.println("invalid move " + m);
		System.err.println(pos);
		Piece piece = pos.getPieces().getPieceAt(m.getSource());
		String p_src = pos.getPieces().getPieceAt(m.getSource()).toString();
		String p_trg = pos.getSquareAt(m.getTarget()).toString();
		System.err.println("Possible moves piece: " + piece.getPossibleMoves());
		System.err.print("source: " + p_src + " ("+ m.getSource()[0] + "/" + m.getSource()[1] + "), ");
		System.err.println("target: " + p_trg + " ("+ m.getTarget()[0] + "/" + m.getTarget()[1] + ")");
		
		for(Piece p : pos.getPieces()) {
			System.err.print(p.getRep() + " (" + p.getPosition()[0] + "/"+ p.getPosition()[1] + "), ");
		}
		System.err.println();
		
		System.err.println();
	}

}

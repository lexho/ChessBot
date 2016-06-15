package board;

import java.util.List;

import board.pieces.Piece;
import board.position.Position12x10;
import board.position.PositionBB;
import util.BitBoardUtils;

public class Move {
	protected String command;
	int promPiece = Piece.EMPTY;
	
	protected int i8x8_src;
	protected int i8x8_trg;
	public Move (String cmd) {
		this.command = cmd;
		
		/* Convert ascii to int source */
		int x = (int) command.charAt(0) - 'a';
		int y = (int) command.charAt(1) - '0' - 1;
		int[] src = new int[2];
		src[0] = x;
		src[1] = y;
		
		/* Convert ascii to int target */
		x = (int) command.charAt(2) - 'a';
		y = (int) command.charAt(3) - '0' - 1;
		int[] trg = new int[2];
		trg[0] = x;
		trg[1] = y;
		
		/* Create 8x8 board index */
		this.i8x8_src = PositionBB.getSquare(src[0], src[1]);
		this.i8x8_trg = PositionBB.getSquare(trg[0], trg[1]);
		
	}
	
	/**
	 * create a new move by source and target coordinates of a 8x8 board
	 * @param src the source coordinates
	 * @param trg the target coordinates
	 * */
	public Move(int[]src, int[] trg) {
		this.i8x8_src = PositionBB.getSquare(src[0], src[1]);
		this.i8x8_trg = PositionBB.getSquare(trg[0], trg[1]);
	}
	
	/** Create a Move by source and target indexes of board 
	 * @param src board index of move's source
	 * @param trg board index of move'S target
	 * @param promPiece the piece to promote to
	 * */
	public Move(int src8x8, int trg8x8, int promPiece) {
		createMove(src8x8, trg8x8, 1);
		this.promPiece = promPiece;
	}
	
	public Move(int src8x8, int trg8x8) {
		createMove(src8x8, trg8x8, 1);
		
	}
	
	public Move(Move m) {
		createMove(m.getSource8x8Index(), m.getSource8x8Index(), 1);
	}
	
	private void createMove(int src, int trg, int type) {
		switch(type) {
		case 1: // create by 8x8 board indices
			this.i8x8_src = src;
			this.i8x8_trg = trg;
			
			break;
		default: // create by 12x10 board indices
			
			/* Create 8x8 board index */
			this.i8x8_src = BitBoardUtils.index12x10ToBBSquare(src);
			this.i8x8_trg = BitBoardUtils.index12x10ToBBSquare(trg);
		}
	}
	
	/**
	 * get the source coordinates of the move
	 * @return the source coordinates
	 */
	public int[] getSource() {
		return new int[] {PositionBB.getX(i8x8_src), PositionBB.getY(i8x8_src)};
	}
	
	/**
	 * get the target coordinates of the move
	 * @return the target coordinates
	 */
	public int[] getTarget() {
		return new int[] {PositionBB.getX(i8x8_trg), PositionBB.getY(i8x8_trg)};
	}
	
	/**
	 * get the source index of the move
	 * @return the source index
	 */
	public int getSourceIndex() {
		return BitBoardUtils.squareTo12x10(i8x8_src);
	}
	
	/**
	 * get the target index of the move
	 * @return the target index
	 */
	public int getTargetIndex() {
		return BitBoardUtils.squareTo12x10(i8x8_trg);
	}
	
	/**
	 * get the source index of the move
	 * @return the source index
	 */
	public int getSource8x8Index() {
		return i8x8_src;
	}
	
	public int getTarget8x8Index() {
		return i8x8_trg;
	}
	
	public int getPromoteTo() {
		return promPiece;
	}
	
	/**
	 * @return the move in long algebraic notation
	 */
	public String toString() {
		if(command != null) return command;
		/* create long algebraic move string */
		int[] src = new int[] {PositionBB.getX(i8x8_src), PositionBB.getY(i8x8_src)};
		int[] trg = new int[] {PositionBB.getX(i8x8_trg), PositionBB.getY(i8x8_trg)};
		char alpha = (char) ((char) src[0] + 'a');
		char num = (char) ((char) src[1] + '0' + 1);
		char alpha1 = (char) ((char) trg[0] + 'a');
		char num1 = (char) ((char) trg[1] + '0' + 1);
		command = Character.toString(alpha) + Character.toString(num) + Character.toString(alpha1) + Character.toString(num1);
		return command;
	}
	
	/** Does the moveList contain the Move move? 
	 * @return contains move */
	public static boolean contains(List<Move> moveList, Move move) {
		for(Move m : moveList) {
			if(m.toString().equals(move.toString())) {
				return true;
			}
		}
		return false;
	}
}

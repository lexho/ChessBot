package board.position;

import java.util.List;

import board.Move;
import board.position.bitboard.PositionBB;

public interface Position {
	boolean whiteMove();
	char getActiveColor();
	void setActiveColor(char color);
	public Position copy();
	List<Move> getPossibleMoves();
	boolean makeMove(Move m);
	PositionBB getPositionBB();
	boolean isRunning();
	boolean isInCheck(boolean isWhite);
	public int getMoveNr();
	public int getPieceAt(int src);
	public int[] getSquares();
}

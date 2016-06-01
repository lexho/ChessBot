package board;

import board.pieces.Piece;
import board.pieces.PieceList;
import board.square.Square;

public interface PositionInterface {
	//Square getSquareAt(int x, int y);
	//Square getSquareAt(int[] coord);
	char getActiveColor();
	void switchActiveColor();
	void setActiveColor(char color);
	char getUnactiveColor();
	boolean[] getCastling();
	void disableCastling(int index);
	PieceList getPieces();
	Piece getPieceAt(int x, int y);
	Piece getPieceAt(int[] coord);
	void updatePieceList();
	void setPieceAt(Piece p, int[] coord);
	void clear(int[] coord);
	boolean isFree(int[] coord);
	boolean isValid(int[] coord);
	int getMoveNr();
	void increaseMoveNr();
	boolean isInCheck();
	boolean isInCheck(char color);
	/** Prints a list of all pieces and their location (for Debugging) */
	void printPieceLocationList();
	String toString();
	
}

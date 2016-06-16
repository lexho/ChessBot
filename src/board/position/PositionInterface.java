package board.position;

import board.pieces.Piece;
import board.pieces.PieceList;

public interface PositionInterface {
	//Square getSquareAt(int x, int y);
	//Square getSquareAt(int[] coord);
	char getActiveColor();
	void switchActiveColor();
	void setActiveColor(char color);
	boolean whiteMove();
	char getUnactiveColor();
	boolean[] getCastling();
	void disableCastling(int index);
	PieceList getPieces();
	Piece getPieceAt(int x, int y);
	Piece getPieceAt(int[] coord);
	int getPieceAt(int index);
	void updatePieceList();
	void setPieceAt(Piece p, int[] coord);
	void clear(int[] coord);
	boolean isFree(int[] coord);
	boolean isValid(int[] coord);
	int getMoveNr();
	void increaseMoveNr();
	boolean isInCheck();
	boolean isInCheck(char color);
	String toString();
	
}

package board;

import java.util.ArrayList;
import java.util.List;

import board.pieces.Piece;
import exceptions.InvalidMoveException;

public class Board {
	Position currentPosition;
	PieceList pieces;
	char color; // our color (white or black)
	
	/* Create a Board by Position */
	public Board(Position position) {
		this.currentPosition = position;
		pieces = position.getPieces();
	}
	
	/* Create a Board by another Board */
	public Board(Board b) {
		this.currentPosition = new Position(b.getPosition());
		//pieces = new PieceList<Piece>();
		this.pieces = currentPosition.getPieces();
		/*for(Piece p : b.getPosition().getPieces()) {
			pieces.add(p);
		}*/
	}
	
	/* Executes valid moves */
	public boolean makeMove(Move m) {
		// TODO remove the next line
		//if(m.getSource()[0] == 7 && m.getSource()[1] == 0 && m.getTarget()[0] == 7 && m.getTarget()[1] == 5) throw new InvalidMoveException(m, currentPosition);
		boolean moveIsValid = MoveValidator.validate(currentPosition, m);
		if(!moveIsValid) throw new InvalidMoveException(m, currentPosition);
		if(moveIsValid) return executeMove(m);

		System.err.println(m.getSource()[0]+ " "+m.getSource()[1]);
		for(Piece p : currentPosition.getPieces()) {
			System.err.println(p.getRepresentation() + " " + p.getPosition()[0] + " "+ p.getPosition()[1]);
		}
		System.err.println(currentPosition.getPieces().getPieceAt(m.getSource()).toString());
		return false;
	}
	
	/* Executes move m on the current board */
	private boolean executeMove(Move m) {
		//System.out.println("executing move: " + m.toString());
		int[] src = m.getSource();
		int[] trg = m.getTarget();
		
		/* Move piece to target */
		Piece piece = currentPosition.getSquareAt(src[0], src[1]).getPiece();
		pieces.removePieceAt(trg);	// remove token piece
		currentPosition.getSquareAt(trg[0], trg[1]).setPiece(piece); // update position table
		piece.setPosition(trg[0], trg[1]); // update piece
		
		/* Clear source */
		currentPosition.getSquareAt(src[0], src[1]).clear();
		
		/* Change Meta-Data */
		currentPosition.switchActiveColor();
		currentPosition.increaseMoveNr();
		
		//System.out.println("active color " + currentPosition.getActiveColor());
		//System.out.println("new moveNr " + currentPosition.getMoveNr());
		return false;
	}
	
	public boolean isRunning() {
		return true;
		/*if(getPossibleMoves().size() == 0) return false;
		else return true;*/
	}
	
	public Position getPosition() {
		return currentPosition;
	}
	
	public char getActiveColor() {
		return currentPosition.getActiveColor();
	}
	
	public int getMoveNr() {
		return currentPosition.getMoveNr();
	}
	
	public List<Move> getPossibleMoves() {
		boolean inCheck = currentPosition.isInCheck();
		
		List<Move> possibleMoves = new ArrayList<Move>();
		
		/*if(inCheck) {
			System.out.println("Check!");
			Piece king = pieces.getByID(Piece.KING);
			
			// get all moves where the result is not a check
			Board testBoard = new Board(this.copy());
			
			for(Piece piece : pieces) {
				for(Move m : piece.getPossibleMoves()) {
					if(MoveValidator.validate(currentPosition, m)) {
						testBoard.makeMove(m);
						if(!testBoard.getPosition().isInCheck(testBoard.getPosition().getUnactiveColor())) {
							possibleMoves.add(m);
						}
					}
				}
			}
			
			/* king's escape moves */
			/*for(Move m : king.getPossibleMoves()) {
				Board testBoard = this.copy();
				testBoard.makeMove(m);
				if(!testBoard.currentPosition.isInCheck()) {
					possibleMoves.add(m); // with this move we can safe the king
				}
			}*/
			// TODO restore king's safety, clear threat
			
		/*} else {*/
			for(Piece piece : pieces) {
				for(Move m : piece.getPossibleMoves()) {
					boolean moveIsValid = false;
					moveIsValid = MoveValidator.validate(currentPosition, m);
					if(moveIsValid) {
						Board testBoard = new Board(this.copy());
						testBoard.makeMove(m);
						if(!testBoard.getPosition().isInCheck(testBoard.getPosition().getUnactiveColor())) {
							possibleMoves.add(m);
						}
					}
				}
			}
		//}
		return possibleMoves;
	}
	
	public void setColor(char color) {
		this.color = color;
	}
	
	public Board copy() {
		return new Board(this);
	}
	
	public void print() {
		System.out.println(this.toString());
	}
	
	public String toString() {
		String piecesstr = pieces.size() + " Pieces on the board: \n" + pieces.toString() + "\n";
		String moves = new String();
		int NrOfMoves = 0;
		for(Piece piece : pieces) {
			NrOfMoves += piece.getPossibleMoves().size();
			for(Move m : piece.getPossibleMoves()) {
				boolean moveIsValid = false;
				moveIsValid = MoveValidator.validate(currentPosition, m);
				if(moveIsValid)
					moves += m + ", ";
			}
		}
		
		//String outstr = piecesstr + "\n" + NrOfMoves + " possible Moves " + moves + "\n\n" + currentPosition.toString();
		String outstr = currentPosition.toString();
		return outstr;
	}
}

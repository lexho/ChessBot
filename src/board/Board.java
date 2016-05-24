package board;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import board.pieces.Piece;
import board.pieces.Queen;
import exceptions.InvalidMoveException;
import search.endconditions.EndCondition;

public class Board {
	Position currentPosition;
	PieceList pieces;
	char color; // our color (white or black)
	
	/**
	 *  Create a Board by Position 
	 *  
	 *  @param position the position that should be on the board
	 *  */
	public Board(Position position) {
		this.currentPosition = position;
		pieces = position.getPieces();
	}
	
	/** Create a Board by another Board (clone)
	 * 
	 * @param b the blueprint board for the new board
	 * */
	public Board(Board b) {
		this.currentPosition = new Position(b.getPosition());
		//pieces = new PieceList<Piece>();
		this.pieces = currentPosition.getPieces();
		/*for(Piece p : b.getPosition().getPieces()) {
			pieces.add(p);
		}*/
		this.color = b.getColor();
	}
	
	/** Executes valid moves 
	 * 
	 * @param m the move that should be executed
	 * */
	public boolean makeMove(Move m) {

		boolean moveIsValid = MoveValidator.validate(currentPosition, m);
		if(!moveIsValid) throw new InvalidMoveException(m, currentPosition);
		if(moveIsValid) return executeMove(m);

		System.err.println(m.getSource()[0]+ " "+m.getSource()[1]);
		for(Piece p : currentPosition.getPieces()) {
			System.err.println(p.getRep() + " " + p.getPosition()[0] + " "+ p.getPosition()[1]);
		}
		System.err.println(currentPosition.getPieces().getPieceAt(m.getSource()).toString());
		return false;
	}
	
	/**
	 *  Executes move m on the current board 
	 *  
	 *  @param m the move that will be executed
	 *  */
	public boolean executeMove(Move m) {
		//TODO detect and handle castle (e8g8 --> e8g8 and h8f8)
		//System.out.println("executing move: " + m.toString());
		int[] src = m.getSource();
		int[] trg = m.getTarget();
		
		/* Move piece to target */
		Piece piece = currentPosition.getSquareAt(src[0], src[1]).getPiece();
		pieces.removePieceAt(trg);	// remove token piece

		/* Promotion (P -> Q,N,R,B on 8th rank)*/
		if(piece.getID() == Piece.WHITE_PAWN && trg[1] == 7) {
			pieces.removePieceAt(src);
			pieces.add(new Queen("Q", new int[]{trg[0],trg[1]}));
			piece = pieces.getPieceAt(trg);
		}
		else if(piece.getID() == Piece.BLACK_PAWN && trg[1] == 0) {
			pieces.removePieceAt(src);
			pieces.add(new Queen("q", new int[]{trg[0],trg[1]}));
			piece = pieces.getPieceAt(trg);
		}
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
		//System.out.println("Board is running");
		return !isMate();
		/*if(getPossibleMoves().size() == 0) return false;
		else return true;*/
	}
	
	public boolean isMate() {
		Piece king = currentPosition.getPieces().getByID(Piece.KING, getActiveColor());
		if(king == null) return true; //TODO handle no king error
		//System.out.println(king.getPossibleMoves());
		/* Are we in check? */
		if(!currentPosition.isInCheck()) return false;
		
		/* Is the king able to escape? */
		for(Move m : king.getPossibleMoves()) {
			//System.out.println(m);
			if(MoveValidator.validate(currentPosition, m)) {
				//System.out.println("is not mate");
				return true;
			}
			/*if(!temp.getPosition().isInCheck(king.getColor())) {
				System.out.println("is not mate");
				return false;
			}*/
		}
		//System.out.println("is mate");
		return true;
	}
	
	/**
	 * 
	 * @return the current position
	 */
	public Position getPosition() {
		return currentPosition;
	}
	
	public char getActiveColor() {
		return currentPosition.getActiveColor();
	}
	
	/** Get our color 
	 * @return our color
	 * */
	public char getColor() {
		return color;
	}
	
	/** Get opponents color 
	 * @return opponents color
	 * */
	public char getOpponentsColor() {
		if(color == 'w') return 'b';
		else if(color == 'b') return 'w';
		else return 'x'; //TODO invalid color (throw Exception?)
	}
	/**
	 * Get the current move number
	 * @return the current move number
	 */
	public int getMoveNr() {
		return currentPosition.getMoveNr();
	}
	
	//TODO this is not nice
	static Board b; // current instance
	
	/**
	 * Get a list of all possible moves in the current position
	 * @return a list of all possible moves in the current position
	 */
	public List<Move> getPossibleMoves() {
		List<Move> possibleMoves = new ArrayList<Move>();
		
		 int threads = Runtime.getRuntime().availableProcessors();
		    ExecutorService service = Executors.newFixedThreadPool(threads);
		    b = this;
		    
		    List<Future<List<Move>>> futures = new ArrayList<Future<List<Move>>>();
		    for (final Piece piece : pieces) {
		        Callable<List<Move>> callable = new Callable<List<Move>>() {
		            public List<Move> call() throws Exception {
		            	List<Move> possibleMoves = new ArrayList<Move>();
		            	for(Move m : piece.getPossibleMoves()) {
		    				if(MoveValidator.validate(currentPosition, m)) {
		    					Board testBoard = new Board(Board.b.copy());
		    					testBoard.makeMove(m);
		    					//char opponent = testBoard.getPosition().getUnactiveColor();
		    					if(!testBoard.getPosition().isInCheck(color)) {
		    						possibleMoves.add(m);
		    					}
		    				}
		    			}
		            	return possibleMoves;
		            }
		        };
		        futures.add(service.submit(callable));
		    }

		    service.shutdown();

		    List<Move> outputs = new ArrayList<Move>();
		    for (Future<List<Move>> future : futures) {
		        try {
					outputs.addAll(future.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    return outputs;
	    
		/*for(Piece piece : pieces) {
			for(Move m : piece.getPossibleMoves()) {
				if(MoveValidator.validate(currentPosition, m)) {
					Board testBoard = new Board(this.copy());
					testBoard.makeMove(m);
					//char opponent = testBoard.getPosition().getUnactiveColor();
					if(!testBoard.getPosition().isInCheck(color)) {
						possibleMoves.add(m);
					}
				}
			}
		}
		return possibleMoves;
		*/
	}
	
	public void setColor(char color) {
		this.color = color;
	}
	
	// TODO implement EndCondition
	public EndCondition getEndCondition() {
		return null;
	}
	/**
	 * 
	 * @return a new Board which is a clone of the current Board
	 */
	public Board copy() {
		return new Board(this);
	}
	
	/**
	 * print the current board to the standard output
	 */
	public void print() {
		System.out.println(this.toString());
	}
	
	/**
	 * create a human-readable representation of the current board and
	 * some additional information about the pieces and possible moves
	 * 
	 * @return the current board representation
	 */
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

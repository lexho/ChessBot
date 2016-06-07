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
import board.pieces.PieceList;
import board.pieces.Queen;
import engine.ChessBot;
import search.endconditions.EndCondition;

public class Board {
	public static boolean DEBUG = false;
	PositionInterface currentPosition;
	//PieceList pieces;
	char color; // our color (white or black) TODO this is probably buggy
	
	/**
	 *  Create a Board by Position 
	 *  
	 *  @param position the position that should be on the board
	 *  */
	public Board(PositionInterface position) {
		this.currentPosition = position;
		//pieces = position.getPieces();
	}
	
	/** Create a Board by another Board (clone)
	 * 
	 * @param b the blueprint board for the new board
	 * */
	public Board(Board b) {
		this.currentPosition = new Position12x10((Position12x10)b.getPosition());
		//pieces = new PieceList<Piece>();
		//this.pieces = currentPosition.getPieces();
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

		//boolean moveIsValid = MoveValidator.validate((Position12x10)currentPosition, m);
		//if(!moveIsValid) throw new InvalidMoveException(m, currentPosition);
		return executeMove(m);

		/*System.err.println(m.getSource()[0]+ " "+m.getSource()[1]);
		for(Piece p : currentPosition.getPieces()) {
			System.err.println(p.getRep() + " " + p.getPosition()[0] + " "+ p.getPosition()[1]);
		}
		System.err.println(currentPosition.getPieces().getPieceAt(m.getSource()).toString());
		return false;*/
	}
	
	/**
	 *  Executes move m on the current board 
	 *  
	 *  @param m the move that will be executed
	 *  */
	public boolean executeMove(Move m) {
		int src = m.getSourceIndex();
		int trg = m.getTargetIndex();
		
		Position12x10 currentPosition = (Position12x10)this.currentPosition;
		
		/* Move piece to target */
		int piece = currentPosition.get12x10Board()[src];
		currentPosition.clear(trg);
		
		boolean[] castling = currentPosition.getCastling();
		
		/* Handle castling move */
		if(m.getSourceIndex() == 25 || m.getSourceIndex() == 95) {
			/* Castling Black */
			if(piece == 'k') {
				/* Queen-side castling */
				if(castling[3] && m.getTargetIndex() == 23) {
					currentPosition.setPieceAt('r', 24);
					currentPosition.clear(21);
				}
				/* King-side castling */
				if(castling[2] && m.getTargetIndex() == 27) {
					currentPosition.setPieceAt('r', 26);
					currentPosition.clear(28);
				}	
			} 
			/* Castling White */
			else if(piece == 'K') {
				/* Queen-side castling */
				if(castling[1] && m.getTargetIndex() == 93) {
					currentPosition.setPieceAt('r', 94);
					currentPosition.clear(91);
				}
				/* King-side castling */
				if(castling[0] && m.getTargetIndex() == 97) {
					currentPosition.setPieceAt('r', 96);
					currentPosition.clear(98);
				}		
			}
		}
		
		/* Disable castling on king and/or rook moves */
		switch(piece) {
		case 'K':
			currentPosition.disableCastling(0);
			currentPosition.disableCastling(1);
			break;
		case 'R':
			if(src == 91)
				currentPosition.disableCastling(1);
			else if(src == 98)
				currentPosition.disableCastling(0);
			break;
		case 'k':
			currentPosition.disableCastling(2);
			currentPosition.disableCastling(3);
			break;
		case 'r':
			if(src == 21)
				currentPosition.disableCastling(3);
			else if(src == 28)
				currentPosition.disableCastling(2);
			break;
		}
		
		/* Promotion (P -> Q,N,R,B on 8th rank)*/
		if(piece == 'P' && trg > 20 && trg < 29) {
			/*System.out.println("promotion white");
			print();*/
			currentPosition.clear(src);
			currentPosition.setPieceAt((int)'Q', trg);
			currentPosition.updatePieceList();
			piece = 'Q';
			//print();
			
		}
		else if(piece == 'p' && trg > 90 && trg < 99) {
			/*System.out.println("promotion black");
			print();*/
			currentPosition.clear(src);
			currentPosition.setPieceAt((int)'q', trg);
			currentPosition.updatePieceList();
			piece = 'q';
			//print();
		}
		currentPosition.setPieceAt(piece, trg); // update position table
		
		/* Clear source */
		currentPosition.clear(src);
		currentPosition.updatePieceList();
		
		/* Change Meta-Data */
		currentPosition.switchActiveColor();
		currentPosition.increaseMoveNr();
		
		return false;
	}
	
	public boolean isRunning() {
		 for (Piece piece : currentPosition.getPieces()) {
			 if(piece.isMoveable((Position12x10)currentPosition)) return true;
		 }
		 return false;
		//System.out.println("is running");
		//return !isMate();
		/*if(getPossibleMoves().size() == 0) return false;
		else return true;*/
	}
	
	public boolean isMate() {
		Piece king = currentPosition.getPieces().getKing(getActiveColor());
		if(king == null) return true; //TODO handle no king error
		//System.out.println(king.getPossibleMoves());
		/* Are we in check? */
		if(!currentPosition.isInCheck()) return false;
		
		ExecutorService service = Executors.newFixedThreadPool(ChessBot.NR_OF_THREADS);
	    b = this;
	    
	    for (final Move m : king.getPossibleMoves((Position12x10)currentPosition)) {
	        Callable<Boolean> callable = new Callable<Boolean>() {
	            public Boolean call() throws Exception {
	            	if(MoveValidator.validate(currentPosition, m)) {
	            		System.out.println("is certainly not mate");
	    				return false; // is not mate for sure
	    			} else
	    				return true; // is probably mate
	            }
	        };
	        try {
				if(!service.submit(callable).get()) return false;
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    service.shutdown();
	    return true;
	    
		/* Is the king able to escape? */
		/*for(Move m : king.getPossibleMoves()) {
			//System.out.println(m);
			if(MoveValidator.validate(currentPosition, m)) {
				//System.out.println("is not mate");
				return false;
			}
		}
		//System.out.println("is mate");
		return true;*/
	}
	
	/**
	 * 
	 * @return the current position
	 */
	public PositionInterface getPosition() {
		return currentPosition;
	}
	
	/**
	 * 
	 * @return the current position (as 12x10 board)
	 */
	public Position12x10 getPosition12x10() {
		return (Position12x10) currentPosition;
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
	
	/** ExecutorService for multi-threading move generator in getPossibleMoves() */
	private ExecutorService service;
	
	/** Stop all running processes */
	public void stop() {
		service.shutdown(); // stop ExecutorService
	}
	
	/**
	 * Get a list of all possible moves in the current position
	 * @return a list of all possible moves in the current position
	 */
	public List<Move> getPossibleMoves() {
		char color = getActiveColor();
		List<Move> possibleMoves = new ArrayList<Move>();
		if(DEBUG) System.out.println("Possible Moves");
		service = Executors.newFixedThreadPool(ChessBot.NR_OF_THREADS);
		b = this;
	    
	    List<Future<List<Move>>> futures = new ArrayList<Future<List<Move>>>();
	    
	    for (final Piece piece : currentPosition.getPieces().getPieces(color)) {
	        Callable<List<Move>> callable = new Callable<List<Move>>() {
	        	
	            public List<Move> call() throws Exception {
	            	List<Move> possibleMoves = new ArrayList<Move>();
	            	if(DEBUG) System.out.print(piece + " ");
	            	for(Move m : piece.getPossibleMoves((Position12x10)currentPosition)) {
	            		if(DEBUG) System.out.print(m);
	            		
	            		//TODO is validation necessary here?
	    				//if(MoveValidator.validate((Position12x10)currentPosition, m)) {
	    					Position12x10 test = new Position12x10((Position12x10)currentPosition);
	    					test.movePiece(m.getSourceIndex(), m.getTargetIndex());
	    					test.setActiveColor(currentPosition.getUnactiveColor());
	    					
	    					if(!test.isInCheck(color)) {
	    						if(m != null)
	    							possibleMoves.add(m);
	    					}
	    					if(DEBUG) System.out.print("(+) ");
	    				/*} else {
	    					if(DEBUG)  System.out.print("(-) ");
	    					throw new InvalidMoveException(m, currentPosition);
	    				}*/
	    			}
	            	if(DEBUG) System.out.println();
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
				//e.printStackTrace();
				/* Interrupt search and return the moves found yet */
				service.shutdown();
				return outputs;
				// TODO Auto-generated catch block
				//e.printStackTrace();
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
		PieceList pieces = currentPosition.getPieces();
		String piecesstr = pieces.size() + " Pieces on the board: \n" + pieces.toString() + "\n";
		String moves = new String();
		int NrOfMoves = 0;
		for(Piece piece : pieces) {
			NrOfMoves += piece.getPossibleMoves((Position12x10)currentPosition).size();
			for(Move m : piece.getPossibleMoves((Position12x10)currentPosition)) {
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

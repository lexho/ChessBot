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
import exceptions.InvalidMoveException;
import search.endconditions.EndCondition;

public class Board {
	static boolean DEBUG = false;
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

		boolean moveIsValid = MoveValidator.validate((Position12x10)currentPosition, m);
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
		Piece piece = currentPosition.getPieceAt(src[0], src[1]);
		//pieces.removePieceAt(trg);	// remove token piece
		currentPosition.clear(trg);

		/* Promotion (P -> Q,N,R,B on 8th rank)*/
		if(piece.getID() == Piece.WHITE_PAWN && trg[1] == 7) {
			System.out.println("promotion white");
			print();
			//pieces.removePieceAt(src);
			currentPosition.clear(src);
			currentPosition.setPieceAt(new Queen('Q', new int[]{trg[0],trg[1]}), trg);
			//pieces.add(new Queen("Q", new int[]{trg[0],trg[1]}));
			piece = currentPosition.getPieceAt(trg);
			currentPosition.clear(trg);
			print();
			
		}
		else if(piece.getID() == Piece.BLACK_PAWN && trg[1] == 0) {
			System.out.println("promotion black");
			print();
			currentPosition.clear(src);
			currentPosition.setPieceAt(new Queen('q', new int[]{trg[0],trg[1]}), trg);
			piece = currentPosition.getPieceAt(trg);
			currentPosition.clear(trg);
			print();
		}
		currentPosition.setPieceAt(piece, trg); // update position table
		//piece.setPosition(trg); // update piece
		
		/* Clear source */
		currentPosition.clear(src);
		currentPosition.updatePieceList();
		
		/* Change Meta-Data */
		currentPosition.switchActiveColor();
		currentPosition.increaseMoveNr();
		
		//System.out.println("active color " + currentPosition.getActiveColor());
		//System.out.println("new moveNr " + currentPosition.getMoveNr());
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
		char color = getActiveColor();
		List<Move> possibleMoves = new ArrayList<Move>();
		if(DEBUG) System.out.println("Possible Moves");
		    ExecutorService service = Executors.newFixedThreadPool(ChessBot.NR_OF_THREADS);
		    b = this;
		    
		    List<Future<List<Move>>> futures = new ArrayList<Future<List<Move>>>();
		    
		    for (final Piece piece : currentPosition.getPieces()) {
		        Callable<List<Move>> callable = new Callable<List<Move>>() {
		        	
		            public List<Move> call() throws Exception {
		            	List<Move> possibleMoves = new ArrayList<Move>();
		            	if(DEBUG) System.out.print(piece + " ");
		            	for(Move m : piece.getPossibleMoves((Position12x10)currentPosition)) {
		            		if(DEBUG) System.out.print(m);
		            		
		    				if(MoveValidator.validate((Position12x10)currentPosition, m)) {
		    					Position12x10 test = new Position12x10((Position12x10)currentPosition);
		    					test.setPieceAt(piece.getCharRep(), m.getTargetIndex());
		    					test.clear(m.getTargetIndex());
		    					test.setActiveColor(currentPosition.getUnactiveColor());
		    					
		    		
		    					if(!test.isInCheck(color)) {
		    						if(m != null)
		    							possibleMoves.add(m);
		    					}
		    					if(DEBUG) System.out.print("(+) ");
		    				} else if(DEBUG)  System.out.print("(-) ");
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

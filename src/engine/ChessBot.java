package engine;

import java.util.Random;
import java.util.function.Predicate;

import search.functions.BoardPredicate;
import search.nodes.BoardNode;
import search.Node;
import board.Board;
import board.Move;
import board.Position;
import board.pieces.Piece;
import search.algorithms.RS;

/**
 * ChessBot is a UCI compatible chess engine
 * 
 * @author Alexander Hoertenhuber
 * @version 0.0
 */
public class ChessBot {
	private Board board;
	
	/**
	 * Create a new (internal) board with initial position
	 */
	public ChessBot() {
		System.out.println("ChessBot by Alexander Hoertenhuber | May 2016");
		init();
	}
	
	/**
	 * Create a new (internal) board with the given position
	 * @param position the position that should be on the board
	 */
	public ChessBot(Position position) {
		board = new Board(position);
	}
	
	/**
	 * Create new (internal) Board with initial position
	 */
	public void init() {
		board = new Board(new Position());
	}
	
	/**
	 * ask the engine to think about the current position and 
	 * return the next move
	 * @return the next move to be played
	 */
	public String getNextMove() {
		board.setColor(board.getActiveColor());
		System.out.println(board.getPossibleMoves());

		/* Random Search */
		RS random = new RS();
		//b.getPosition().getPieces().getBlackPieces().size() < b.getPosition().getPieces().getWhitePieces().size();
		// TODO change Predicate to real goal of the game
		Predicate<Node> endReached = new BoardPredicate(b -> (b.getPossibleMoves().size() == 0 || b.getMoveNr() > 200));
		
		BoardNode endNode = null;
		int i = 0;
		while(endNode == null && i < 10) {
			//System.out.println("search " + i);
			//System.out.println(board.toString());
			BoardNode start = new BoardNode(board.copy());
			endNode = (BoardNode) random.search(start, endReached);
			i++;
		}
		Move nextMove;
		if(endNode == null) {

		}
		
		Node node = endNode;
		try {
			while(!node.parent().isRoot()) {
				node = node.parent();
			}
		} catch (NullPointerException e) {
			System.err.println("Random Search returned null");
			Random rand = new Random();
			//nextMove = board.getPossibleMoves().get(rand.nextInt(board.getPossibleMoves().size()));
			nextMove = null;
			return nextMove.toString();
		}
		/*System.out.println(node.parent());
		for(Node n : node.parent().adjacent()) {
			System.out.println(n.getAction().toString());
		}*/
		System.out.println(node);
		nextMove = node.getAction();

		/*Piece p = board.getPosition().getPieces().getPieceAt(nextMove.getSource());
		System.out.println(board.getPossibleMoves());*/
		
		return nextMove.toString();
	}
	
	/**
	 * 
	 * @param movecmd the move to be made
	 * @return the move is valid and has been executed
	 */
	public boolean makeMove(String movecmd) {
		//System.out.println("Bot: MakeMove: " + movecmd);
		return board.makeMove(new Move(movecmd));
	}
	
	/**
	 * prints a human-readable board to the commandline
	 */
	public void printBoard() {
		board.print();
	}

}

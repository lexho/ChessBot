package engine;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

import search.evalfunctions.ScoreBoard;
import search.datastructures.Pair;
import search.algorithms.AlphaBetaSearch;
import search.functions.BoardFunction;
import search.functions.BoardPredicate;
import search.nodes.BoardNode;
import search.nodes.LimitedNode;
import search.Node;
import board.Board;
import board.Move;
import board.NullMove;
import board.Position;
import board.Position12x10;
import board.PositionInterface;
import board.pieces.Piece;
import board.position.Fen;
import search.algorithms.RS;

/**
 * ChessBot is a UCI compatible chess engine
 * 
 * @author Alexander Hoertenhuber
 * @version 0.0
 */
public class ChessBot {
	private Board board;
	
	public static int NR_OF_THREADS;
	int depthLimit = 3;
	
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
	public ChessBot(PositionInterface position) {
		board = new Board(position);
	}
	
	/** Create a new (internal) board with the given fenstring */
	public ChessBot(String fenstr) {
		board = new Board(new Position(new Fen(fenstr)));
	}
	
	/**
	 * Create new (internal) Board with initial position
	 */
	public void init() {
		NR_OF_THREADS = Runtime.getRuntime().availableProcessors();
		board = new Board(new Position12x10());
	}
	
	/**
	 * ask the engine to think about the current position and 
	 * return the next move
	 * @return the next move to be played
	 */
	public String getNextMove() {
		board.setColor(board.getActiveColor());
		//System.out.println(board);
		//System.out.println(board.getPossibleMoves());
		Move nextMove;
		nextMove = alphaBetaSearch();
		//nextMove = randomSearch();
		//System.out.println(nextMove);
		if(nextMove == null) nextMove = new NullMove();
		return nextMove.toString();
	}
	
	/** use Random Search as search engine 
	 * @return best move found
	 * */
	private Move randomSearch() {
		Move nextMove;
		
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
			return nextMove;
		}
		/*System.out.println(node.parent());
		for(Node n : node.parent().adjacent()) {
			System.out.println(n.getAction().toString());
		}*/
		System.out.println(node);
		nextMove = node.getAction();
		return nextMove;
	}
	
	/** use AlphaBeta Search as search engine
	 * @return best move found
	 * */
	private Move alphaBetaSearch() {
		Move nextMove;
		long searchtime = System.currentTimeMillis(); // current time in milliseconds
		
		/* AlphaBeta Search */
		AlphaBetaSearch alphabeta = new AlphaBetaSearch((d, current) -> d < depthLimit);
		ScoreBoard scoreboard = new ScoreBoard(board.copy());
		Function<Node, Double> evalFunction = new BoardFunction(scoreboard);
		Pair<Node, Double> result = alphabeta.search(
				new LimitedNode(board.copy()),
				evalFunction);
		nextMove = result.f.getAction();
		
		searchtime = System.currentTimeMillis() - searchtime;
		//System.out.println("Possible moves: " + new LimitedNode(board.copy()).adjacent().size());
		System.out.println("Possible moves: " + board.getPossibleMoves().size());
		//System.out.println(alphabeta.nodecount/(searchtime / 60) + " nodes per second");
		System.out.println("searchtime: " + searchtime / 1000 + " s");
		System.out.println(scoreboard.scoreCounter + " scores");
		System.out.println(nextMove + " " + result.s);
		System.out.println();
		//System.out.println(result.f); // print board
		return nextMove;
	}
	/**
	 * 
	 * @param movecmd the move to be made
	 * @return the move is valid and has been executed
	 */
	public boolean makeMove(String movecmd) {
		//System.out.println("Bot: MakeMove: " + movecmd);
		return board.makeMove(new Move(movecmd)); //TODO change to makeMove to enable move validation
	}
	
	/**
	 * prints a human-readable board to the commandline
	 */
	public void printBoard() {
		board.print();
	}
	
	public void printPossibleMoves() {
		int[] prev = {-1, -1};
		for(Move m : board.getPossibleMoves()) {
			int[] src = m.getSource();
			if(src != prev) {
				System.out.println();
				System.out.print(board.getPosition().getPieceAt(src).getCharRep() + " ");
			}
			m.getSourceIndex();
			System.out.print(m);
			prev = src;
		}
		//System.out.print(board.getPossibleMoves());
	}

}

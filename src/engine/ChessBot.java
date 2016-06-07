package engine;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
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
import exceptions.SearchFailException;
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
	int depthLimit = 4;
	
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
	
	/** Stop thinking process of the bot and return bestmove */
	public void stop() {
		//System.out.println("stopping");
		try {
			alphabeta.interrupt();
			board.stop();
		} catch (NullPointerException e) {
			
		}
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
	
	AlphaBetaSearch alphabeta;
	private double currentScore = 0d;
	private double[] aspwindow = {25d, 25d};
	
	/** use AlphaBeta Search as search engine
	 * @return best move found
	 * */
	private Move alphaBetaSearch() {
		Move nextMove = null;
		long searchtime = System.currentTimeMillis(); // current time in milliseconds
		
		/* AlphaBeta Search */
		ScoreBoard scoreboard = new ScoreBoard(board.copy());
		Function<Node, Double> evalFunction = new BoardFunction(scoreboard);
		
		/* Reset aspiration window */
		aspwindow[0] = 25d;
		aspwindow[1] = 25d;
		for(int depth = 1; depth <= depthLimit; depth++) {
			alphabeta = new AlphaBetaSearch(depth);
			
			Pair<Node, Double> result = null;
			double[] aspReset = {new Double(aspwindow[0]), new Double(aspwindow[1])};
			boolean failed = false;
			do {
				/* Prune search tree at higher depths to reduce search time */
				if(depth > 1)
				alphabeta.setBounds(currentScore - aspwindow[0], currentScore + aspwindow[1]); // the higher alpha the more pruning, the lower beta the more pruning
				
				try {
					result = alphabeta.search(
						new BoardNode(board.copy()),
						evalFunction);	
					failed = false;
				} catch (SearchFailException e) {
					failed = true;
					if(e.isAlphaFail()) {
						System.out.println("alpha fail");
						aspwindow[0] = Math.pow(Math.abs(aspwindow[0]), 1.2);
					}
					else {
						System.out.println("beta fail");
						aspwindow[1] = Math.pow(aspwindow[1], 1.2);
					}
				}
				//aspwindow = Math.pow(aspwindow, 1.2);
				System.out.println("new aspiration window: " + (currentScore - aspwindow[0]) + " " + (currentScore + aspwindow[1]));
				/* Aspiration window approach failed --> fall back to full range search */
				if(aspwindow[0] > 10000 || aspwindow[1] > 10000) {
					aspwindow = aspReset; // reset aspiration window
					System.err.println("aspwindow is bigger than 10000 and no move found yet");
					System.out.println("aspwindow is bigger than 10000 and no move found yet");
					System.err.println("starting search without full range window (-infinity and +infinity)");
					System.out.println("starting search without full range window (-infinity and +infinity)");
					alphabeta.setBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
					result = alphabeta.search(
							new BoardNode(board.copy()),
							evalFunction);	
					currentScore = result.s;
					return result.f.getAction();
				}
			} while(failed);
			
			aspwindow = aspReset; // reset aspiration window
			
			//if(result.f != null) {
				System.out.println(result.f.getAction() + " " + result.s + " depth " + depth);
				currentScore = result.s;
				nextMove = result.f.getAction();
			//}
		}
		
		searchtime = System.currentTimeMillis() - searchtime;
		//System.out.println("Possible moves: " + new LimitedNode(board.copy()).adjacent().size());
		System.out.println("score: " + currentScore);
		System.out.println("Possible moves: " + board.getPossibleMoves().size());
		//System.out.println(alphabeta.nodecount/(searchtime / 60) + " nodes per second");
		System.out.println("searchtime: " + searchtime / 1000 + " s");
		System.out.println("scores: " + scoreboard.scoreCounter);
		System.out.println("aspiration window: " + alphabeta.getBounds()[0] + " " + alphabeta.getBounds()[1]);
		System.out.println("pruning alpha: " + alphabeta.getPruningStats().get(0) + ", beta: " + alphabeta.getPruningStats().get(1));
		System.out.println("leaf nodes: " + alphabeta.getLeafNodeStatistics());
		System.out.println();
		return nextMove;
	}
	/**
	 * 
	 * @param movecmd the move to be made
	 * @return the move is valid and has been executed
	 */
	public boolean makeMove(String movecmd) {
		return board.makeMove(new Move(movecmd));
	}
	
	/** Set the number of threads
	 * @param threads number of threads */
	public void setNrOfThreads(int threads) {
		NR_OF_THREADS = threads;
	}
	
	/** Turn Debug-Mode on/off */
	public boolean debugOnOff() {
		if(board.DEBUG) board.DEBUG = false;
		else board.DEBUG = true;
		return board.DEBUG;
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

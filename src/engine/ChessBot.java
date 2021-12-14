package engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Predicate;

import board.Move;
import board.NullMove;
import board.position.Fen;
import board.position.Position;
import board.position.bitboard.PositionBB;
import exceptions.SearchFailException;
import search.Node;
import search.algorithms.AlphaBetaHashSearch;
import search.algorithms.AlphaBetaSearchDouble;
import search.algorithms.RS;
import search.datastructures.Pair;
import search.evalfunctions.ScoreBitBoardDouble;
import search.evalfunctions.ScoreBoard;
import search.functions.BoardFunction;
import search.functions.BoardFunctionDouble;
import search.functions.BoardPredicate;
import search.hashfunctions.ZobristHash;
import search.hashtables.MainTranspositionTable;
import search.nodes.BoardNode;

/**
 * ChessBot is a UCI compatible chess engine
 * 
 * @author Alexander Hoertenhuber
 * @version 0.0
 */
public class ChessBot {
	private Position board;
	
	public static int NR_OF_THREADS;
	int depthLimit = 6;
	private MainTranspositionTable hashmap;
	
	/** ExecutorService for multi-threading move generator in getPossibleMoves() */
	public static ExecutorService executor;
	
	/* Optional Enhancements */
	boolean useHashTable = false;
	boolean useAspWindows = false;
	
	/**
	 * Create a new (internal) board with initial position
	 */
	public ChessBot() {
		System.out.println("ChessBot by Alexander Hoertenhuber | January 2017");
		board = new PositionBB();
		init();
	}

	/**
	 * Create a new (internal) board with the given position
	 * @param position the position that should be on the board
	 */
	public ChessBot(Position position) {
		board = new PositionBB(position);
		init();
	}
	
	/** Create a new (internal) board with the given fenstring */
	public ChessBot(String fenstr) {
		board = new PositionBB(new Fen(fenstr));
		init();
	}
	
	/**
	 * Initialize HashMap, Hashing function and multithreading
	 */
	private void init() {
		NR_OF_THREADS = Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(4);
		ZobristHash.init(); // init hashing function
		//hashmap = new MainTranspositionTable();
		if(useHashTable) hashmap = MainTranspositionTable.readHashMapFromFile("/home/alex/ChessBot/assets/hashmap/hashmap.ser");
	}
	
	/**
	 * Create new (internal) Board with initial position
	 */
	public void reset() {
		NR_OF_THREADS = Runtime.getRuntime().availableProcessors();
		board = new PositionBB();
		ZobristHash.init(); // init hash table
		//hashmap = new MainTranspositionTable();
	}
	
	public void useHashTable(boolean useHashTable) {
		this.useHashTable = useHashTable;
	}

	public void useAspWindows(boolean useAspWindows) {
		this.useAspWindows = useAspWindows;
	}
	
	/** Stop thinking process of the bot and return bestmove */
	public void stop() {
		//System.out.println("stopping");
		try {
			alphabeta.interrupt();
			//board.stop();
		} catch (NullPointerException e) {
			
		}
	}
	
	/** was search result a null move? */
	boolean nullmove = true;
	
	/** was search result a null move? */
	public boolean wasNullMove() {
		return nullmove;
	}
	
	/**
	 * ask the engine to think about the current position and 
	 * return the next move
	 * @return the next move to be played
	 */
	public String getNextMove() {
		//System.out.println("active color: " + board.getActiveColor());
		board.setActiveColor(board.getActiveColor());
		
		Move nextMove = null;
		
		if(!useAspWindows && !useHashTable) nextMove = alphaBetaSearchPure();
		if(useAspWindows) nextMove = alphaBetaSearchAspWindows();
		if(!useAspWindows && useHashTable) nextMove = alphaBetaSearchHashTable();

		if(nextMove == null) {
			nullmove = true;
			nextMove = new NullMove();
			// fallback to random move
			/*Random rnd = new Random();
			int r = rnd.nextInt(board.getPossibleMoves().size());
			Move m = board.getPossibleMoves().get(r);
			if(m == null) System.err.println("rnd move is null");
			nextMove = m;*/
		} else nullmove = false;
		
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
	
	public AlphaBetaSearchDouble alphabeta;
	private double currentScore = 0;
	private int[] aspwindow = {25, 25};
	
	/** use AlphaBeta Search with aspiration windows and a hashtable as search engine
	 * @return best move found
	 * */
	private Move alphaBetaSearchAspWindows() {
		Move nextMove = null;
		long searchtime = System.currentTimeMillis(); // current time in milliseconds
		
		/* AlphaBeta Search */
		ScoreBoard<Double> scoreboard = new ScoreBitBoardDouble(board.copy());
		Function<Node, Double> evalFunction = new BoardFunction(scoreboard);
		
		/* Reset aspiration window */
		aspwindow[0] = 25;
		aspwindow[1] = 25;

		// iterative deepening
		for(int depth = 1; depth <= depthLimit; depth++) {
			/* Use hashmap? */
			if(useHashTable) 
				alphabeta = null; //new AlphaBetaHashSearch(depth, hashmap, searchtime);
			else 
				alphabeta = new AlphaBetaSearchDouble(depth, searchtime);
				
			Pair<Node, Double> result = null;
			int[] aspReset = {new Integer(aspwindow[0]), new Integer(aspwindow[1])};
			boolean failed = false;
			do {
				/* Prune search tree at higher depths to reduce search time */
				if(depth > 1)
				alphabeta.setBounds(currentScore - aspwindow[0], currentScore + aspwindow[1]); // the higher alpha the more pruning, the lower beta the more pruning
				alphabeta.setTime(time);
				
				//System.out.println("possible moves: " + board.getPossibleMoves());
				//System.out.println("possible moves: " + board.copy().getPossibleMoves());
				try {
					result = alphabeta.search(
						new BoardNode(board.copy()),
						evalFunction);	
					System.out.println("result depth " + depth + ": " + result);
					failed = false;
				} catch (SearchFailException e) {
					failed = true;
					if(e.isAlphaFail()) {
						aspwindow[0] = (int) Math.pow(Math.abs(aspwindow[0]), 1.2);
					}
					else {
						aspwindow[1] = (int) Math.pow(aspwindow[1], 1.2);
					}
					System.out.println("new aspiration window: " + (currentScore - aspwindow[0]) + " " + (currentScore + aspwindow[1]));
				}
				
				System.out.println("cb result state: " + alphabeta.getResultState());
				if(alphabeta.getResultState() != 0) System.exit(-1);
				
				/* Aspiration window approach failed --> fall back to full range search */
				if(aspwindow[0] > 10000 || aspwindow[1] > 10000) {
					aspwindow = aspReset; // reset aspiration window
					System.err.println("aspwindow is bigger than 10000 and no move found yet");
					System.out.println("aspwindow is bigger than 10000 and no move found yet");
					System.err.println("starting search without full range window (-infinity and +infinity)");
					System.out.println("starting search without full range window (-infinity and +infinity)");
					alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
					result = alphabeta.search(
							new BoardNode(board.copy()),
							evalFunction);	
					currentScore = result.s;
					System.out.println("fallback result depth " + depth + ": " + result);
					return result.f.getAction();
				}
			} while(failed);
			
			aspwindow = aspReset; // reset aspiration window
			
			System.out.println(result.f.getAction() + " " + result.s + " depth " + depth);
			currentScore = result.s;
			nextMove = result.f.getAction();
		}
		
		searchtime = System.currentTimeMillis() - searchtime;
		
		//printSearchStats(searchtime, scoreboard);
		
		return nextMove;
	}
	
	/** use AlphaBeta Search with a hashtable as search engine
	 * @return best move found
	 * */
	private Move alphaBetaSearchHashTable() {
		Move nextMove = null;
		// TODO fix
		long searchtime = System.currentTimeMillis(); // current time in milliseconds
		
		// AlphaBeta Search 
		ScoreBoard<Double> scoreboard = new ScoreBitBoardDouble(board.copy());
		Function<Node, Double> evalFunction = new BoardFunctionDouble(scoreboard);
		
		long starttime = System.currentTimeMillis();
		Timer timer = new Timer();
		// iterative deepening
		List<Pair<Node,Double>> results = new ArrayList<Pair<Node,Double>>();
		for(int depth = 1; depth <= depthLimit; depth++) {
			//System.out.println("depth: " + depth);
			alphabeta = new AlphaBetaHashSearch(depth, hashmap, starttime);
			
			Pair<Node, Double> result = null;
			//double[] aspReset = {new Double(aspwindow[0]), new Double(aspwindow[1])};
			//boolean failed = false;
			alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
			alphabeta.setTime(time);
			
			/*executor = Executors.newFixedThreadPool(4);
			AlphaBetaSearchInt alphabeta1 = new AlphaBetaHashSearch(depth, hashmap, starttime);
			AlphaBetaSearchInt alphabeta2 = new AlphaBetaHashSearch(depth, hashmap, starttime);
			AlphaBetaSearchInt alphabeta3 = new AlphaBetaHashSearch(depth + 1, hashmap, starttime);
			AlphaBetaSearchInt alphabeta4 = new AlphaBetaHashSearch(depth + 1, hashmap, starttime);
			Callable<Pair<Node, Integer>> search1 = new Callable<Pair<Node, Integer>>() {
	        	
	            public Pair<Node, Integer> call() throws Exception {
	            	 return alphabeta1.search(
	     					new BoardNode(board.copy()),
	    					evalFunction);	
	            }
			};
			Callable<Pair<Node, Integer>> search2 = new Callable<Pair<Node, Integer>>() {
	        	
	            public Pair<Node, Integer> call() throws Exception {
	            	 return alphabeta2.search(
	     					new BoardNode(board.copy()),
	    					evalFunction);	
	            }
			};
			Callable<Pair<Node, Integer>> search3 = new Callable<Pair<Node, Integer>>() {
	        	
	            public Pair<Node, Integer> call() throws Exception {
	            	 return alphabeta3.search(
	     					new BoardNode(board.copy()),
	    					evalFunction);	
	            }
			};
			Callable<Pair<Node, Integer>> search4 = new Callable<Pair<Node, Integer>>() {
	        	
	            public Pair<Node, Integer> call() throws Exception {
	            	 return alphabeta4.search(
	     					new BoardNode(board.copy()),
	    					evalFunction);	
	            }
			};
			try {
				result = executor.submit(search1).get();
				result = executor.submit(search2).get();
				result = executor.submit(search3).get();
				result = executor.submit(search4).get();
				System.out.println("Nr of Threads: " + Thread.activeCount());			
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			executor.shutdown();*/
			
			result = alphabeta.search(
					new BoardNode(board.copy()),
					evalFunction);	
			results.add(result);
			System.out.println("bot result state iteration " + depth + ": " + alphabeta.getResultState());
			System.out.println("result depth " + depth + ": " + result);
			
			class InfoMessageTask extends TimerTask {  
				@Override
			   public void run()
				{
					alphabeta.printInfoMessage();
					/*alphabeta2.printInfoMessage();
					alphabeta3.printInfoMessage();
					alphabeta4.printInfoMessage();*/
		   		}
			};
			timer.cancel();
			timer = new Timer();
			//timer.scheduleAtFixedRate(new InfoMessageTask(), 0, 1000);
			
	        /*try {
				executor.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        System.out.println("Finished all threads");*/

			currentScore = result.s;
			nextMove = result.f.getAction();
			
			System.out.println("bot result state: " + alphabeta.getResultState());
			if((alphabeta.getResultState() & 0b10000) == 16) {
			//if(alphabeta.getResultState() == 16 ||  alphabeta.getResultState() == 32) {
				System.out.println("fallback action");
				// fallback to previous iteration
				
				int i = results.size() - 1;
				if(i >= 0) {
					while(i > 0 && results.get(i).f.getAction() == null)
						--i; // back to previous iteration
	
					for(int j = results.size() - 1; j >= 0; --j) {
						if(results.get(j).f.getAction() != null)
							System.out.println(j + " " + results.get(j).f.getAction().toString());
						else
							System.out.println(j + " null");
					}
					
					nextMove = results.get(i).f.getAction();
					System.out.println("falling back to iteration " + (i+1));
					System.out.println("results size: " + results.size());
					System.out.println("nextMove: " + nextMove);
					System.out.println("depth: " + depth);
				}
			}
		}
		
		
		
		searchtime = System.currentTimeMillis() - searchtime;
        timer.cancel();
		printSearchStats(searchtime, scoreboard);
		
		return nextMove;
	}
	
	/** use AlphaBeta Search without any additional enhancement as search engine
	 * @return best move found
	 * */
	private Move alphaBetaSearchPure() {
		System.out.println("alphabeta pure");
		Move nextMove = null;
		long searchtime = System.currentTimeMillis(); // current time in milliseconds
		
		/* AlphaBeta Search */
		ScoreBoard<Double> scoreboard = new ScoreBitBoardDouble(board.copy());
		Function<Node, Double> evalFunction = new BoardFunction(scoreboard);
		
		long starttime = System.currentTimeMillis();
		// iterative deepening
		List<Pair<Node,Double>> results = new ArrayList<Pair<Node,Double>>();
		for(int depth = 1; depth <= depthLimit; depth++) {

			Pair<Node, Double> result = null;
			alphabeta = new AlphaBetaSearchDouble(depth, starttime);
			alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
			alphabeta.setTime(time);

			result = alphabeta.search(
					new BoardNode(board.copy()),
					evalFunction);
			results.add(result);
			System.out.println("bot result state iteration " + depth + ": " + alphabeta.getResultState());
			System.out.println("result depth " + depth + ": " + result);
			currentScore = result.s;
			
			nextMove = result.f.getAction();
			
			// interrupted or time is up
			//TODO incomplete!
			System.out.println("bot result state: " + alphabeta.getResultState());
			// time is up --> fallback to previous iteration
			//if((alphabeta.getResultState() & 0b10000) == 16) { 
			if(alphabeta.getResultState() == 16 ||  alphabeta.getResultState() == 32) {
				System.out.println("time is up! fallback action");
				
				int i = results.size() - 1;
				if(i >= 0) {
					while(i > 0 && results.get(i).f.getAction() == null)
						--i; // back to previous iteration
	
					for(int j = results.size() - 1; j >= 0; --j) {
						if(results.get(j).f.getAction() != null)
							System.out.println(j + " " + results.get(j).f.getAction().toString());
						else
							System.out.println(j + " null");
					}
					
					nextMove = results.get(i).f.getAction();
					System.out.println("falling back to iteration " + (i+1));
					System.out.println("results size: " + results.size());
					System.out.println("nextMove: " + nextMove);
					System.out.println("depth: " + depth);
					
					searchtime = System.currentTimeMillis() - searchtime;
					
					//printSearchStats(searchtime, scoreboard);
					System.out.println("move: " + nextMove);
					return nextMove;
				}
			}
		}
		
		searchtime = System.currentTimeMillis() - searchtime;
		
		//printSearchStats(searchtime, scoreboard);
		System.out.println("move: " + nextMove);
		return nextMove;
	}
	
	private void printSearchStats(long searchtime, ScoreBoard<Double> scoreboard) {
		System.out.println("score: " + currentScore);
		System.out.println("Possible moves: " + board.getPossibleMoves().size());
		System.out.println("searchtime: " + searchtime / 1000 + " s");
		System.out.println("scores: " + scoreboard.getScoreCounter());
		System.out.println("aspiration window: " + alphabeta.getBounds()[0] + " " + alphabeta.getBounds()[1]);
		System.out.println("hashmap size: " + hashmap.size());
		System.out.println("pruning alpha: " + alphabeta.getPruningStats().get(0) + ", beta: " + alphabeta.getPruningStats().get(1));
		System.out.println("leaf nodes: " + alphabeta.getLeafNodeStatistics());
		System.out.println();
	}
	
	/** @return current bestmove score */
	public double getScore() {
		return currentScore;
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
	
	/** Set the depth limit
	 * @param depth limit */
	public void setDepthLimit(int limit) {
		depthLimit = limit;
	}
	
	int time;
	int wtime;
	int btime;

	public void setWtime(int time) {
		this.wtime = time;
		this.time = wtime;
	}
	
	public void setBtime(int btime) {
		this.btime = btime;
		this.time = (wtime + btime) / 2;
	}
	
	/** Turn Debug-Mode on/off */
	public boolean debugOnOff() {
		/*if(board.DEBUG) board.DEBUG = false;
		else board.DEBUG = true;
		return board.DEBUG;*/
		// TODO debug function has no effect
		return false;
	}
	
	/**
	 * prints a human-readable board to the commandline
	 */
	public void printBoard() {
		System.out.println(board);
	}
	
	public void printPossibleMoves() {
		int prev = -1;
		for(Move m : board.getPossibleMoves()) {
			int src = m.getSource8x8Index();
			if(src != prev) {
				System.out.println();
				System.out.print(board.getPieceAt(src) + " ");
			}

			System.out.print(m + " ");
			//System.out.print(m.getSource8x8Index() + " " + m.getTarget8x8Index() + " ");
			prev = src;
		}
		System.out.println();
		//System.out.print(board.getPossibleMoves());
	}

}

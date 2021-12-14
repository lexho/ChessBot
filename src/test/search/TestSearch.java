package test.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import board.Move;
import board.position.Position;
import board.position.bitboard.PositionBB;
import engine.ChessBot;
import search.Node;
import search.algorithms.AlphaBetaSearch2;
import search.algorithms.AlphaBetaSearchDouble;
import search.algorithms.integer.AlphaBetaSearchInt;
import search.datastructures.Pair;
import search.evalfunctions.ScoreBitBoard;
import search.evalfunctions.ScoreBitBoardDouble;
import search.evalfunctions.ScoreBoard;
import search.functions.BoardFunction;
import search.functions.BoardFunctionDouble;
import search.nodes.BoardNode;
import util.NullPrinter;

//@RunWith(Parameterized.class)
public class TestSearch {
	
	//@Parameters
	/*public static Collection<Object[]> generateParams() {
		List<Object[]> params = new ArrayList<Object[]>();
		
		for(int d = 1; d < 8; d++) {
			params.add(new Object[] {new Integer(d), new String("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - 0 2")});
		}

		return params;
	}
	
	private int depth;
	private String fenstr;
	
	public TestSearch(Integer d, String f) {
		depth = d;
		fenstr = f;
	}*/
	
	/*@Test
	public void testSearch() {
		// disable output stream
		PrintStream outputStream = System.out;
		System.setOut(NullPrinter.out);
		
		ChessBot bot;
		//ChessBot bot = new ChessBot();
		bot = new ChessBot(fenstr);
		bot.useAspWindows(false);
		bot.useHashTable(false);
		
		bot.stop(); // stop currently running processes
		bot.setDepthLimit(depth);

		String nextMove = bot.getNextMove();
		
		// enable output stream
		System.setOut(outputStream);
		System.out.println("depth " + depth + " bestmove " + nextMove + " " + bot.getScore());
	}*/
	
	@Test
	public void testAlphaBetaSearchDepth1() {
		Position board = new PositionBB();
		ScoreBoard<Integer> scoreboard = new ScoreBitBoard(board.copy());
		List<Pair<Move,Integer>> scoredMoves = new ArrayList<Pair<Move,Integer>>();
		// score every opening move a2a3 => -40, b2b3 => -40, c2c3 => -30,..
		// and sort them to get best scored move
		for(Move move : board.getPossibleMoves()) {
			Position b = board.copy();
			b.makeMove(move);
			int score = scoreboard.apply(b);
			Pair<Move,Integer> scoredMove = new Pair<Move,Integer>(move, score);
			scoredMoves.add(scoredMove);
			//System.out.println(move + " " + score);
		}
		class ScoreComparator implements Comparator<Pair<Move,Integer>> {
		    @Override
		    public int compare(Pair<Move,Integer> a, Pair<Move,Integer> b) {
		    	 return a.s > b.s ? -1 : a.s == b.s ? 0 : 1;
		    }
		}

		Collections.sort(scoredMoves, new ScoreComparator());
		System.out.println(scoredMoves);
		
		
		
		board = new PositionBB();
		Move nextMove = null;
		long searchtime = System.currentTimeMillis(); 
		Function<Node, Integer> evalFunction = new BoardFunction(scoreboard);
		
		long starttime = System.currentTimeMillis();

		AlphaBetaSearchInt alphabeta = new AlphaBetaSearchInt(1, starttime);
		
		Pair<Node, Integer> result = null;
		alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
		alphabeta.setTime(300000);
		//if(alphabeta.timeIsUp()) break; //TODO messes up result states
		result = alphabeta.search(
				new BoardNode(board.copy()),
				evalFunction);

		System.out.println(result);
		System.out.println("bestmove: " + scoredMoves.get(0).f + " " + scoredMoves.get(0).s);
		System.out.println("bestmove: " + result.f.getAction() + " " + result.s);
		assertEquals(scoredMoves.get(0).f, result.f.getAction());
		assertEquals(scoredMoves.get(0).s, result.s);
	}
	
	@Test
	public void testAlphaBetaSearchAlgorithms() {
		int depth = 2;
		List<Long> searchTime = new ArrayList<Long>(); 
		Position board = new PositionBB("r1bq2r1/pppppk1p/5b2/4p1B1/Q1P1N3/8/PP3PPP/R3K1NR b KQ - 4 13");
		ScoreBoard scoreboard = new ScoreBitBoard(board.copy());
		System.out.println("### search1");
		Move nextMove = null;
		Function<Node, Integer> evalFunction = new BoardFunction(scoreboard);
		
		long startTime = System.currentTimeMillis();

		AlphaBetaSearchInt alphabeta = new AlphaBetaSearchInt(depth, startTime);
		
		Pair<Node, Integer> result = null;
		alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
		alphabeta.setTime(300000);

		result = alphabeta.search(
				new BoardNode(board.copy()),
				evalFunction);

		//System.out.println(result);
		System.out.println("bestmove: " + result.f.getAction() + " " + result.s);
		long searchTime1 = System.currentTimeMillis() - startTime;
		searchTime.add(searchTime1);
		System.out.println("search time: " + searchTime1);
		System.out.println();
		
		// search algorithm 2
		System.out.println("### search2");
		ScoreBoard<Double> scoreboard2 = new ScoreBitBoardDouble(board.copy());
		nextMove = null;
		Function<Node, Double> evalFunction2 = new BoardFunctionDouble(scoreboard2);
		
		startTime = System.currentTimeMillis();

		AlphaBetaSearch2 alphabeta2 = new AlphaBetaSearch2((d, current) -> d < depth);
		
		Pair<Node, Double> result2 = null;
		/*alphabeta2.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
		alphabeta2.setTime(300000);*/

		result2 = alphabeta2.search(
				new BoardNode(board.copy()),
				evalFunction2);

		//System.out.println(result);
		System.out.println("bestmove: " + result2.f.getAction() + " " + result2.s);
		long searchTime2 = System.currentTimeMillis() - startTime;
		searchTime.add(searchTime2);
		System.out.println("search time: " + searchTime2);
		System.out.println();

		// search algorithm 3
		System.out.println("### search3 (AlphaBeta with double)");
		scoreboard2 = new ScoreBitBoardDouble(board.copy());
		nextMove = null;
		evalFunction2 = new BoardFunctionDouble(scoreboard2);

		startTime = System.currentTimeMillis();

		//AlphaBetaSearchDouble alphabeta3 = new AlphaBetaSearchDouble((d, current) -> d < depth);
		AlphaBetaSearchDouble alphabeta3 = new AlphaBetaSearchDouble(depth, startTime);

		Pair<Node, Double> result3 = null;
		//alphabeta2.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
		//alphabeta2.setTime(300000);
		alphabeta3.setTime(300000);

		result3 = alphabeta3.search(
				new BoardNode(board.copy()),
				evalFunction2);

		//System.out.println(result);
		System.out.println("bestmove: " + result3.f.getAction() + " " + result3.s);
		long searchTime3 = System.currentTimeMillis() - startTime;
		searchTime.add(searchTime3);
		System.out.println("search time: " + searchTime3);
		
		System.out.println();
		
		System.out.println("### comparison speed search1 and search2 and search3");
		long fastestIndex = 0;
		long fastestTime = Integer.MAX_VALUE;
		int i = 0;
		for(long searchtime : searchTime) {
			if(searchtime < fastestTime)  fastestIndex = i;
			i++;
		}
		fastestIndex++; // correct index (starts with 1, not 0)
		
		System.out.println("fastest: search" + fastestIndex);
		System.out.println("search algorithm " + fastestIndex + " is faster");
		System.out.println("search time1: " + searchTime1);
		System.out.println("search time2: " + searchTime2);
		System.out.println("search time3: " + searchTime3);
		//long diff = searchTime2 - searchTime1;
		//if(diff < 0) diff *= -1d;
		/*if(searchTime2 < searchTime1) System.out.println("search algorithm 2 is faster (diff: " + diff + " ms)");
		else if (searchTime2 == searchTime1) System.out.println("search algorithm are equal");
		else System.out.println("search algorithm 1 is faster (diff: " + diff + " ms)");*/
	
		assertEquals(result.f.getAction().toString(), result3.f.getAction().toString());
	}
	
	@Test
	public void testAlphaBetaSearchAlgorithms2() {
		int depth = 6;
		List<Long> searchTime = new ArrayList<Long>(); 
		Position board = new PositionBB("r1bq2r1/pppppk1p/5b2/4p1B1/Q1P1N3/8/PP3PPP/R3K1NR b KQ - 4 13");
		board = new PositionBB(); // initial position
		ScoreBoard scoreboard = new ScoreBitBoard(board.copy());
		System.out.println("### search1");
		Move nextMove = null;
		Function<Node, Integer> evalFunction = new BoardFunction(scoreboard);
		
		long startTime = System.currentTimeMillis();

		AlphaBetaSearchInt alphabeta = new AlphaBetaSearchInt(depth, startTime);
		
		Pair<Node, Integer> result = null;
		alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
		alphabeta.setTime(15000000);

		result = alphabeta.search(
				new BoardNode(board.copy()),
				evalFunction);

		//System.out.println(result);
		System.out.println("bestmove: " + result.f.getAction() + " " + result.s);
		long searchTime1 = System.currentTimeMillis() - startTime;
		searchTime.add(searchTime1);
		System.out.println("search time: " + searchTime1);
		System.out.println();
		

		// search algorithm 2
		System.out.println("### search2 (AlphaBeta with double)");
		ScoreBoard scoreboard2;
		Function<Node, Double> evalFunction2;
		scoreboard2 = new ScoreBitBoardDouble(board.copy());
		nextMove = null;
		evalFunction2 = new BoardFunctionDouble(scoreboard2);

		startTime = System.currentTimeMillis();

		//AlphaBetaSearchDouble alphabeta3 = new AlphaBetaSearchDouble((d, current) -> d < depth);
		AlphaBetaSearchDouble alphabeta2 = new AlphaBetaSearchDouble(depth, startTime);

		Pair<Node, Double> result2 = null;
		alphabeta2.setTime(15000000);

		result2 = alphabeta2.search(
				new BoardNode(board.copy()),
				evalFunction2);

		//System.out.println(result);
		System.out.println("bestmove: " + result2.f.getAction() + " " + result2.s);
		long searchTime2 = System.currentTimeMillis() - startTime;
		searchTime.add(searchTime2);
		System.out.println("search time: " + searchTime2);
		
		System.out.println();
		
		System.out.println("### comparison speed search1 and search2");
		long fastestIndex = 0;
		long fastestTime = Integer.MAX_VALUE;
		int i = 0;
		for(long searchtime : searchTime) {
			if(searchtime < fastestTime)  fastestIndex = i;
			i++;
		}
		fastestIndex++; // correct index (starts with 1, not 0)
		
		System.out.println("fastest: search" + fastestIndex);
		System.out.println("search algorithm " + fastestIndex + " is faster");
		System.out.println("search time1: " + searchTime1);
		System.out.println("search time2: " + searchTime2);
		long diff = searchTime2 - searchTime1;
		if(diff < 0) diff *= -1d;
		if(searchTime2 < searchTime1) System.out.println("search algorithm 2 is faster (diff: " + diff + " ms)");
		else if (searchTime2 == searchTime1) System.out.println("search algorithm are equal");
		else System.out.println("search algorithm 1 is faster (diff: " + diff + " ms)");
	
		assertEquals(result.f.getAction().toString(), result2.f.getAction().toString()); // resulting moves should be the same
	}
	
	@Test
	public void testAlphaBetaSearchDefensiveKnightMove() {
		Position board = new PositionBB();
		ScoreBoard scoreboard = new ScoreBitBoard(board.copy());

		board = new PositionBB("rnbqkb1r/ppp1pppp/5n2/8/4p3/5N2/PPPPPPPP/R1BQKB1R w KQkq - 0 4");
		Move nextMove = null;
		long searchtime = System.currentTimeMillis(); 
		Function<Node, Integer> evalFunction = new BoardFunction(scoreboard);
		
		long starttime = System.currentTimeMillis();

		for(int d = 4; d <= 6; d++) {
			AlphaBetaSearchInt alphabeta = new AlphaBetaSearchInt(d, starttime);

			Pair<Node, Integer> result = null;
			alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
			alphabeta.setTime(1200000); // 20 min
			//if(alphabeta.timeIsUp()) break; //TODO messes up result states
			result = alphabeta.search(
					new BoardNode(board.copy()),
					evalFunction);

			//System.out.println(result);
			System.out.println("bestmove: " + result.f.getAction() + " " + result.s);
			// do not sacrifice the knight for a 'good' position
			assertNotEquals("f3e5", result.f.getAction().toString()); 
			//assertTrue(result.f.getAction().toString().equals("f3g1")||result.f.getAction().toString().equals("f3g5"));

		}
	}
	
	@Test
	public void testAlphaBetaSearchDefensiveKnightMove2() {
		Position board = new PositionBB();
		ScoreBoard scoreboard = new ScoreBitBoard(board.copy());

		board = new PositionBB("r1bqkb1r/pppppppp/2n2n2/8/3PP3/2N5/PPP2PPP/R1BQKBNR b KQkq d3 0 3");
		Move nextMove = null;
		long searchtime = System.currentTimeMillis(); 
		Function<Node, Integer> evalFunction = new BoardFunction(scoreboard);
		
		long starttime = System.currentTimeMillis();

		for(int d = 4; d <= 6; d++) {
			AlphaBetaSearchInt alphabeta = new AlphaBetaSearchInt(d, starttime);

			Pair<Node, Integer> result = null;
			alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
			alphabeta.setTime(1200000); // 20 min
			//if(alphabeta.timeIsUp()) break; //TODO messes up result states
			result = alphabeta.search(
					new BoardNode(board.copy()),
					evalFunction);

			//System.out.println(result);
			System.out.println("bestmove: " + result.f.getAction() + " " + result.s);
			// do not sacrifice the knight for a 'good' position
			assertNotEquals("c6e5", result.f.getAction().toString()); 
			//assertTrue(result.f.getAction().toString().equals("f3g1")||result.f.getAction().toString().equals("f3g5"));

		}
	}
	
	@Test
	public void testAlphaBetaSearchDefensiveBishopMove() {
		Position board = new PositionBB();
		ScoreBoard scoreboard = new ScoreBitBoard(board.copy());

		board = new PositionBB("r1bq1rk1/pp2bppp/2n2n2/2p5/4p3/4P3/PPPPBPPP/R1BQ2KR w - - 2 12");
		Move nextMove = null;
		long searchtime = System.currentTimeMillis(); 
		Function<Node, Integer> evalFunction = new BoardFunction(scoreboard);
		
		long starttime = System.currentTimeMillis();

		for(int d = 4; d <= 6; d++) {
			AlphaBetaSearchInt alphabeta = new AlphaBetaSearchInt(d, starttime);

			Pair<Node, Integer> result = null;
			alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
			alphabeta.setTime(2200000); // 20 min
			//if(alphabeta.timeIsUp()) break; //TODO messes up result states
			result = alphabeta.search(
					new BoardNode(board.copy()),
					evalFunction);

			//System.out.println(result);
			System.out.println("bestmove: " + result.f.getAction() + " " + result.s);
			// do not sacrifice the bishop for a 'good' position
			assertNotEquals("e2g4", result.f.getAction().toString()); 
		}
	}
	
	@Test
	public void testAlphaBetaSearchTakeTheBishopMove() {
		Position board = new PositionBB();
		ScoreBoard scoreboard = new ScoreBitBoard(board.copy());

		board = new PositionBB("r1bq2r1/pppppk1p/5b2/4p1B1/Q1P1N3/8/PP3PPP/R3K1NR b KQ - 4 13");
		System.out.println(board);
		Move nextMove = null;
		long searchtime = System.currentTimeMillis(); 
		Function<Node, Integer> evalFunction = new BoardFunction(scoreboard);
		
		long starttime = System.currentTimeMillis();

		for(int d = 4; d <= 6; d++) {
			AlphaBetaSearchInt alphabeta = new AlphaBetaSearchInt(d, starttime);

			Pair<Node, Integer> result = null;
			alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
			alphabeta.setTime(2200000); // 20 min
			//if(alphabeta.timeIsUp()) break; //TODO messes up result states
			result = alphabeta.search(
					new BoardNode(board.copy()),
					evalFunction);

			//System.out.println(result);
			System.out.println("bestmove: " + result.f.getAction() + " " + result.s);
			// do not sacrifice the bishop for a 'good' position
			assertEquals("g8g5", result.f.getAction().toString()); 
		}
	}
}

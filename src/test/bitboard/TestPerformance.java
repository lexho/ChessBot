package test.bitboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import board.Board;
import board.Move;
import board.pieces.Piece;
import board.position.Fen;
import board.position.Position;
import board.position.bitboard.PositionBB;
import board.position.bitboard.UndoInfo;

@RunWith(Parameterized.class)
public class TestPerformance {
	@Parameters
	public static Collection<Object[]> generateParams() {
		List<Object[]> params = new ArrayList<Object[]>();
		
		/*for(int i = 0; i < 6; i++) {
			params.add(new Object[]{i, expected[i]});
		}*/
		
		PositionBB initialPos = new PositionBB(); // initial position
		
		/* perft results for initial position from 
		 * http://chessprogramming.wikispaces.com/Perft+Results
		 *                       depth nodes position */
		params.add(new Object[]{ 0,    1L, initialPos});
		params.add(new Object[]{ 1,    20L, initialPos});
		params.add(new Object[]{ 2,    400L, initialPos});
		params.add(new Object[]{ 3,    8902L, initialPos});
		params.add(new Object[]{ 4,    197281L, initialPos});
		params.add(new Object[]{ 5,    4865609L, initialPos});
		params.add(new Object[]{ 6,    119060324L, initialPos});
		
		params.add(new Object[]{ 7,    119060324L, initialPos});
		params.add(new Object[]{ 8,    119060324L, initialPos});
		params.add(new Object[]{ 9,    119060324L, initialPos});
		params.add(new Object[]{ 10,    119060324L, initialPos});
		params.add(new Object[]{ 11,    119060324L, initialPos});
		params.add(new Object[]{ 12,    119060324L, initialPos});
		
		return params;
	}
	
	private int depth;
	private long expected; // expected number of nodes
	private PositionBB position;
	
	public TestPerformance(Integer depth, Long expected, PositionBB pos) {
		this.depth = depth;
		this.expected = expected;
		this.position = pos;
	}
	
	@Test
	public void testMoveGeneratorInitialPosition() {
		final long start = System.currentTimeMillis();
		long nodes = perft(position, depth);
		final long time = System.currentTimeMillis() - start;
		System.out.print("nodes: " + nodes + " (" + expected + ")");
		//System.out.print(" on position " + );
		System.out.print(" at depth: " + depth + " in ");
		System.out.println(time + " ms");
		//System.out.println("speed: " + nodes/Math.round(((double)time / 1000d)) + " nps");
		assertEquals(expected, nodes);		
	}
	
	@Test
	public void testMoveGenerateorRandomPosition() {
		Random random = new Random();
		PositionBB positionRnd = new PositionBB(true);
		List<Short> piecesW = new ArrayList<Short>();
		
		for(short p : Piece.piecesWwithoutPawns) {
			piecesW.add(p);
		}
		int sq = random.nextInt(64);
		int p = random.nextInt(piecesW.size());
		int piece = piecesW.get(p);
		piecesW.remove(p);
		//positionRnd.setPiece(sq, piece);
		System.out.println(positionRnd);
	}
	
	/** performance test 
	 * http://chessprogramming.wikispaces.com/Perft */
	private long perft(PositionBB pos, int depth)
	{
	    List<Move> move_list;
	    long nodes = 0;
	 
	    if (depth == 0) return 1;
	 
	    move_list = pos.getPossibleMoves();
	    for(Move m : move_list) {
	    	UndoInfo ui = new UndoInfo();
	    	pos.makeMove(m, ui);
	    	nodes += perft(pos, depth - 1);
	    	pos.unMakeMove(m, ui);
	    }
	    return nodes;
	}
	
	
	@Test
	public void checkDetectionSpeed() {
		String fenstring = "8/8/8/5k2/8/r2K4/8/8 w - - 0 1"; // white is in check
		Board b = new Board(new PositionBB(new Fen(fenstring)));
		b.print();
		b.getPositionBB().whiteMove();
		Position pos = b.getPosition();
		long start = System.currentTimeMillis();
		for(int i = 0; i < 100000; i++) {
			pos.isInCheck(true); // white is in check?
			pos.isInCheck(false); // black is in check?
		}
		long end = System.currentTimeMillis();
		long duration = end - start;
		duration *= 100000;
		System.out.println("start: " + start);
		System.out.println("end: " + end);
		System.out.println("duration: " + duration + " iterations/s");
		assertTrue(duration > 20000000);
	}

}

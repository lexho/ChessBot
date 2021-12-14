package test.bitboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

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

public class TestPerformance2 {

	@Test
	public void testMoveGenerateorRandomPosition() {
		Random random = new Random();
		PositionBB positionRnd = new PositionBB(true);
		List<Short> piecesW = new ArrayList<Short>();
		for(short p : Piece.piecesWwithoutPawns) {
			piecesW.add(p);
		}
		
		Stack<Integer> squares = new Stack<Integer>();
		for(int i = 0; i < 64; i++) {
			squares.push(i);
		}
		
		Collections.shuffle(squares);
		int limit = random.nextInt(piecesW.size());
		
		while(piecesW.size() - limit > 0) {
			int sq = squares.pop();
			int p = random.nextInt(piecesW.size());
			int piece = piecesW.get(p);
			int pieceB = piecesW.get(p) + 6;
			piecesW.remove(p);
			positionRnd.setPiece(sq, piece);
			sq = squares.pop();
			positionRnd.setPiece(sq, pieceB);
		}
		
		System.out.println(positionRnd);
		PositionBB position = positionRnd;
		for(int d = 1; d < 6; d++) {
			int depth = d;
			long expected = 8902L;

			final long start = System.currentTimeMillis();
			long nodes = perft(position, depth);
			final long time = System.currentTimeMillis() - start;
			System.out.print("nodes: " + nodes + " (" + expected + ")");
			//System.out.print(" on position " + );
			System.out.print(" at depth: " + depth + " in ");
			System.out.print(time + " ms, ");
			long nps = (long) ((double) nodes / (double) time * 1000d);
			System.out.println(nps + " nps");
			//System.out.println("speed: " + nodes/Math.round(((double)time / 1000d)) + " nps");
			//assertEquals(expected, nodes);		
		}
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
	public void testPieceMoves() {
		boolean result;
		String fenstring = "8/8/2n2n2/8/8/2N2N2/8/8 w - - 0 1";
		System.out.println("performance knights: ");
		Board b = new Board(new PositionBB(new Fen(fenstring)));
		PositionBB position = b.getPositionBB();
		result = boardPerformance(position);
		if(!result) {
			System.err.println("move generator performs too bad with knights.");
			fail(); // nps < NPSLIMIT
		}
		System.out.println();
		
		System.out.println("performance bishops: ");
		fenstring = "8/8/2b2b2/8/8/2B2B2/8/8 w - - 0 1";
		b = new Board(new PositionBB(new Fen(fenstring)));
		position = b.getPositionBB();
		result = boardPerformance(position);
		if(!result) {
			System.err.println("move generator performs too bad with bishops.");
			fail(); // nps < NPSLIMIT
		}
		System.out.println();
		
		System.out.println("performance rooks: ");
		fenstring = "8/8/2r2r2/8/8/2R2R2/8/8 w - - 0 1";
		b = new Board(new PositionBB(new Fen(fenstring)));
		position = b.getPositionBB();
		result = boardPerformance(position);
		if(!result) {
			System.err.println("move generator performs too bad with rooks.");
			fail(); // nps < NPSLIMIT
		}
		System.out.println();
		
		System.out.println("performance pawn: ");
		fenstring = "8/3p1p2/2p1p1p1/8/2P1P3/1P1P1P2/8/8 w - - 0 1";
		b = new Board(new PositionBB(new Fen(fenstring)));
		position = b.getPositionBB();
		result = boardPerformance(position);
		if(!result) {
			System.err.println("move generator performs too bad with pawn.");
			fail(); // nps < NPSLIMIT
		}
		System.out.println();
	}
	
	@Test
	public void testMixedPieceMoves() {
		String fenstring; Board b; PositionBB position; boolean result;
		System.out.println("performance bishops and knights: ");
		fenstring = "8/8/5n2/2b5/5B2/2N5/8/8 w - - 0 1";
		b = new Board(new PositionBB(new Fen(fenstring)));
		position = b.getPositionBB();
		result = boardPerformance(position);
		if(!result) {
			System.err.println("move generator performs too bad with pawn.");
			fail(); // nps < NPSLIMIT
		}
		System.out.println();
	}
	
	static final int NPSLIMIT = 200000;
	
	private boolean boardPerformance(PositionBB position) {
		for(int d = 1; d < 6; d++) {
			int depth = d;
			long expected = -1L; //TODO implement

			final long start = System.currentTimeMillis();
			long nodes = perft(position, depth);
			long time = System.currentTimeMillis() - start;
			System.out.print("nodes: " + nodes + " (" + expected + ")");
			//System.out.print(" on position " + );
			System.out.print(" at depth: " + depth + " in ");
			System.out.print(time + " ms, ");
			if(time == 0) time = 1;
			long nps = (long) ((double) nodes / (double) time * 1000d);
			System.out.println(nps + " nps");
			//System.out.println("speed: " + nodes/Math.round(((double)time / 1000d)) + " nps");
			//assertEquals(expected, nodes);	
			if(depth > 3 && nps < NPSLIMIT) {
				return false;
			}
		}
		return true;
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

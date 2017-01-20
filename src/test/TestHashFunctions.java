package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

import board.Board;
import board.Move;
import board.position.Position12x10;
import board.position.bitboard.PositionBB;
import board.position.bitboard.UndoInfo;
import search.Node;
import search.algorithms.AlphaBetaHashSearch;
import search.algorithms.AlphaBetaSearchInt;
import search.datastructures.Pair;
import search.evalfunctions.ScoreBitBoard;
import search.evalfunctions.ScoreBoard;
import search.functions.BoardFunction;
import search.hashfunctions.ZobristHash;
import search.hashtables.HashTableEntry;
import search.hashtables.MainTranspositionTable;
import search.nodes.BoardNode;

public class TestHashFunctions {
	@Test
	public void testZobristHash() {
		List<Long> hashes = new ArrayList<Long>();
		ZobristHash.init();
		
		// Hash 0: create startposition
		Position12x10 pos = new Position12x10();
		pos.printPieceBoard();
		long hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash " + (hashes.size() - 1) + ": " + hash);
		System.out.println();

		// Hash 1: move white pawn
		pos.movePiece(82, 62);
		pos.printPieceBoard();
		hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash " + (hashes.size() - 1) + ": " + hash);
		System.out.println();
		
		// Hash 2: recreate startposition (should the same as Hash 0)
		pos = new Position12x10();
		pos.printPieceBoard();
		hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash " + (hashes.size() - 1) + ": " + hash);
		System.out.println();
		
		// Hash 3: move Q and P
		pos.movePiece(84, 64);
		pos.movePiece(94, 74);
		pos.printPieceBoard();
		hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash " + (hashes.size() - 1) + ": " + hash);
		System.out.println();
		
		// Hash 4: move Q back
		pos.movePiece(74, 94);
		pos.printPieceBoard();
		hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash " + (hashes.size() - 1) + ": " + hash);
		System.out.println();
		
		// Hash 0 and Hash 2 should be the same (startposition)
		assertEquals(hashes.get(0), hashes.get(2));
		
		// these hashes should not be the same
		assertFalse(hashes.get(1).equals(hashes.get(3)));
		assertFalse(hashes.get(1).equals(hashes.get(4)));
		
		assertFalse(hashes.get(3).equals(hashes.get(0)));
		assertFalse(hashes.get(3).equals(hashes.get(4)));
		
		assertFalse(hashes.get(4).equals(hashes.get(0)));
		assertFalse(hashes.get(4).equals(hashes.get(1)));
		assertFalse(hashes.get(4).equals(hashes.get(2)));
		assertFalse(hashes.get(4).equals(hashes.get(3)));
		
	}
	
	@Test
	public void testZobristHashBB() {
		List<Long> hashes = new ArrayList<Long>();
		ZobristHash.init();
		
		// Hash 0: create startposition
		PositionBB pos = new PositionBB();
		System.out.println(pos);
		
		hashes = new ArrayList<Long>();
		
		List<Move> possible = pos.getPossibleMoves();
		for(Move m : possible) {
			UndoInfo ui = new UndoInfo();
			pos.makeMove(m, ui);
			long hash = ZobristHash.hash(pos);
			System.out.print(pos);
			System.out.println(hash);
			System.out.println();
			assertFalse(hashes.contains(hash));
			hashes.add(hash);
			pos.unMakeMove(m, ui);
		}
		
		System.out.println(hashes);

		
		/*// Hash 0 and Hash 2 should be the same (startposition)
		assertEquals(hashes.get(0), hashes.get(2));
		
		// these hashes should not be the same
		assertFalse(hashes.get(1).equals(hashes.get(3)));
		assertFalse(hashes.get(1).equals(hashes.get(4)));
		
		assertFalse(hashes.get(3).equals(hashes.get(0)));
		assertFalse(hashes.get(3).equals(hashes.get(4)));
		
		assertFalse(hashes.get(4).equals(hashes.get(0)));
		assertFalse(hashes.get(4).equals(hashes.get(1)));
		assertFalse(hashes.get(4).equals(hashes.get(2)));
		assertFalse(hashes.get(4).equals(hashes.get(3)));
		*/
	}

	@Test
	public void testHashTableEfficiency() {
		int depth = 4;
		
		PositionBB board = new PositionBB();
		MainTranspositionTable hashmap = new MainTranspositionTable();
		ZobristHash.init(); // init hashing function
		
		// AlphaBeta Search 
		ScoreBoard scoreboard = new ScoreBitBoard(board.copy());
		Function<Node, Integer> evalFunction = new BoardFunction(scoreboard);
		
		List<Long> times = new ArrayList<Long>();
		
		for(int i = 0; i < 5; i++) {
			long starttime = System.currentTimeMillis();
			MainTranspositionTable.PROBE = 0;
			MainTranspositionTable.READ = 0;
			//MainTranspositionTable.WRITE = 0;
			AlphaBetaSearchInt alphabeta = new AlphaBetaHashSearch(depth, hashmap, starttime);
			
			Pair<Node, Integer> result = null;
			alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
			result = alphabeta.search(
					new BoardNode(board.copy()),
					evalFunction);	
			long time = System.currentTimeMillis() - starttime;
			times.add(time);
			System.out.println("hashmap size: " + hashmap.size());
			System.out.println("search time: " + time + " ms");
			System.out.println("probe: " + MainTranspositionTable.PROBE);
			System.out.println("read: " + MainTranspositionTable.READ);
			System.out.println("write: " + MainTranspositionTable.WRITE);
			System.out.println();
		}
		
		long average = 0;
		for(int i = 1; i < times.size(); i++) {
			average += times.get(i);
		}
		average /= (times.size() - 1);
		
		System.out.println("first search: " + times.get(0) + " ms , then " + average + " ms");
		assertTrue(average < times.get(0));
		
		/*for(Map.Entry<Long, HashTableEntry> entry: hashmap.entrySet()) {
			System.out.println(entry.hashCode());
		}*/
	}
}

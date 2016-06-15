package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import board.Position12x10;
import search.hashfunctions.ZobristHash;

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
}

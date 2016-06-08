package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import board.Position12x10;
import search.hashfunctions.*;

public class TestHashFunctions {
	@Test
	public void testZobristHash() {
		List<Integer> hashes = new ArrayList<Integer>();
		ZobristHash.init();
		
		Position12x10 pos = new Position12x10();
		pos.printPieceBoard();
		int hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash: " + hash);

		pos.movePiece(82, 62);
		pos.printPieceBoard();
		hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash: " + hash);
		
		pos = new Position12x10();
		pos.printPieceBoard();
		hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash: " + hash);
		
		pos.movePiece(84, 64);
		pos.movePiece(94, 74);
		pos.printPieceBoard();
		hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash: " + hash);
		
		pos.movePiece(74, 94);
		pos.printPieceBoard();
		hash = ZobristHash.hash(pos);
		hashes.add(hash);
		System.out.println("hash: " + hash);
		
		assertEquals(hashes.get(0), hashes.get(2));
		assertEquals(hashes.get(3), hashes.get(4));
	}
}

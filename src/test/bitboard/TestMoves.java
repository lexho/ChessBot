package test.bitboard;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import board.Board;
import board.Move;
import board.position.PositionInterface;
import board.position.bitboard.PositionBB;

public class TestMoves {
	@Test
	public void testMoves() {
		PositionBB pos = new PositionBB();
		Board board = new Board(pos);
		List<Move> possible = board.getPossibleMoves();
		
		// startpos
		board.print();
		System.out.println(possible);
		
		// position 1
		System.out.println("position 1");
		board.makeMove(new Move("d2d4"));
		board.makeMove(new Move("d7d5"));
		board.print();
		possible = board.getPossibleMoves();
		System.out.println(possible);
		
		String[] expected;
		
		// pawn move
		System.out.println("Pawn Moves: ");
		containsNot(possible, "d2d3");
		System.out.println();
		
		/* Queen Moves */
		System.out.println("Queen Moves: ");
		expected = new String[]{"d1d2", "d1d3"};
		for(String move : expected) {
			contains(possible, move);
		}
		System.out.println();
		
		/* Bishop Moves */
		System.out.println("Bishop Moves: ");
		expected = new String[]{"c1d2", "c1e3", "c1f4", "c1g5", "c1h6"};
		for(String move : expected) {
			contains(possible, move);
		}
		System.out.println();
		
		/* King Moves */
		System.out.println("King Moves: ");
		contains(possible, "e1d2");
		System.out.println();
		
		/* Knight Moves */
		System.out.println("Knight Moves: ");
		expected = new String[]{"b1a3", "b1c3", "g1f3", "g1h3"};
		for(String move : expected) {
			contains(possible, move);
		}
		expected = new String[]{"b1h3", "b1f3", "g1a3", "g1c3"};
		for(String move : expected) {
			containsNot(possible, move);
		}
		System.out.println();
		
		board.makeMove(new Move("b1c3"));
		board.print();
		possible = board.getPossibleMoves();
		System.out.println(possible);
	}
	
	/** test if possible contains the expected move */
	public static void contains(List<Move> possible, String expected) {
		boolean containsMove = possible.contains(new Move(expected));
		String result = containsMove ? "ok" : "fail";
		System.out.println("contains move " + expected + " " + result);
		assertTrue(containsMove);
	}
	
	/** test if possible does not contain the expected move */
	public static void containsNot(List<Move> possible, String expected) {
		boolean containsMove = possible.contains(new Move(expected));
		String result = !containsMove ? "ok" : "fail";
		System.out.println("!contain move " + expected + " " + result);
		assertFalse(containsMove);
	}
}

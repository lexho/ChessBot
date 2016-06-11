package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import board.Board;
import board.Move;
import board.position.Position12x10;
import engine.ChessBot;

public class TestCastling {
	
	@Test
	public void testCastlingMoves() {

		
		Move[] moves = {new Move("e2e3"), new Move("b8c6"), new Move("d1f3"), new Move("c6d4"), new Move("e3d4"), new Move("a8b8"), new Move("b1c3"), new Move("b8a8"), new Move("f1c4"), new Move("g8h6"), new Move("d2d3"), new Move("f7f6"), new Move("f3h5"), new Move("g7g6"), new Move("h5h3"), new Move("h6f5"), new Move("h3g4"), new Move("f5e3"), new Move("f2e3"), new Move("f8h6"), new Move("g4h4"), new Move("h6e3")};
		Board b = new Board(new Position12x10());
		for(Move m : moves) {
			b.makeMove(m);
		}
		b.print();
		ChessBot.NR_OF_THREADS = 1;
		Board.DEBUG = true;
		List<Move> possible = b.getPossibleMoves();
		System.out.println(possible);
		assertFalse(Move.contains(possible, new Move("e8e7")));
	}
	
	@Test
	public void testDisableCastling() {
		
		/* Move rooks */
		Move[] moves = {new Move("e2e3"), new Move("h7h5"), new Move("b1c3")};
		Board b = new Board(new Position12x10());
		for(Move m : moves) {
			b.makeMove(m);
		}
		b.print();
		
		boolean[] castling = b.getPosition().getCastling();
		boolean[] expected = new boolean[]{true,true,true,true};
		for(int i = 0; i < castling.length; i++) {
			assertEquals(expected[i], castling[i]);
		}
		b.makeMove(new Move("h8h6"));
		expected = new boolean[]{true,true,false,true};
		for(int i = 0; i < castling.length; i++) {
			assertEquals(expected[i], castling[i]);
		}
		b.makeMove(new Move("a2a4"));
		b.makeMove(new Move("a7a5"));
		b.makeMove(new Move("a1a3"));
		expected = new boolean[]{true,false,false,true};
		for(int i = 0; i < castling.length; i++) {
			assertEquals(expected[i], castling[i]);
		}
		b.makeMove(new Move("a8a7"));
		expected = new boolean[]{true,false,false,false};
		for(int i = 0; i < castling.length; i++) {
			assertEquals(expected[i], castling[i]);
		}
		b.makeMove(new Move("e1e2"));
		expected = new boolean[]{false,false,false,false};
		for(int i = 0; i < castling.length; i++) {
			assertEquals(expected[i], castling[i]);
		}
		
		/* Move king */
		b = new Board(new Position12x10());
		castling = b.getPosition().getCastling();
		b.makeMove(new Move("e2e4"));
		b.makeMove(new Move("e7e6"));
		b.makeMove(new Move("e1e2"));
		expected = new boolean[]{false, false, true, true};
		for(int i = 0; i < castling.length; i++) {
			assertEquals(expected[i], castling[i]);
		}
		b.makeMove(new Move("e8e7"));
		expected = new boolean[]{false, false, false, false};
		for(int i = 0; i < castling.length; i++) {
			assertEquals(expected[i], castling[i]);
		}
		
	}
}

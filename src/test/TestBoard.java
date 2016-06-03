package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import board.Board;
import board.Move;
import board.Position;
import board.Position12x10;
import board.position.Fen;
import engine.ChessBot;

public class TestBoard {

	
	@Test
	public void testNewBoard() {
		Board b1 = new Board(new Position12x10());
		Board b2 = new Board(b1);
		//assertEquals(b1,b2);
		assertEquals(b1.getActiveColor(), 'w');
		assertEquals(b1.getMoveNr(), 0);
	}
	
	@Test
	public void testPositionByFen() {
		Board b = new Board(new Position12x10(new Fen("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR b KQkq - 0 1")));
		b.print();
		System.out.println(b.getActiveColor());
		assertEquals(b.getActiveColor(), 'b');
	}
	
	@Test
	public void testPositionByMove() {
		Board b = new Board(new Position12x10());
		ChessBot.NR_OF_THREADS = 1;
		b.makeMove(new Move("d2d4"));
		b.makeMove(new Move("e7e5"));
		b.print();
		List<Move> possibleMoves = b.getPossibleMoves();
		System.out.println(b.getPossibleMoves().toString());
		System.out.println(b.getActiveColor());

		/* Pawn moves */
		System.out.print("Pawn moves");
		assertTrue(Move.contains(possibleMoves, new Move("a2a4")));
		assertTrue(Move.contains(possibleMoves, new Move("b2b3")));
		assertTrue(Move.contains(possibleMoves, new Move("b2b4")));
		assertTrue(Move.contains(possibleMoves, new Move("d4d5")));
		System.out.println(" checked");
		
		/* Queen moves */
		System.out.print("Queen moves");
		assertTrue(Move.contains(possibleMoves, new Move("d1d2")));
		assertTrue(Move.contains(possibleMoves, new Move("d1d3")));
		assertTrue(!Move.contains(possibleMoves, new Move("d1d4")));
		System.out.println(" checked");
		
		/* Bishop moves */
		System.out.print("Bishop moves");
		assertTrue(Move.contains(possibleMoves, new Move("c1d2")));
		assertTrue(Move.contains(possibleMoves, new Move("c1e3")));
		assertTrue(Move.contains(possibleMoves, new Move("c1h6")));
		assertTrue(!Move.contains(possibleMoves, new Move("f1e2")));
		assertTrue(!Move.contains(possibleMoves, new Move("f1g2")));
		System.out.println(" checked");
		
		//assertEquals(b.getActiveColor(), 'b');
	}

	@Test
	public void testPromotion() {
		ChessBot.NR_OF_THREADS = 1;
		Board b = new Board(new Position12x10(new Fen("5k2/3P1p2/8/8/8/8/4P3/4K3 w - - 0 1")));
		b.print();
		b.makeMove(new Move("e7e8"));
		b.makeMove(new Move("g8g7"));
		//b.DEBUG = true;
		System.out.println("possible moves: " + b.getPossibleMoves());
		assertTrue(Move.contains(b.getPossibleMoves(), new Move("e8g6")));
		assertTrue(Move.contains(b.getPossibleMoves(), new Move("e8e1")));
	}
}

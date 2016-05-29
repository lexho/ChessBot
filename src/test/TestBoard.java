package test;

import static org.junit.Assert.assertEquals;
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
		assertTrue(contains(possibleMoves, new Move("a2a4")));
		assertTrue(contains(possibleMoves, new Move("b2b3")));
		assertTrue(contains(possibleMoves, new Move("b2b4")));
		assertTrue(contains(possibleMoves, new Move("d4d5")));
		System.out.println(" checked");
		
		/* Queen moves */
		System.out.print("Queen moves");
		assertTrue(contains(possibleMoves, new Move("d1d2")));
		assertTrue(contains(possibleMoves, new Move("d1d3")));
		assertTrue(!contains(possibleMoves, new Move("d1d4")));
		System.out.println(" checked");
		
		/* Bishop moves */
		System.out.print("Bishop moves");
		assertTrue(contains(possibleMoves, new Move("c1d2")));
		assertTrue(contains(possibleMoves, new Move("c1e3")));
		assertTrue(contains(possibleMoves, new Move("c1h6")));
		assertTrue(!contains(possibleMoves, new Move("f1e2")));
		assertTrue(!contains(possibleMoves, new Move("f1g2")));
		System.out.println(" checked");
		
		//assertEquals(b.getActiveColor(), 'b');
	}
	
	/** Does the moveList contain the Move move? 
	 * @return contains move */
	private boolean contains(List<Move> moveList, Move move) {
		for(Move m : moveList) {
			if(m.toString().equals(move.toString())) {
				return true;
			}
		}
		return false;
	}
}

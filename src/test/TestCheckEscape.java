package test;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Test;

import board.Board;
import board.Move;
import board.Position12x10;
import engine.ChessBot;

public class TestCheckEscape {
	@Test
	public void testEscape() {
		Move[] moves = {new Move("e2e3"), new Move("d7d5"), new Move("d1f3"), new Move("e7e6"), new Move("b1c3"), new Move("a7a6"), new Move("f3g4"), new Move("g8f6"), new Move("g4g5"), new Move("h7h6"), new Move("g5h4"), new Move("f8d6"), new Move("h2h3"), new Move("b8c6"), new Move("g1f3"), new Move("e6e5"), new Move("c3d5"), new Move("f6d5"), new Move("h4e4"), new Move("d6c5"), new Move("f3e5"), new Move("c6e5"), new Move("e4e5")};
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
}

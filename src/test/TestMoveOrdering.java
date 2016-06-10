package test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import board.Board;
import board.Move;
import board.Position12x10;
import engine.ChessBot;
import search.evalfunctions.MoveComparator;

public class TestMoveOrdering {

	@Test
	public void testMVV_LVA() {
		Board board = new Board(new Position12x10());
		board.makeMove(new Move("e2e4"));
		board.makeMove(new Move("d7e5"));
		board.makeMove(new Move("f1b5"));
		board.makeMove(new Move("b7b6"));
		ChessBot.NR_OF_THREADS = 4;
		List<Move> possible = board.getPossibleMoves();
		System.out.println(possible);
		possible.sort(new MoveComparator(board.getPosition12x10().get12x10Board()));
		System.out.println(possible);
		assertEquals(possible.get(0).toString(), "b5e8");
	}
}

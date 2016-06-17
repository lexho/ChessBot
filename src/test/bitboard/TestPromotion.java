package test.bitboard;

import java.util.List;

import org.junit.Test;

import board.Board;
import board.Move;
import board.position.Fen;
import board.position.bitboard.PositionBB;

public class TestPromotion {
	@Test
	public void testPromotion() {
		Board board = new Board(new PositionBB(new Fen("4k3/2P5/8/8/8/8/1p6/4K3 w - - 0 1")));
		board.print();
		List<Move> possible = board.getPossibleMoves();
		System.out.println(possible);
		String[] expected = new String[]{"c7c8q", "c7c8r", "c7c8b", "c7c8n"};
		for(String move : expected) {
			TestMoves.contains(possible, move);
		}
	}

}

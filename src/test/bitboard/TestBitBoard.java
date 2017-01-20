package test.bitboard;

import org.junit.Test;

import board.Move;
import board.pieces.Piece;
import board.position.bitboard.PositionBB;

public class TestBitBoard {
	@Test
	public void testFlipping() {
		PositionBB pos = new PositionBB();
		pos.makeMove(new Move("e7e6"));
		System.out.println(pos);
		pos.flip();
		pos.swapPieceColors();
		System.out.println(pos);
	}
}

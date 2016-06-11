package test;

import org.junit.Test;

import board.position.PositionBB;

public class TestBitBoards {
	@Test
	public void testBitBoards() {
		PositionBB pos = new PositionBB();
		pos.createStartpos();
		System.out.println(pos.toString());
	}
}

package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import board.Board;
import board.Position;
import position.Fen;

public class TestBoard {

	
	@Test
	public void testNewBoard() {
		Board b1 = new Board(new Position());
		Board b2 = new Board(b1);
		//assertEquals(b1,b2);
		assertEquals(b1.getActiveColor(), 'w');
		assertEquals(b1.getMoveNr(), 0);
	}
	
	@Test
	public void testPositionByFen() {
		Board b = new Board(new Position(new Fen("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR b KQkq - 0 1")));
		b.print();
		System.out.println(b.getActiveColor());
		assertEquals(b.getActiveColor(), 'b');
	}
}

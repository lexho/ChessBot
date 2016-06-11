package test;

import org.junit.Test;

import board.Board;
import board.Move;
import board.position.Position12x10;
import exceptions.InvalidIndexException;
import exceptions.InvalidMoveException;

public class TestExceptionHandling {
	
	@Test(expected = InvalidIndexException.class)
	public void TestsetPieceAt() {
		Position12x10 pos = new Position12x10();
		pos.setPieceAt('p', 200);
	}
	
	@Test(expected = InvalidIndexException.class)
	public void TestisValid() {
		Position12x10 pos = new Position12x10();
		pos.isValid(200);
	}
	
	@Test(expected = InvalidIndexException.class)
	public void Testclear() {
		Position12x10 pos = new Position12x10();
		pos.clear(121);
	}
	
	@Test(expected = InvalidMoveException.class)
	public void TestmakeMove() {
		Board board = new Board(new Position12x10());
		board.makeMove(new Move("a8a9"));
	}
}

package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import board.Board;
import board.Position12x10;
import engine.ChessBot;

public class TestPosition12x10 {
	
	@Test
	public void testPosition() {
		Position12x10 pos = new Position12x10();

		/* Compare output string with expected output */
		String output = new String();
		for(int y = 7; y >= 0; y--) {
			for(int x = 0; x < 8; x++) {
				output += (pos.getPieceAt(x, y));
			}
		}
		System.out.println(output);
		
		String expected = "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR";
		assertEquals(output, expected);
	}
	
	@Test
	public void testPossibleMoves() {
		ChessBot.NR_OF_THREADS = 4; // set number of threads
		Board board = new Board(new Position12x10());
		board.getPossibleMoves();
		System.out.println(board.getPossibleMoves());
	}
}

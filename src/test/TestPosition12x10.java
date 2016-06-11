package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import board.Board;
import board.position.Position12x10;
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
	
	@Test
	public void testIndexConversion() {
		List<int[]> coords = new ArrayList<int[]>();
		
		/* create coordinates */
		for(int y = 0; y < 8; y++) {
			for(int x = 0; x < 8; x++) {
				coords.add(new int[]{x,y});
			}
		}
		
		for(int[] coord : coords) {
			int index = Position12x10.coordToIndex(coord);
			int[] newCoord = Position12x10.indexToCoord(index);
			System.out.print(coord[0] + "/" + coord[1] + " --> " + index);
			System.out.println(" --> " + newCoord[0] + "/" + newCoord[1]);
		}
	}
}

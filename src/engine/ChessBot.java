package engine;

import board.Board;
import board.Position;

public class ChessBot {

	public ChessBot(Position position) {
		System.out.println("Hello from ChessBot!");
		System.out.println();
		System.out.println(position.toString());
		board = new Board(position); // Create new (internal) Board by position argument
	}
	
	public String getNextMove() {
		return "";
	}
	
	private Board board;

}

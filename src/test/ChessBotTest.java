package test;

import board.Position;
import engine.ChessBot;

public class ChessBotTest {

	
	public static void main(String[] args) {
		System.out.println();
		System.out.println("+++ ChessBotTest +++");
		System.out.println();
		
		System.out.println("create initial Position...");
		Position position = new Position();
		System.out.println("create new ChessBot...");
		ChessBot chessBot = new ChessBot(position);
		System.out.println("get next move...");
		String nextMove = chessBot.getNextMove();
		System.out.println(nextMove);
	}

}

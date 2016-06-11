package test;

import java.util.Scanner;

import board.Board;
import board.Move;
import board.position.Position12x10;
import engine.ChessBot;

public class ChessBotTest {

	
	public static void main(String[] args) {
		System.out.println();
		System.out.println("+++ ChessBotTest +++");
		System.out.println();
		
		System.out.println("create initial Position...");
		Position12x10 position = new Position12x10();
		Board board = new Board(position); // Create new (internal) Board by position argument
		
		Scanner terminalInput = new Scanner(System.in);
		
		System.out.println("create new ChessBot...");
		
		while(true) {
			System.out.println();
			System.out.println(board.toString());
			System.out.print("Your move: ");
			String move = terminalInput.nextLine(); // read user input
			board.makeMove(new Move(move));
			
			ChessBot chessBot = new ChessBot(board.getPosition());
			String nextMove = chessBot.getNextMove();
			System.out.println(nextMove);
			board.makeMove(new Move(nextMove));
		}


	}

}

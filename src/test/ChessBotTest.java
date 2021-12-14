package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Scanner;

import org.junit.Test;

import board.Board;
import board.Move;
import board.position.Position12x10;
import engine.ChessBot;
import search.algorithms.integer.AlphaBetaSearchInt;

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
	
	
	@Test
	public void testIterativeDeepeningTime() {
		ChessBot bot = new ChessBot();
		//bot.setWtime(3000);
		//bot.getNextMove();
		
		String m = "0000";
		int time = 0;
		while(bot.wasNullMove()) {
			time += 1000; // time 1000ms with 50 remaining moves, 1000ms/50 => 20 ms/move
			bot.setWtime(time);
			m = bot.getNextMove();
			System.out.println(m);
			System.out.println(); //TODO how to deal with null nodes
		}
		//System.out.println("getNextMove succeeded after " + time + "ms");
		System.out.println("getNextMove needs " + time + "ms to return usable results");
	}
	
	@Test
	public void testIterativeDeepeningFallback() {
		ChessBot bot = new ChessBot();
		//bot.setWtime(3000);
		//bot.getNextMove();
		
		String m = "0000";
		int time = 5000;
		while(time < 10000) {
			time += 1000; // time 1000ms with 50 remaining moves, 1000ms/50 => 20 ms/move
			bot.setWtime(time);
			m = bot.getNextMove();
			System.out.println(m);
			System.out.println("result state: " + bot.alphabeta.getResultState());
			if(bot.alphabeta.getResultState() != 0) System.exit(-1);
			System.out.println(); //TODO how to deal with null nodes
		}
	}
	
	/** even with very little time left the bot should return good results */
	@Test
	public void testIterativeDeepeningTimeLimit() {
		ChessBot bot = new ChessBot();
		//bot.setWtime(3000);
		//bot.getNextMove();
		
		String m = "0000";
		int time = 0;
		//while(m == "0000") {
			time += 5000; // time 500ms with 50 remaining moves, 500ms/50 => 10 ms/move
			bot.setWtime(time);
			m = bot.getNextMove();
			System.out.println("getNextMove() returns: " + m);
			System.out.println(); //TODO how to deal with null nodes
		//}
			System.out.println("remaining moves: " + AlphaBetaSearchInt.approxRemainingNrOfMoves);
		System.out.println("game time limit: " + time + "ms");
		System.out.println("move limit: " + time / AlphaBetaSearchInt.approxRemainingNrOfMoves + "ms/move");
		if(bot.wasNullMove()) System.err.println("search failed, result is nullmove");
		assertFalse(bot.wasNullMove());
	}
}

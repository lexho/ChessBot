package test;

import java.util.Scanner;

import org.junit.Test;

import board.NullMove;
import engine.ChessBot;

public class TestEngineMatch {
	@Test
	public void testEngineMatch() {
		ChessBot botWhite = new ChessBot();
		ChessBot botBlack = new ChessBot();
		
		String move;
		ChessBot activePlayer = botWhite;
		ChessBot unActivePlayer = botBlack;
		
		Scanner terminalInput = new Scanner(System.in);
		do {
			move = activePlayer.getNextMove();
			activePlayer.makeMove(move);
			unActivePlayer.makeMove(move);
			
			System.out.println("next move: " + move);
			System.out.println();
			activePlayer.printBoard();
			
			/* Switch active player */
			ChessBot tmp = activePlayer;
			activePlayer = unActivePlayer;
			unActivePlayer = tmp;
			
			System.out.print("Press Enter for next move: ");
			terminalInput.nextLine(); // read user input
		} while(move != new NullMove().toString());
	}
}

package test;

import java.io.PrintWriter;
import java.util.Scanner;

import board.Board;
import board.Move;
import board.position.bitboard.PositionBB;
import commands.Command;
import engine.ChessBot;

public class DebugBoard {
	
	private static Board board;
	
	public static void main(String[] args) {
		System.out.println("ChessBot :: Board-Debug Tool");
		board = new Board(new PositionBB());
		
		Scanner terminalInput = new Scanner(System.in);
		try {
			//cmdlog = new PrintWriter("/home/alex/Code/java/ChessBot/log/cmds.txt", "UTF-8");
			
			//System.setOut(new PrintStream(new File("/home/alex/Code/java/ChessBot/log/output.txt")));
			//System.setErr(new PrintStream(new File("/home/alex/Code/java/ChessBot/log/error.txt")));
			//bot = new ChessBot();
		
			/* Wait for commands */
			//current = new Command(); // dummy command for interrupts
			//Command.setBot(bot); // give command class a bot to pass the commands to
			while(true) {
				System.out.print("move: ");
				String command = terminalInput.nextLine(); // read user input
				//cmdlog.println(command);
				//cmdlog.flush();
				parseCommand(command);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void parseCommand(String cmd) {
		switch(cmd) {
		case "possible":
			System.out.println(board.getPossibleMoves());
			break;
		default: // make a move
			board.makeMove(new Move(cmd));
			board.print();
			break;
		}
	}
}

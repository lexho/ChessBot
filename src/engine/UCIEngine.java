package engine;

import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import exceptions.InvalidMoveException;
import util.StringUtils;

public class UCIEngine {
	static ChessBot bot;
	static PrintWriter cmdlog;
	
	// TODO remove this unused constructor in static context
	public UCIEngine() {
		
	}
	
	/**
	 * parse UCI commands from system standard input
	 * and apply them on ChessBot
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner terminalInput = new Scanner(System.in);
		try {
			cmdlog = new PrintWriter("/home/alex/Code/java/ChessBot/log/cmds.txt", "UTF-8");
			
			//System.setOut(new PrintStream(new File("/home/alex/Code/java/ChessBot/log/output.txt")));
			System.setErr(new PrintStream(new File("/home/alex/Code/java/ChessBot/log/error.txt")));
			bot = new ChessBot();
		
			/* Wait for commands */
			while(true) {
				String command = terminalInput.nextLine(); // read user input
				cmdlog.println(command);
				cmdlog.flush();
				parseCommand(command);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * parse command string and apply the appropriate action
	 * @param cmd UCI command to be parsed
	 */
	private static void parseCommand(String cmd) {
		
		int sep = cmd.indexOf(' '); // index of separator (first space-Character)
		
		String subcmd;
		if(sep == -1) subcmd = cmd;
		else subcmd = cmd.substring(0, sep);
		
		List<String> cmds = StringUtils.splitString(cmd, ' ');
		switch(cmds.get(0)) {
		case "uci":
			System.out.println("id name ChessBot");
			System.out.println("id author Alexander Hoertenhuber");
			System.out.println("uciok");
			break;
		case "debug":
			break;
		case "isready":
			System.out.println("readyok");
			break;
		case "set": // set option name
			break;
		case "register":
			break;
		case "ucinewgame":
			break;
		case "position":
			/*subcmd = cmd.substring(sep + 1);
			int end = subcmd.indexOf(' ');
			System.out.println(subcmd.substring(0, end));*/
			//System.out.println(cmds.get(1));
			if(cmds.get(1).equals("startpos")) {
				bot.init();
				//System.out.println("eq startpos");
				if(cmds.size() > 2)
					if(cmds.get(2).equals("moves")) {
						//System.out.println("eq moves");
						for(int i = 3; i < cmds.size(); i++) {
							try {
								bot.makeMove(cmds.get(i));
							} catch (InvalidMoveException e) {
								System.out.println(e.getMessage());
							}
						}
					}
			} else if(cmds.get(1).equals("fen")) {
				bot = new ChessBot(cmds.get(2));
			}
			break;
		case "go":
			String nextMove = bot.getNextMove();
			System.out.println("bestmove " + nextMove + " ponder");	
			break;
		case "stop":
			break;
		case "ponderhit":
			break;
		case "quit":
			System.exit(0);
			break;
		default:
			System.out.println("unknown command");
			break;
		}
	}
}

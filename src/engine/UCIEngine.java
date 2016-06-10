package engine;

import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import commands.Command;
import commands.GoCommand;
import exceptions.InvalidMoveException;
import util.StringUtils;

public class UCIEngine {
	static ChessBot bot;
	static PrintWriter cmdlog;
	static Command current;
	
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
			current = new Command(); // dummy command for interrupts
			Command.setBot(bot); // give command class a bot to pass the commands to
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
			if(cmds.get(1).equals("startpos")) {
				bot.reset();
				if(cmds.size() > 2)
					if(cmds.get(2).equals("moves")) {
						for(int i = 3; i < cmds.size(); i++) {
							try {
								bot.makeMove(cmds.get(i));
							} catch (InvalidMoveException e) {
								System.err.println(e.getMessage());
							}
						}
					}
			} else if(cmds.get(1).equals("fen")) {
				//int pos = cmds.get(0).length() + cmds.get(1).length();
				String fenstr = cmd.substring(13);
				bot = new ChessBot(fenstr);
				Command.setBot(bot);
			}
			break;
		case "go":
			current.interrupt();
			current = new GoCommand();
			current.start();
			break;
		case "stop":
			current.interrupt();
			break;
		case "ponderhit":
			break;
		case "print":
			bot.printBoard();
			break;
		case "possible":
			bot.printPossibleMoves();
			break;
		case "debug":
			bot.debugOnOff();
			break;
		case "quit":
			current.interrupt();
			System.exit(0);
			break;
		default:
			System.out.println("unknown command");
			break;
		}
	}
}

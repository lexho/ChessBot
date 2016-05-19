package engine;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import board.Move;
import exceptions.InvalidMoveException;

public class UCIEngine {
	static ChessBot bot;
	static PrintWriter log;
	
	// TODO remove this unused constructor in static context
	public UCIEngine() {
		
	}
	
	/**
	 * parse UCI commands from system standard input
	 * and apply them on ChessBot
	 * @param args
	 */
	public static void main(String[] args) {
		bot = new ChessBot();
		
		Scanner terminalInput = new Scanner(System.in);
		try {
			log = new PrintWriter("/home/alex/Code/java/ChessBot/log.txt", "UTF-8");
			
		} catch (FileNotFoundException e) {
			try {
				log = new PrintWriter("log.txt", "UTF-8");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		/* Wait for commands */
		while(true) {
			String command = terminalInput.nextLine(); // read user input
			log.println(command);
			log.flush();
			parseCommand(command);
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
		
		List<String> cmds = splitCommand(cmd);
		switch(cmds.get(0)) {
		case "uci":
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
	
	/**
	 * split a command string into single commands
	 * @param cmd the command string that should be splitted
	 * @return a list of string commands
	 */
	private static List<String> splitCommand(String cmd) {
		List<String> cmd_splitted= new ArrayList<String>();
		
		int e = cmd.indexOf(' ');
		
		while(e != -1) {
			cmd_splitted.add(cmd.substring(0, e));
			//System.out.println("add: " + cmd.substring(0, e));
			cmd = cmd.substring(e + 1);
			e = cmd.indexOf(' ');
		}
		
		/* Add last word */
		//System.out.println("add: " + cmd);
		cmd_splitted.add(cmd);
		
		return cmd_splitted;
	}

}

package commands;

import engine.ChessBot;

public class Command extends Thread {
	
	private static ChessBot bot;
	
	public Command() {

	}
	
	@Override
	public void run() {
		
	}

	public static ChessBot getBot() {
		return bot;
	}

	public static void setBot(ChessBot bot) {
		Command.bot = bot;
	}
	
}

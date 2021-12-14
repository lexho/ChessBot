package commands;

import engine.ChessBot;
import engine.integer.ChessBotInt;

public class CommandInt extends Thread {
	
	private static ChessBotInt bot;
	
	public CommandInt() {

	}
	
	@Override
	public void run() {
		
	}

	public static ChessBotInt getBot() {
		return bot;
	}

	public static void setBot(ChessBotInt bot) {
		CommandInt.bot = bot;
	}
	
	public static void stopBot() {
		bot.stop();
	}
	
}

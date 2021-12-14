package commands;

public class GoCommand extends Command {
	
	public void setWtime(int wtime) {
		getBot().setWtime(wtime);
	}
	
	@Override
	public void run() {
		getBot().stop(); // stop currently running processes
		String nextMove = getBot().getNextMove();
		System.out.println("bestmove " + nextMove + " ponder");	
	}
}

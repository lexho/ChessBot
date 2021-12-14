package commands;

public class GoCommandInt extends CommandInt {
	
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

package test;

import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import engine.ChessBot;
import util.NullPrinter;

public class TestSpeed {
	PrintStream out;
	
	@Test
	public void testAspirationWindowSpeed() {
		out = System.out;
		long starttime;
		long[] time = new long[2];

		/* Create Bot without Aspiration Windows */
		System.setOut(new PrintStream(NullPrinter.out));
		ChessBot bot1 = new ChessBot();
		bot1.useAspWindows(false);
		bot1.useHashTable(false);
		
		starttime = System.currentTimeMillis();
		bot1.getNextMove();
		time[0] = System.currentTimeMillis() - starttime;
		
		System.setOut(out);
		System.out.println("bot1 time: " + time[0]);
		
		/* Create Bot with Aspiration Windows */
		System.setOut(new PrintStream(NullPrinter.out));
		ChessBot bot2 = new ChessBot();
		
		starttime = System.currentTimeMillis();
		bot2.getNextMove();
		time[1] = System.currentTimeMillis() - starttime;
		
		System.setOut(out);
		System.out.println("bot2 time: " + time[1]);
		
		/* Compare search times of the bots */
		assertTrue(time[1] < time[0]);
	}
	
	@Test
	public void testHashTableSpeed() {
		out = System.out;
		long starttime;
		long[] time = new long[2];
		int depthLimit = 4;

		/* Create Bot without hashtable */
		System.setOut(new PrintStream(NullPrinter.out));
		ChessBot bot1 = new ChessBot();
		bot1.useAspWindows(false);
		bot1.useHashTable(false);
		bot1.setDepthLimit(depthLimit);
		List<String> moves = new ArrayList<String>();
		starttime = System.currentTimeMillis();
		
		String m;
		for(int i = 0; i < 5; i++) {
			m = bot1.getNextMove();
			moves.add(m);
			bot1.makeMove(m);
		}
		time[0] = System.currentTimeMillis() - starttime;
		
		System.setOut(out);
		System.out.println("bot1 time: " + time[0]);
		System.out.println(moves);
		
		/* Create Bot with hashtable */
		System.setOut(new PrintStream(NullPrinter.out));
		ChessBot bot2 = new ChessBot();
		bot2.useAspWindows(false);
		bot2.useHashTable(true);
		bot2.setDepthLimit(depthLimit);
		moves = new ArrayList<String>();
		starttime = System.currentTimeMillis();
		
		for(int i = 0; i < 5; i++) {
			m = bot2.getNextMove();
			moves.add(m);
			bot2.makeMove(m);
		}
		time[1] = System.currentTimeMillis() - starttime;
		
		System.setOut(out);
		System.out.println("bot2 time: " + time[1]);
		System.out.println(moves);
		
		/* Compare search times of the bots */
		assertTrue(time[1] < time[0]);
	}
}

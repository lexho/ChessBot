package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintStream;

import org.junit.Test;

import board.Move;
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

		/* Create Bot without hashtable */
		System.setOut(new PrintStream(NullPrinter.out));
		ChessBot bot1 = new ChessBot();
		bot1.useAspWindows(false);
		bot1.useHashTable(false);
		starttime = System.currentTimeMillis();
		
		String m;
		for(int i = 0; i < 6; i++) {
			m = bot1.getNextMove();
			bot1.makeMove(m);
		}
		time[0] = System.currentTimeMillis() - starttime;
		
		System.setOut(out);
		System.out.println("bot1 time: " + time[0]);
		
		/* Create Bot with hashtable */
		System.setOut(new PrintStream(NullPrinter.out));
		ChessBot bot2 = new ChessBot();
		bot1.useAspWindows(false);
		bot1.useHashTable(true);
		
		starttime = System.currentTimeMillis();
		for(int i = 0; i < 6; i++) {
			m = bot2.getNextMove();
			bot2.makeMove(m);
		}
		time[1] = System.currentTimeMillis() - starttime;
		
		System.setOut(out);
		System.out.println("bot2 time: " + time[1]);
		
		/* Compare search times of the bots */
		assertTrue(time[1] < time[0]);
	}
}

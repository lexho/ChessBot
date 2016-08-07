package test.search;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import engine.ChessBot;
import util.NullPrinter;

@RunWith(Parameterized.class)
public class TestSearch {
	
	@Parameters
	public static Collection<Object[]> generateParams() {
		List<Object[]> params = new ArrayList<Object[]>();
		
		for(int d = 1; d < 8; d++) {
			params.add(new Object[] {new Integer(d), new String("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - 0 2")});
		}

		return params;
	}
	
	private int depth;
	private String fenstr;
	
	public TestSearch(Integer d, String f) {
		depth = d;
		fenstr = f;
	}
	
	@Test
	public void testSearch() {
		// disable output stream
		PrintStream outputStream = System.out;
		System.setOut(NullPrinter.out);
		
		ChessBot bot;
		//ChessBot bot = new ChessBot();
		bot = new ChessBot(fenstr);
		bot.useAspWindows(false);
		bot.useHashTable(false);
		
		bot.stop(); // stop currently running processes
		bot.setDepthLimit(depth);

		String nextMove = bot.getNextMove();
		
		// enable output stream
		System.setOut(outputStream);
		System.out.println("depth " + depth + " bestmove " + nextMove + " " + bot.getScore());
	}
}

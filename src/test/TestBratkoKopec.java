package test;

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

import board.Board;
import board.position.Fen;
import board.position.bitboard.PositionBB;
import engine.ChessBot;
import util.StringUtils;

@RunWith(Parameterized.class)
public class TestBratkoKopec {
	
	@Parameters
	public static Collection<Object[]> generateParams() {
		List<Object[]> params = new ArrayList<Object[]>();
		try {
			List<String> positions = Files.readAllLines(
			Paths.get("assets/positions/bratko-kopec-test.txt"), StandardCharsets.UTF_8);
			
			for (String str : positions) {
				List<String> strs = StringUtils.splitString(str, "bm");
				String bestmove = strs.get(1).substring(1); // remove space character
				params.add(new Object[] {strs.get(0), bestmove });
			}
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return params;
	}
	
	String fenstr;
	String bestmove;
	
	public TestBratkoKopec(String fenstr, String bestmove) {
		this.fenstr = fenstr;
		this.bestmove = bestmove;
	}
	
	@Test
	public void testBratkoKopec() {
		System.out.println(fenstr);
		System.out.println("bestmove: " + bestmove);
		ChessBot bot = new ChessBot(fenstr);
		String bestmove_bot = bot.getNextMove();
		System.out.println("bestmove: " + bestmove_bot + " (expected " + bestmove + ")");
	}

}

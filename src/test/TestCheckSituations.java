package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import board.Move;
import board.MoveValidator;
import board.PieceList;
import board.Position;
import board.pieces.Piece;
import position.Fen;

@RunWith(Parameterized.class)
public class TestCheckSituations {

	@Parameters
	public static Collection<Object[]> generateParams() {
		List<Object[]> params = new ArrayList<Object[]>();
		try {
			List<String> positions = Files.readAllLines(
			Paths.get("assets/positions/check.txt"), StandardCharsets.UTF_8);
			for (int i = 0; i < positions.size(); i++) {
				params.add(new Object[] { new Integer(i) });
			}
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return params;
	}
	
	private String fenstring;
	private boolean inCheck;
	
	public TestCheckSituations(Integer i) {
		try {
			List<String> positions = Files.readAllLines(
				Paths.get("assets/positions/check.txt"), StandardCharsets.UTF_8);
			fenstring = positions.get(i);
			inCheck = true;
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testCheckSituations() {
		Board b = new Board(new Position(new Fen(fenstring)));
		b.print();
		if(inCheck) {
			Position pos = b.getPosition();
			List<Piece> opponentsPieces = pos.getPieces().getPieces(pos.getUnactiveColor());
			for(Piece p : opponentsPieces) {
				System.out.println(p + " " + p.getPossibleMoves());
				/*for(Move m : p.getPossibleMoves()) {
					/*if(MoveValidator.validate(pos, m)) 
					if(m.getTarget()[0] == king.getPosition()[0] && m.getTarget()[1] == king.getPosition()[1]) {
						
					}
				}*/
			}
			assertTrue(pos.isInCheck());
		} else {
			//assertFalse(b.getPosition().isInCheck());
		}
	}
}

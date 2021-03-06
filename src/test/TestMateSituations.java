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
import board.pieces.Piece;
import board.position.Fen;
import board.position.Position12x10;
import board.position.Position;

@RunWith(Parameterized.class)
public class TestMateSituations {

	@Parameters
	public static Collection<Object[]> generateParams() {
		List<Object[]> params = new ArrayList<Object[]>();
		try {
			List<String> positions = Files.readAllLines(
			Paths.get("assets/positions/mate.txt"), StandardCharsets.UTF_8);
			positions.addAll(Files.readAllLines(
					Paths.get("assets/positions/notMate.txt"), StandardCharsets.UTF_8));
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
	
	public TestMateSituations(Integer i) {
		try {
			List<String> positions = Files.readAllLines(
				Paths.get("assets/positions/mate.txt"), StandardCharsets.UTF_8);
			int sw = positions.size();
			positions.addAll(Files.readAllLines(
					Paths.get("assets/positions/notMate.txt"), StandardCharsets.UTF_8));
			fenstring = positions.get(i);
			if(i >= sw) inCheck = false; else inCheck = true;
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testCheckSituations() {
		Board b = new Board(new Position12x10(new Fen(fenstring)));
		b.print();
		if(inCheck) {
			Position pos = b.getPosition();
			List<Piece> opponentsPieces = pos.getPieces().getPieces(pos.getUnactiveColor());
			/*for(Piece p : opponentsPieces) {
				System.out.println(p + " " + p.getPossibleMoves());
				for(Move m : p.getPossibleMoves()) {
					System.out.print(m + " ");
					if(MoveValidator.validate(pos, m)) System.out.print("valid");
					else System.out.print("invalid");
					System.out.println();	
				}
			}*/
			//pos.printPieceLocationList();
			
			assertTrue(pos.isInCheck());
		} else {
			assertFalse(b.getPosition().isInCheck());
		}
	}
}

package test.board12x10;

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
import board.position.bitboard.PositionBB;

@RunWith(Parameterized.class)
public class TestCheckSituations {

	@Parameters
	public static Collection<Object[]> generateParams() {
		List<Object[]> params = new ArrayList<Object[]>();
		try {
			List<String> positions = Files.readAllLines(
			Paths.get("assets/positions/check.txt"), StandardCharsets.UTF_8);
			positions.addAll(Files.readAllLines(
					Paths.get("assets/positions/notCheck.txt"), StandardCharsets.UTF_8));
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
			int sw = positions.size();
			positions.addAll(Files.readAllLines(
					Paths.get("assets/positions/notCheck.txt"), StandardCharsets.UTF_8));
			fenstring = positions.get(i);
			if(i >= sw) inCheck = false; else inCheck = true;
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void testCheckSituations12x10() {
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
	
	@Test
	public void testCheckSituationsBB() {
		Board b = new Board(new PositionBB(new Fen(fenstring)));
		b.print();
		b.getPositionBB().whiteMove();
		Position pos = b.getPosition();
		if(pos.whiteMove()) { // white to move
			if(inCheck) assertTrue(pos.isInCheck(true)); // white is in check?
			else assertFalse(pos.isInCheck(true)); // white is not in check?
		} else {// black to move
			if(inCheck) assertTrue(pos.isInCheck(false)); // black is in check?
			else assertFalse(pos.isInCheck(false)); // black is not in check?
		}
	}
	
	@Test
	public void checkDetectionSpeed() {
		//String fenstring = "8/8/8/5k2/8/r2K4/8/8 w - - 0 1"; // white is in check
		Board b = new Board(new PositionBB(new Fen(fenstring)));
		b.print();
		b.getPositionBB().whiteMove();
		Position pos = b.getPosition();
		long start = System.currentTimeMillis();
		for(int i = 0; i < 100000; i++) {
			pos.isInCheck(true); // white is in check?
			pos.isInCheck(false); // black is in check?
		}
		long end = System.currentTimeMillis();
		long duration = end - start;
		System.out.println("start: " + start);
		System.out.println("end: " + end);
		System.out.println("duration: " + duration + "00000 iterations/s");
	}
}

package test.evalfunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import board.Board;
import board.Move;
import board.pieces.Piece;
import board.position.Fen;
import board.position.Position;
import board.position.bitboard.Movement;
import board.position.bitboard.PositionBB;
import search.datastructures.ScoreData;
import search.evalfunctions.ScoreBitBoard;
import search.evalfunctions.ScoreBitBoardDouble;
import search.evalfunctions.ScoreBoard;
import test.bitboard.TestMoves;
import util.StringUtils;

@RunWith(Parameterized.class)
public class TestEvalFunction {
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
	
	public TestEvalFunction(String fenstr, String bestmove) {
		this.fenstr = fenstr;
		this.bestmove = bestmove;
	}
	
	@Test
	public void testEvalFunction() {
		Board  board = new Board(new PositionBB());
		ScoreBitBoardDouble scoreboard = new ScoreBitBoardDouble(board.copy().getPositionBB());
		
		boolean firstRow = true;
		for(Move m : board.getPossibleMoves()) {
			Board execBoard = board.copy();
			execBoard.makeMove(m);
			Position pos = execBoard.getPositionBB();
			ScoreData scores = new ScoreData();
			scores.addLabel("Material");
			scores.addLabel("Mobility");
			scores.addLabel("Position");
			scores.add(scoreboard.scoreMaterial(pos));
			scores.add(scoreboard.scoreMobility(pos));
			scores.add(scoreboard.scorePosition(pos));
			//scoreboard.applyDetailed(execBoard);
			if(firstRow) {
				System.out.println("Move " + scores.getLabelRowString());
				firstRow = false;
			}
			System.out.print(m);
			System.out.println(scores);
			//System.out.println(m + " " + scores.get(0) + " " +  scores.get(1) + " " + scores.get(2));
			}
		
		
		/*Board board = new Board(new PositionBB(new Fen("4k3/2P5/8/8/8/8/1p6/4K3 w - - 0 1")));
		board.print();
		List<Move> possible = board.getPossibleMoves();
		System.out.println(possible);
		String[] expected = new String[]{"c7c8q", "c7c8r", "c7c8b", "c7c8n"};
		for(String move : expected) {
			TestMoves.contains(possible, move);
		}*/
	}
	
	@Test
	public void testMaterialScore() {
		String[] poslist = {"rnbqkbnr/pppppppp/8/8/8/8/1PPPPPPP/RNBQKBNR w KQkq - 0 2", 
				"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKB1R w KQkq - 0 2",
				"rn1qkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKB1R w KQkq - 0 2"};
		double[] expectedScores = {0 - Piece.PAWN_V, 0 - Piece.KNIGHT_V, 0 - Piece.KNIGHT_V + Piece.BISHOP_V};
		List<Double> results = new ArrayList<Double>();
		
		List<String> positions = new ArrayList<String>(Arrays.asList(poslist));
		for(int i = 0; i < positions.size(); i++) {
			PositionBB pos = new PositionBB(new Fen(positions.get(i)));
			System.out.println(pos);
			Board  board = new Board(pos);
			ScoreBitBoardDouble scoreboard = new ScoreBitBoardDouble(board.copy().getPositionBB());
			
			double mtrlScore = scoreboard.scoreMaterial(board.getPositionBB());
			assertEquals(mtrlScore, expectedScores[i]);
			results.add(mtrlScore);
			System.out.println(mtrlScore);
		}
		
		assertTrue(results.get(0) < 0); // position is bad for white
		assertTrue(results.get(1) < 0); // position is bad for white
		assertTrue(results.get(0) > results.get(1));
		assertTrue(results.get(2) > results.get(1));
	}
	
	@Test
	public void testMobilityScore() {
		//Board board = new Board(new PositionBB(new Fen("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2")));
		Board board = new Board(new PositionBB(new Fen(fenstr)));
		ScoreBitBoardDouble scoreboard = new ScoreBitBoardDouble(board.copy().getPositionBB());
		
		double mobScore = scoreboard.scoreMobility(board.getPositionBB());
		double expected = board.getPossibleMoves().size();
		board.getPositionBB().setWhiteMove(false);
		expected -= board.getPossibleMoves().size();
		double diff = Math.abs(expected - mobScore);
		
		System.out.print("expected: " + expected + ", ");
		System.out.print("function: " + mobScore + ", ");
		System.out.println("diff: " + diff);
		assertTrue(expected >= 0 && mobScore >= 0 );
		//assertEquals(expected, mobScore);
	}
}

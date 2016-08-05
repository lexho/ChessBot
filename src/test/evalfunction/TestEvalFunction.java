package test.evalfunction;

import java.util.List;

import org.junit.Test;

import board.Board;
import board.Move;
import board.position.Fen;
import board.position.bitboard.PositionBB;
import search.datastructures.ScoreData;
import search.evalfunctions.ScoreBitBoard;
import test.bitboard.TestMoves;

public class TestEvalFunction {
	@Test
	public void testEvalFunction() {
		Board  board = new Board(new PositionBB());
		ScoreBitBoard scoreboard = new ScoreBitBoard(board.copy());
		
		boolean firstRow = true;
		for(Move m : board.getPossibleMoves()) {
			Board execBoard = board.copy();
			execBoard.makeMove(m);
			ScoreData scores = new ScoreData();
			scores.addLabel("Material");
			scores.addLabel("Mobility");
			scores.addLabel("Position");
			scores.add(scoreboard.scoreMaterial(execBoard));
			scores.add(scoreboard.scoreMobility(execBoard));
			scores.add(scoreboard.scorePosition(execBoard));
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
}

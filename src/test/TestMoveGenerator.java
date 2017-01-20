package test;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import board.Move;
import board.position.bitboard.PositionBB;
import board.position.bitboard.UndoInfo;

public class TestMoveGenerator {
	@Test
	public void testPseudoLegalMoves() {
		PositionBB pos = new PositionBB();
		long start = System.currentTimeMillis();
		Random random = new Random(start);
		
		
		List<Move> pseudoLegal = null;
		long time = 0;
		int i = 0;
		while(time < 10000) {
			//pos = new PositionBB();
			//System.out.println(pos);
			pseudoLegal = pos.getPseudoLegalMoves();
			time = System.currentTimeMillis() - start;
			i++;
			UndoInfo ui = new UndoInfo();
			if(pseudoLegal.size() > 0) 
				pos.makeMove(pseudoLegal.get(random.nextInt(pseudoLegal.size())), ui);
			else 
				pos = new PositionBB();
		}
		System.out.println(pseudoLegal);
		
		
		System.out.print(pseudoLegal.size() + " moves in ");
		System.out.println(time + " ms");
		System.out.println("after " + i + " iterations.");
	}
}

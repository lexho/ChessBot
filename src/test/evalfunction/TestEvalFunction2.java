package test.evalfunction;

import static org.junit.Assert.*;

import org.junit.Test;

import board.position.Position;
import board.position.bitboard.Movement;
import board.position.bitboard.PositionBB;
import util.BitBoardUtils;

public class TestEvalFunction2 {
	
	@Test
	public void testCenterScore() {
		Position board = new PositionBB();
		long wBishopValid = Movement.whiteBishopsValid((PositionBB)board);
		long centerMask = 0b0000000000000000000000000001100000011000000000000000000000000000L;
		long sum = wBishopValid & centerMask;
		//System.out.println(Long.toBinaryString(sum));
		//System.out.println(BitBoardUtils.bitboardToString(sum, 'x'));
		System.out.println(BitBoardUtils.binaryToString(sum));
		assertEquals(0, sum);
		System.out.println("1-bits: " + Long.bitCount(sum));
		
		board = new PositionBB("rnbqkbnr/ppp2ppp/3pp3/8/8/1P1PP3/PBP2PPP/RN1QKBNR w KQkq - 0 1");
		wBishopValid = Movement.whiteBishopsValid((PositionBB)board);
		sum = wBishopValid & centerMask;
		//System.out.println(Long.toBinaryString(sum));
		//System.out.println(BitBoardUtils.bitboardToString(sum, 'x'));
		System.out.println(BitBoardUtils.binaryToString(sum));
		assertNotEquals(0, sum);
		System.out.println("1-bits: " + Long.bitCount(sum));
		
		String fenstr = "8/8/2b2b2/8/8/2B2B2/8/8 w - - 0 1";
		board = new PositionBB(fenstr);
		wBishopValid = Movement.whiteBishopsValid((PositionBB)board);
		sum = wBishopValid & centerMask;
		//System.out.println(Long.toBinaryString(sum));
		//System.out.println(BitBoardUtils.bitboardToString(sum, 'x'));
		System.out.println(BitBoardUtils.binaryToString(sum));
		assertNotEquals(0, sum);
		System.out.println("1-bits: " + Long.bitCount(sum));
		
		fenstr = "8/8/2N2N2/8/8/2N2N2/8/8 w - - 0 1";
		board = new PositionBB(fenstr);
		wBishopValid = Movement.whiteKnightsValid((PositionBB)board);
		sum = wBishopValid & centerMask;
		//System.out.println(Long.toBinaryString(sum));
		//System.out.println(BitBoardUtils.bitboardToString(sum, 'x'));
		System.out.println(BitBoardUtils.binaryToString(sum));
		assertNotEquals(0, sum);
		assertEquals(centerMask, sum);
		System.out.println("1-bits: " + Long.bitCount(sum));
		
		fenstr = "8/2N2N2/8/8/8/8/2N2N2/8 w - - 0 1";
		board = new PositionBB(fenstr);
		wBishopValid = Movement.whiteKnightsValid((PositionBB)board);
		sum = wBishopValid & centerMask;
		//System.out.println(Long.toBinaryString(sum));
		//System.out.println(BitBoardUtils.bitboardToString(sum, 'x'));
		System.out.println(BitBoardUtils.binaryToString(sum));
		assertNotEquals(0, sum);
		assertEquals(centerMask, sum);
		System.out.println("1-bits: " + Long.bitCount(sum));
		
		fenstr = "8/8/1N4N1/8/8/1N4N1/8/8 w - - 0 1";
		board = new PositionBB(fenstr);
		wBishopValid = Movement.whiteKnightsValid((PositionBB)board);
		sum = wBishopValid & centerMask;
		//System.out.println(Long.toBinaryString(sum));
		//System.out.println(BitBoardUtils.bitboardToString(sum, 'x'));
		System.out.println(BitBoardUtils.binaryToString(sum));
		assertNotEquals(0, sum);
		assertEquals(centerMask, sum);
		System.out.println("1-bits: " + Long.bitCount(sum));
	}
	
	@Test
	public void testCenterScore2() {
		Position board;
		long wBishopValid;
		long centerMask = 0b0000000000000000000000000001100000011000000000000000000000000000L;
		long sum;

		
		board = new PositionBB("8/8/8/8/8/2P5/1B6/8 w - - 0 1");
		wBishopValid = Movement.whiteBishopsValid((PositionBB)board);
		sum = wBishopValid & centerMask;
		//System.out.println(Long.toBinaryString(sum));
		//System.out.println(BitBoardUtils.bitboardToString(sum, 'x'));
		System.out.println(BitBoardUtils.binaryToString(sum));
		//assertNotEquals(0, sum);
		//System.out.println("1-bits: " + Long.bitCount(sum));
		
		System.out.println(board.getPossibleMoves());
		assertTrue(!board.getPossibleMoves().contains("b2d4"));
		assertTrue(!board.getPossibleMoves().contains("b2e5"));
	}

}

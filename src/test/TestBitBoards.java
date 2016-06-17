package test;

import static org.junit.Assert.assertEquals;

import java.util.function.Function;

import org.junit.Test;

import board.Board;
import board.pieces.Piece;
import board.position.BitBoard;
import board.position.bitboard.Movement;
import board.position.bitboard.PositionBB;
import search.Node;
import search.algorithms.AlphaBetaSearch;
import search.datastructures.Pair;
import search.evalfunctions.ScoreBitBoard;
import search.functions.BoardFunction;
import search.nodes.BoardNode;

public class TestBitBoards {
	@Test
	public void testBitBoards() {
		PositionBB pos = new PositionBB();
		pos.createStartpos();
		System.out.println(pos.toString());
	}
	
	@Test
	public void testMoveMasks() {
		System.out.println("possible white King moves");
		PositionBB pos = new PositionBB();
		pos.createStartpos();
		
		/* Modify startpos */
		pos.pieceTypeBB[Piece.WPAWN] = 0b0000000000000000000000000000000000000000000000000000000000000000L; // WhitePawns
		pos.whiteBB = 0b0000000000000000000000000000000000000000000000000000000011111111L;
		pos.allBB = 0b1111111111111111000000000000000000000000000000000000000011111111L;
		System.out.println(pos.toString());
		
		Movement.whiteKingValid(pos);
		long validMoves = Movement.whiteKingValid(pos); //Movement.compute_king_incomplete(pos.pieceTypeBB[pos.WKING], pos.whiteBB);
		System.out.println("white king valid moves: ");
		System.out.println(BitBoard.toString(validMoves));
		//System.out.println("act: " + Long.toBinaryString(validMoves));
		//System.out.println("exp: " + Long.toBinaryString(0b0000000000000000000000000000000000000000000000000011100000000000L));
		assertEquals(validMoves, 0b0000000000000000000000000000000000000000000000000011100000000000L);
		
		validMoves = Movement.blackKingValid(pos);
		System.out.println("black king valid moves: ");
		System.out.println(BitBoard.toString(validMoves));
		assertEquals(validMoves, 0b0L);
		
		validMoves = Movement.whiteKnightsValid(pos); //Movement.compute_king_incomplete(pos.pieceTypeBB[pos.WKING], pos.whiteBB);
		System.out.println("white knights valid moves: ");
		System.out.println(BitBoard.toString(validMoves));
		assertEquals(validMoves, 0b0000000000000000000000000000000000000000101001010001100000000000L);
		
		validMoves = Movement.blackKnightsValid(pos); //Movement.compute_king_incomplete(pos.pieceTypeBB[pos.WKING], pos.whiteBB);
		System.out.println("black knights valid moves: ");
		System.out.println(BitBoard.toString(validMoves));
		assertEquals(validMoves, 0b0000000000000000101001010000000000000000000000000000000000000000L);
		
		validMoves = Movement.whiteBishopsValid(pos);
		System.out.println("white bishops valid moves: ");
		System.out.println(BitBoard.toString(validMoves));
		
		/*validMoves = Movement.whiteRooksValid(pos);
		System.out.println("white rooks valid moves: ");
		System.out.println(BitBoard.toString(validMoves));*/
		
		pos.createStartpos();
		validMoves = Movement.whitePawnsValid(pos); //Movement.compute_king_incomplete(pos.pieceTypeBB[pos.WKING], pos.whiteBB);
		System.out.println("white pawns valid moves: ");
		System.out.println(BitBoard.toString(validMoves));
		//assertEquals(validMoves, 0b0000000000000000101001010000000000000000000000000000000000000000L);
	}

	@Test
	public void testValidMoves() {
		PositionBB pos = new PositionBB();
		pos.createStartpos();
		
		/* Modify startpos */
		pos.pieceTypeBB[Piece.WPAWN] = 0b0000000000000000000000000000000000000000000000000000000000000000L; // WhitePawns
		pos.whiteBB = 0b0000000000000000000000000000000000000000000000000000000011111111L;
		pos.allBB = 0b1111111111111111000000000000000000000000000000000000000011111111L;
		System.out.println(pos.toString());
		
		System.out.println("possible Moves: ");
		System.out.println(pos.getPossibleMoves());
	}
	
	@Test
	public void testPositionCopy() {
		PositionBB pos = new PositionBB();
		//System.out.println(pos.getActiveColor());
		PositionBB pos_copy = new PositionBB(pos);
		//System.out.println(pos_copy.getActiveColor());
		
		assertEquals(pos_copy.whiteMove(), pos.whiteMove());
		assertEquals(pos_copy.getWhiteMaterial(), pos.getWhiteMaterial());
		assertEquals(pos_copy.getBlackMaterial(), pos.getBlackMaterial());
		
		int[] squares = pos.getSquares();
		int[] squares_copy = pos_copy.getSquares();
		for(int i = 0; i < pos.getSquares().length; i++) {
			assertEquals(squares_copy[i], squares[i]);
		}
		assertEquals(pos_copy.toString(), pos.toString());
	}
	
	@Test
	public void testSearch() {
		Board board = new Board(new PositionBB());
		Node node = new BoardNode(board.copy());
		AlphaBetaSearch alphabeta = new AlphaBetaSearch(2, System.currentTimeMillis());
		Function<Board, Double> scoreboard = new ScoreBitBoard(board.copy());
		Function<Node, Double> evalFunction = new BoardFunction(scoreboard);
		Pair<Node, Double> result = alphabeta.search(
				new BoardNode(board.copy()),
				evalFunction);	
		System.out.println(result.f.getAction() + " " + result.s);
	}
	
	@Test
	public void testMaterial() {
		PositionBB pos = new PositionBB();
		System.out.println(pos.getWhiteMaterial() + " " +
		pos.getBlackMaterial());
	}
}

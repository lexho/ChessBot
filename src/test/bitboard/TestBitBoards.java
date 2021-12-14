package test.bitboard;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.Test;

import board.Board;
import board.pieces.Piece;
import board.position.BitBoard;
import board.position.Fen;
import board.position.bitboard.Movement;
import board.position.bitboard.PositionBB;
import engine.ChessBot;
import search.Node;
import search.algorithms.AlphaBetaSearchDouble;
import search.datastructures.Pair;
import search.evalfunctions.ScoreBitBoardDouble;
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
		Node node = new BoardNode(board.copy().getPositionBB());
		AlphaBetaSearchDouble alphabeta = new AlphaBetaSearchDouble(2, System.currentTimeMillis());
		Function<Board, Double> scoreboard = new ScoreBitBoardDouble(board.copy().getPositionBB());
		Function<Node, Double> evalFunction = new BoardFunction(scoreboard);
		Pair<Node, Double> result = alphabeta.search(
				new BoardNode(board.copy().getPositionBB()),
				evalFunction);	
		System.out.println(result.f.getAction() + " " + result.s);
	}
	
	// positions to test
	String[] fenPawnTakesQueen = {"3k4/8/1p6/Q7/8/8/8/4K3 b - - 1 1", "b6a5"};
	String[] fenPawnTakesRook = {"4k3/8/1p6/R7/8/8/8/4K3 b - - 1 1", "b6a5"};
	String[] fenPawnTakesKnight = {"4k3/8/1p6/N7/8/8/8/4K3 b - - 1 1", "b6a5"};
	
	String[] fenWhitePawnTakesQueen = {"3k4/8/1q6/2P5/8/8/8/4K3 w - - 1 1", "c5b6"};
	
	@Test
	public void testMaterialDepth1() {
		PositionBB pos = new PositionBB();
		System.out.println(pos.getWhiteMaterial() + " " +
		pos.getBlackMaterial());
		
		
		List<String[]> positions = new ArrayList<String[]>();
		positions.add(fenPawnTakesQueen);
		positions.add(fenPawnTakesRook);
		positions.add(fenPawnTakesKnight);
		
		positions.add(fenWhitePawnTakesQueen);

		pos = new PositionBB(new Fen(fenWhitePawnTakesQueen[0]));

		System.out.println("material: w: " + pos.getWhiteMaterial() + " b: " + pos.getBlackMaterial());
		//System.exit(-1);
		//PositionBB pos1 = new PositionBB(fen1);

		for(String[] position : positions) {
		//String[] position = fenPawnTakesQueen; {
			// two limits depth and time		
			ChessBot bot = new ChessBot(position[0]);
			String move = "0000";
			int time = 10000; // time 1000ms with 50 remaining moves, 1000ms/50 => 20 ms/move
			bot.setWtime(time);
			bot.setDepthLimit(1);
			move = bot.getNextMove();
			System.out.println("move: " + move);
			if(!move.equals(position[1])) System.err.println("failed at depth 1");
			assertEquals(position[1], move); // black pawn takes queen as he should

			System.out.println("result state: " + bot.alphabeta.getResultState());
			System.out.println(); //TODO how to deal with null nodes
		}
	}
	
	@Test
	public void testMaterialAllDepths() {
		PositionBB pos = new PositionBB();
		System.out.println(pos.getWhiteMaterial() + " " +
		pos.getBlackMaterial());
		
		String fen1 = "4k3/8/1p6/Q7/8/8/8/4K3 b - - 1 1";
		String fen2 = "3qk3/1p2p3/Q7/2p3B1/2pP4/p7/4P3/4K3 b Kk - 1 9";
		
		//PositionBB pos1 = new PositionBB(fen1);
		int muchmuchtime = 900000; // 15 min
		
		// two limits depth and time		
		for(int d = 1; d <= 12; d++) {
			ChessBot bot = new ChessBot(fenPawnTakesQueen[0]);
			String move = "0000";
			int time = muchmuchtime; // time 1000ms with 50 remaining moves, 1000ms/50 => 20 ms/move
			bot.setWtime(time);
			bot.setDepthLimit(d); //TODO test lower depths too
			move = bot.getNextMove();
			System.out.println("move: " + move);
			if(!move.equals(fenPawnTakesQueen[1])) System.err.println("failed at depth " + d);
			assertEquals(fenPawnTakesQueen[1], move); // black pawn takes queen as he should

			System.out.println("result state: " + bot.alphabeta.getResultState());
			//if(bot.alphabeta.getResultState() != 0) System.exit(-1);
			System.out.println(); //TODO how to deal with null nodes
		}
	}
}

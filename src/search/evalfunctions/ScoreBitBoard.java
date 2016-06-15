package search.evalfunctions;

import java.util.function.Function;

import board.Board;
import board.pieces.Piece;
import board.pieces.PieceList;
import board.position.PositionBB;
import board.position.bitboard.Movement;

/**
 * This is how a scoring function could look like. It's not a very sophisticated
 * scoring function, but it shows a few basic principles. Remember that this
 * function will only get called for leaf nodes:
 * 
 * - if the search encounters a node in which the game is over, this function
 * gets called.
 * 
 * - if the search reaches its depth-limit, this function gets
 * called!
 * @param <V>
 */
public class ScoreBitBoard<V> implements ScoreBoard
{
	private char player_id;
	private char opponent_id;
	private Board start;
	public int scoreCounter;
	private PositionBB position;

	/**
	 * This particular scoring function will get constructed each time a search is run.
	 * It gets passed the starting state of the board, so it can judge wether a sequence
	 * of moves during the search has led to a better state!
	 * @param start
	 * @param player_id
	 * @param opponent_id
	 */
	public ScoreBitBoard(Board start)
	{
		this.start = start;
		this.player_id = start.getColor();
		this.opponent_id = start.getOpponentsColor();
		this.scoreCounter = 0;
	}

	@Override
	public Double apply(Board board)
	{
		scoreCounter++;
		this.position = (PositionBB) board.getPosition();
		// nobody won or lost so far
		if (board.isRunning())
		  {
			double score = 0d;

			score += scoreMaterial(board);
			score += scoreMobility(board);
			score += scorePosition(board);
			
			return score;
		  } else
		{
			// Who is checkmate?
			if(board.getPosition().isInCheck(opponent_id)) {
				return 10000d;
			} else if (board.getPosition().isInCheck(player_id)) {
				return -10000d;
			} else {
				return 0d;
			}
		}
	}
	
	public int getScoreCounter() {
		return scoreCounter;
	}
	
	private double scoreMaterial(Board board) {
		double score = 0d;
		if(player_id == 'w') {
			score += board.getPositionBB().getWhiteMaterial();
			score -= board.getPositionBB().getBlackMaterial();
		} else {
			score += board.getPositionBB().getBlackMaterial();
			score -= board.getPositionBB().getWhiteMaterial();
		}
		
		return score;
	}
	
	private double scoreMobility(Board board) {
		long validMoves;
		if(board.getPositionBB().whiteMove)
			validMoves = Movement.whitePiecesValid(board.getPositionBB());
		else 
			validMoves = Movement.blackPiecesValid(board.getPositionBB());
		return Long.bitCount(validMoves);
		
		/*int[] white_pieces = {Position12x10.WROOK, Position12x10.WKNIGHT, Position12x10.WBISHOP, Position12x10.WQUEEN, Position12x10.WKING};
		int[] black_pieces = {Position12x10.BROOK, Position12x10.BKNIGHT, Position12x10.BBISHOP, Position12x10.BQUEEN, Position12x10.BKING};
		int factor;
		if(player_id == 'w') factor = 1; else factor = -1; 
		
		/* Calculate score 
		double score = 0d;
		for(int piece : white_pieces) {
			score += possibleMoves(board, piece) * factor;
		}
		for(int piece : black_pieces) {
			score += possibleMoves(board, piece) * factor * -1d;
		}
		return score / 16d; // scale score */
	}
	
	/*private double possibleMoves(Board board, int piece) {
		double score = 0d;
		for(Piece p : position.getPieceByID(piece)) {
			score += p.getPossibleMoves((Position12x10) board.getPosition()).size();
		}
		// TODO create method to convert white to black piece
		/*for(Piece p : position.getPieceByID(piece + 65)) {
			score += p.getPossibleMoves((Position12x10) board.getPosition()).size() * -1;
		}
		return score;
	} */


	private double scorePosition(Board board) {
		double score = 0d;

		int[] squares = board.getPositionBB().getSquares();
		for(int i = 0; i < squares.length; i++) {
			int index;
			if(player_id == 'w') { 
				index = i;
				switch (squares[i]) {
				case Piece.WPAWN:
					score += PawnTable[index];
					break;
				case Piece.WKNIGHT:
					score += KnightTable[index];
					break;
				case Piece.WBISHOP:
					score += BishopTable[index];
					break;
				case Piece.WKING:
					score += KingTable[index];
					//TODO score king end game
					break;
				}
			} else {
				index  = squares.length - i;
				switch (squares[i]) {
				case Piece.BPAWN:
					score += PawnTable[index];
					break;
				case Piece.BKNIGHT:
					score += KnightTable[index];
					break;
				case Piece.BBISHOP:
					score += BishopTable[index];
					break;
				case Piece.BKING:
					score += KingTable[index];
					//TODO score king end game
					break;
				}
			}
		}
		return score;
	}
	
	/** Pawns are encouraged to stay in the center and advance forward */
	private static short[] PawnTable = new short[] {
			-1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1,  -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1,  0,  0,  0,  0,  0,  0,  0,  0, -1,
			-1, 50, 50, 50, 50, 50, 50, 50, 50, -1,
			-1, 10, 10, 20, 30, 30, 20, 10, 10, -1,
			-1,  5,  5, 10, 27, 27, 10,  5,  5, -1,
			-1,  0,  0,  0, 25, 25,  0,  0,  0, -1,
			-1,  5, -5,-10,  0,  0,-10, -5,  5, -1,
			-1,  5, 10, 10,-25,-25, 10, 10,  5, -1,
			-1,  0,  0,  0,  0,  0,  0,  0,  0  -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1
	};
	
	/** Knights are encouraged to control the center and stay away from edges to increase mobility */
	private static short[] KnightTable = new short[] {
			-1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -50,-40,-30,-30,-30,-30,-40,-50, -1,
			-1, -40,-20,  0,  0,  0,  0,-20,-40, -1,
			-1, -30,  0, 10, 15, 15, 10,  0,-30, -1,
			-1, -30,  5, 15, 20, 20, 15,  5,-30, -1,
			-1, -30,  0, 15, 20, 20, 15,  0,-30, -1,
			-1, -30,  5, 10, 15, 15, 10,  5,-30, -1,
			-1, -40,-20,  0,  5,  5,  0,-20,-40, -1,
			-1, -50,-40,-20,-30,-30,-20,-40,-50, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1,  -1, -1, -1, -1, -1, -1, -1, -1, -1
	};
	
	/** Bishops are also encouraged to control the center and stay away from edges and corners */
	private static short[] BishopTable = new short[] {
			-1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -20,-10,-10,-10,-10,-10,-10,-20, -1,
			-1, -10,  0,  0,  0,  0,  0,  0,-10, -1,
			-1, -10,  0,  5, 10, 10,  5,  0,-10, -1,
			-1, -10,  5,  5, 10, 10,  5,  5,-10, -1,
			-1, -10,  0, 10, 10, 10, 10,  0,-10, -1,
			-1, -10, 10, 10, 10, 10, 10, 10,-10, -1,
			-1,  -10,  5,  0,  0,  0,  0,  5,-10, -1,
			-1, -20,-10,-40,-10,-10,-40,-10,-20, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1,  -1, -1, -1, -1, -1, -1, -1, -1, -1
	};
	
	/** Kings have 2 piece square tables, one for the end game and one for the middle game. 
	 * During the middle game kings are encouraged to stay in the corners, while in the end game kings are encouraged to move towards the center.*/
	 private static short[] KingTable = new short[] {
			 -1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -30, -40, -40, -50, -50, -40, -40, -30, -1,
			-1, -30, -40, -40, -50, -50, -40, -40, -30, -1,
			-1, -30, -40, -40, -50, -50, -40, -40, -30, -1,
			-1, -30, -40, -40, -50, -50, -40, -40, -30, -1,
			-1, -20, -30, -30, -40, -40, -30, -30, -20, -1,
			-1, -10, -20, -20, -20, -20, -20, -20, -10, -1,
			-1, 20,  20,   0,   0,   0,   0,  20,  20, -1,
			-1, 20,  30,  10,   0,   0,  10,  30,  20 -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1,  -1, -1, -1, -1, -1, -1, -1, -1, -1
	};

	/** Kings have 2 piece square tables, one for the end game and one for the middle game. 
	 * During the middle game kings are encouraged to stay in the corners, while in the end game kings are encouraged to move towards the center.*/
	private static short[] KingTableEndGame = new short[] {
			-1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -50,-40,-30,-20,-20,-30,-40,-50, -1,
			-1, -30,-20,-10,  0,  0,-10,-20,-30, -1,
			-1, -30,-10, 20, 30, 30, 20,-10,-30, -1,
			-1, -30,-10, 30, 40, 40, 30,-10,-30, -1,
			-1, -30,-10, 30, 40, 40, 30,-10,-30, -1,
			-1, -30,-10, 20, 30, 30, 20,-10,-30, -1,
			-1, -30,-30,  0,  0,  0,  0,-30,-30, -1,
			-1, -50,-30,-30,-30,-30,-30,-30,-50, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1,	-1,
			-1,  -1, -1, -1, -1, -1, -1, -1, -1, -1
	};
}

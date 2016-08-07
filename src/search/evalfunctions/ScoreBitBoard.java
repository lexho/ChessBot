package search.evalfunctions;

import board.Board;
import board.pieces.Piece;
import board.position.bitboard.Movement;
import board.position.bitboard.PositionBB;
import util.BitBoardUtils;

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
	public Integer apply(Board board)
	{
		scoreCounter++;
		this.position = (PositionBB) board.getPosition();

		// nobody won or lost so far
		if (board.isRunning())
		  {
			int score = 0;
			
			/* scoring dimensions */
			int mtrl = scoreMaterial(board);
			int mobility = scoreMobility(board) * 2;
			int pos = scorePosition(board);
			
			score = mtrl + mobility + pos;
			if(player_id == 'b') score *= -1;
			
			//if(mtrl > 0) System.out.println("material: " + mtrl + ", mobility: " + mobility + ", position: " + pos);
			
			return score;
		  } else
		{
			// Who is checkmate?
			if(board.getPosition().isInCheck(opponent_id)) {
				return 100000;
			} else if (board.getPosition().isInCheck(player_id)) {
				return -100000;
			} else {
				return 0;
			}
		}
	}
	
	public int getScoreCounter() {
		return scoreCounter;
	}
	
	public int scoreMaterial(Board board) {
		int score;
		score = board.getPositionBB().getWhiteMaterial();
		score -= board.getPositionBB().getBlackMaterial();
		//System.out.println(board);
		//System.out.println(board.getPositionBB().getWhiteMaterial() + " " + board.getPositionBB().getBlackMaterial());
		return score;
	}
	
	/* side to move relative score */
	public int scoreMobility(Board board) {
		
		/* get number of pieces and active color */
		//TODO fix bitboard move count
		board.getPositionBB().updateBitBoards();
		long validMovesW = Movement.whitePiecesValid(board.getPositionBB());
		long validMovesB = Movement.blackPiecesValid(board.getPositionBB());
		
		int wMobility = Long.bitCount(validMovesW); // number of valid white moves
		int bMobility = Long.bitCount(validMovesB); // number of valid black moves
		
		/*System.out.println(BitBoardUtils.bitboardToString(validMovesW, 'w'));
		System.out.println(BitBoardUtils.bitboardToString(validMovesB, 'b'));
		System.out.println("w mob: " + wMobility + ", b mob: " + bMobility);*/
		
		/* compute score */
		int score = (wMobility - bMobility);
		
		return score;
	}

	public int scorePosition(Board board) {
		int score = 0;

		int[] squares = board.getPositionBB().getSquares();
		
		for(int i = 0; i < squares.length; i++) {
			switch (squares[i]) {
			case Piece.WPAWN:
				score += PawnTable[i];
				break;
			case Piece.WKNIGHT:
				score += KnightTable[i];
				break;
			case Piece.WBISHOP:
				score += BishopTable[i];
				break;
			case Piece.WKING:
				score += KingTable[i];
				//TODO score king end game
				break;
			case Piece.BPAWN:
				score -= PawnTable[(int)(((int)(i + 56)) - (int)((int)(i / 8) * 16))];
				break;
			case Piece.BKNIGHT:
				score -= KnightTable[(int)(((int)(i + 56)) - (int)((int)(i / 8) * 16))];
				break;
			case Piece.BBISHOP:
				score -= BishopTable[(int)(((int)(i + 56)) - (int)((int)(i / 8) * 16))];
				break;
			case Piece.BKING:
				score -= KingTable[(int)(((int)(i + 56)) - (int)((int)(i / 8) * 16))];
				//TODO score king end game
				break;
			}
		}
		return score;
	}
	
	/** Pawns are encouraged to stay in the center and advance forward */
	private static short[] PawnTable = new short[] {
			 0,  0,  0,  0,  0,  0,  0,  0,
			50, 50, 50, 50, 50, 50, 50, 50,
			10, 10, 20, 30, 30, 20, 10, 10,
			 5,  5, 10, 27, 27, 10,  5,  5,
			 0,  0,  0, 25, 25,  0,  0,  0,
			 5, -5,-10,  0,  0,-10, -5,  5,
			 5, 10, 10,-25,-25, 10, 10,  5,
			 0,  0,  0,  0,  0,  0,  0,  0
	};
	
	/** Knights are encouraged to control the center and stay away from edges to increase mobility */
	private static short[] KnightTable = new short[] {
			-50,-40,-30,-30,-30,-30,-40,-50,
			-40,-20,  0,  0,  0,  0,-20,-40,
			-30,  0, 10, 15, 15, 10,  0,-30,
			-30,  5, 15, 20, 20, 15,  5,-30,
			-30,  0, 15, 20, 20, 15,  0,-30,
			-30,  5, 10, 15, 15, 10,  5,-30,
			-40,-20,  0,  5,  5,  0,-20,-40,
			-50,-40,-20,-30,-30,-20,-40,-50,
	};
	
	/** Bishops are also encouraged to control the center and stay away from edges and corners */
	private static short[] BishopTable = new short[] {
			-20,-10,-10,-10,-10,-10,-10,-20,
			-10,  0,  0,  0,  0,  0,  0,-10,
			-10,  0,  5, 10, 10,  5,  0,-10,
			-10,  5,  5, 10, 10,  5,  5,-10,
			-10,  0, 10, 10, 10, 10,  0,-10,
			-10, 10, 10, 10, 10, 10, 10,-10,
			-10,  5,  0,  0,  0,  0,  5,-10,
			-20,-10,-40,-10,-10,-40,-10,-20,
	};
	
	/** Kings have 2 piece square tables, one for the end game and one for the middle game. 
	 * During the middle game kings are encouraged to stay in the corners, while in the end game kings are encouraged to move towards the center.*/
	 private static short[] KingTable = new short[] {
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-30, -40, -40, -50, -50, -40, -40, -30,
			-20, -30, -30, -40, -40, -30, -30, -20,
			-10, -20, -20, -20, -20, -20, -20, -10,
			 20,  20,   0,   0,   0,   0,  20,  20,
			 20,  30,  10,   0,   0,  10,  30,  20
	};

	/** Kings have 2 piece square tables, one for the end game and one for the middle game. 
	 * During the middle game kings are encouraged to stay in the corners, while in the end game kings are encouraged to move towards the center.*/
	private static short[] KingTableEndGame = new short[] {
			-50,-40,-30,-20,-20,-30,-40,-50,
			-30,-20,-10,  0,  0,-10,-20,-30,
			-30,-10, 20, 30, 30, 20,-10,-30,
			-30,-10, 30, 40, 40, 30,-10,-30,
			-30,-10, 30, 40, 40, 30,-10,-30,
			-30,-10, 20, 30, 30, 20,-10,-30,
			-30,-30,  0,  0,  0,  0,-30,-30,
			-50,-30,-30,-30,-30,-30,-30,-50,
	};
}

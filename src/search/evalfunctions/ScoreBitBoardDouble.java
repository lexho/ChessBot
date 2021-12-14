package search.evalfunctions;

import board.Board;
import board.pieces.Piece;
import board.position.Position;
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
public class ScoreBitBoardDouble<V> implements ScoreBoard<Double>
{
	private boolean player_id;
	private boolean opponent_id;
	private Position start;
	public int scoreCounter;
	private PositionBB position;
	private static final boolean WHITE = true;
	private static final boolean BLACK = false;

	/**
	 * This particular scoring function will get constructed each time a search is run.
	 * It gets passed the starting state of the board, so it can judge wether a sequence
	 * of moves during the search has led to a better state!
	 * @param start
	 * @param player_id
	 * @param opponent_id
	 */
	public ScoreBitBoardDouble(Position start)
	{
		this.start = start;
		if(start.whiteMove())
			this.player_id = WHITE;
		else
			this.player_id = BLACK;
		this.opponent_id = !player_id;
		this.scoreCounter = 0;
	}

	@Override
	public Double apply(Position board)
	{
		scoreCounter++;
		this.position = board.getPositionBB();

		// nobody won or lost so far
		if (board.isRunning())
		  {
			double score = 0;
			
			/* scoring dimensions */
			double mtrl = scoreMaterial(board);
			double mobility = 0; //scoreMobility(board) * 2;
			double pos = scorePosition(board)*0.5d;
			double center = scoreCenter(board);
			
			score = mtrl + mobility + pos + center;
			
			if(player_id == BLACK) score *= -1;
			//if(player_id == WHITE) score *= -1;

			//if(mtrl > 0) System.out.println("material: " + mtrl + ", mobility: " + mobility + ", position: " + pos);
			//System.out.println("score: " + score);
			return score;
		  } else
		{
			// Who is checkmate?
			if(board.isInCheck(opponent_id)) {
				return Double.MAX_VALUE;
			} else if (board.isInCheck(player_id)) {
				return Double.MIN_VALUE;
			} else {
				return 0d;
			}
			  //return 0;
		}
	}
	
	private double scoreCenter(Position board) {
		// TODO Auto-generated method stub
		long centerMask = 0b0000000000000000000000000001100000011000000000000000000000000000L;
		
		long wBishopValid = Movement.whiteBishopsValid((PositionBB)board);
		long wBish = wBishopValid & centerMask;
		long wKnightsValid = Movement.whiteKnightsValid((PositionBB)board);
		long wKnights = wKnightsValid & centerMask;
		long wRooksValid = Movement.whiteRooksValid((PositionBB)board);
		long wRooks = wRooksValid & centerMask;
		
		long bBishopValid = Movement.blackBishopsValid((PositionBB)board);
		long bBish = bBishopValid & centerMask;
		long bKnightsValid = Movement.blackKnightsValid((PositionBB)board);
		long bKnights = bKnightsValid & centerMask;
		long bRooksValid = Movement.blackRooksValid((PositionBB)board);
		long bRooks = bRooksValid & centerMask;
		
		long sum = wBish + wKnights + wRooks - bBish - bKnights - bRooks;
		return Long.bitCount(sum);
	}

	public int getScoreCounter() {
		return scoreCounter;
	}
	
	public double scoreMaterial(Position board) {
		double score;

		score = board.getPositionBB().getWhiteMaterial();
		score -= board.getPositionBB().getBlackMaterial();
		
		// results differ (not anymore?) !!!
		//System.out.println(board);
		//System.out.println("material: w: " + board.getPositionBB().getWhiteMaterial() + " b: " + board.getPositionBB().getBlackMaterial());
		//System.out.println("score: " + score);
		
		/*board.updateMaterial(); // wMtrl and bMtrl do not get counted well so we have to do this manually
		score = board.getPositionBB().getWhiteMaterial();
		score -= board.getPositionBB().getBlackMaterial();*/
		//System.out.println("material: w: " + board.getPositionBB().getWhiteMaterial() + " b: " + board.getPositionBB().getBlackMaterial());
		//System.out.println("score: " + score);
		
		return score;
	}
	
	/* side to move relative score */
	public double scoreMobility(Position board) {
		
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
		double score = (wMobility - bMobility);
		
		return score;
	}

	public double scorePosition(Position board) {
		double score = 0;

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
				//score -= PawnTable[(int)(((int)(i + 56)) - (int)((int)(i / 8) * 16))];
				score -= PawnTable[i ^ 56];
				break;
			case Piece.BKNIGHT:
				//score -= KnightTable[(int)(((int)(i + 56)) - (int)((int)(i / 8) * 16))];
				score -= KnightTable[i ^ 56];
				break;
			case Piece.BBISHOP:
				//score -= BishopTable[(int)(((int)(i + 56)) - (int)((int)(i / 8) * 16))];
				score -= BishopTable[i ^ 56];
				break;
			case Piece.BKING:
				//score -= KingTable[(int)(((int)(i + 56)) - (int)((int)(i / 8) * 16))];
				score -= KingTable[i ^ 56];
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

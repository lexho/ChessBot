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
public class ScoreBitBoardMaterial<V> implements ScoreBoard<Integer>
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
	public ScoreBitBoardMaterial(Position start)
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
	public Integer apply(Position board)
	{
		scoreCounter++;
		this.position = board.getPositionBB();

		// nobody won or lost so far
		if (board.isRunning())
		  {
			int score = 0;
			
			/* scoring dimensions */
			int mtrl = scoreMaterial(board);
			int mobility = 0; //scoreMobility(board) * 2;
			int pos = 0; //scorePosition(board);
			
			score = mtrl + mobility + pos;

			if(player_id == BLACK) score *= -1;
			
			//if(mtrl > 0) System.out.println("material: " + mtrl + ", mobility: " + mobility + ", position: " + pos);
			//System.out.println("score: " + score);
			return score;
		  } else
		{
			// Who is checkmate?
			/*if(board.isInCheck(opponent_id)) {
				return 100000;
			} else if (board.isInCheck(player_id)) {
				return -100000;
			} else {
				return 0;
			}*/
			  return 0;
		}
	}
	
	public int getScoreCounter() {
		return scoreCounter;
	}
	
	public int scoreMaterial(Position board) {
		int score;
		//if(!board.getPositionBB().whiteMove && player_id == BLACK) invert = -1;
		score = board.getPositionBB().getWhiteMaterial();
		score -= board.getPositionBB().getBlackMaterial();
		//if(opponent_id == WHITE) score *= -1;
		//if(!board.getPositionBB().whiteMove && player_id == BLACK) score *= -1;
		
		// results differ (not anymore?) !!!
		System.out.println(board);
		//System.out.println("material: w: " + board.getPositionBB().getWhiteMaterial() + " b: " + board.getPositionBB().getBlackMaterial());
		//System.out.println("score: " + score);
		
		//board.updateMaterial(); // wMtrl and bMtrl do not get counted well so we have to do this manually
		score = board.getPositionBB().getWhiteMaterial();
		score -= board.getPositionBB().getBlackMaterial();
		System.out.println("material: w: " + board.getPositionBB().getWhiteMaterial() + " b: " + board.getPositionBB().getBlackMaterial());
		//System.out.println("score: " + score);
		
		return score;
	}
}

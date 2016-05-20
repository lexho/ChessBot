package search.evalfunctions;

import java.util.function.Function;

import board.Board;
import search.endconditions.EndCondition;

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
public class ScoreBoard<V> implements Function<Board, Double>
{
	private char unicorn_id;
	private char opponent_id;
	private Board start;

	/**
	 * This particular scoring function will get constructed each time a search is run.
	 * It gets passed the starting state of the board, so it can judge wether a sequence
	 * of moves during the search has led to a better state!
	 * @param start
	 * @param unicorn_id
	 * @param opponent_id
	 */
	public ScoreBoard(Board start)
	{
		this.start = start;
		this.unicorn_id = start.getColor();
		this.opponent_id = start.getOpponentsColor();
	}

	@Override
	public Double apply(Board board)
	{
		// nobody won or lost so far
		if (board.isRunning())
		{
			double score = 0d;

			/*score += scoreFountains(board);
			score += getFountainBalance(board);
			score += scoreClouds(board);
			score -= getDistanceToFountain(board);
			score += scoreSeeds(board);
			score -= distanceToOpponent(board);*/
			
			score += getPieceDifference(board);
			score += possibleMoves(board);
			//System.out.println(score);
			
			return score;
		} else
		{
			/* Who is checkmate? */
			if(board.getPosition().isInCheck(opponent_id)) {
				return 10000d;
			} else if (board.getPosition().isInCheck(unicorn_id)) {
				return -10000d;
			} else {
				return 0d;
			}
			/*EndCondition ec = board.getEndCondition();
			if (ec.getWinner() == unicorn_id)
			{
				return 10000d; // our bot won! (yay!)
			}
			else if (ec.getWinner() == opponent_id)
			{
				return -10000d; // our bot lost! (we don't want that ...)
			}
			else
			{
				return 0d; // it's a draw (boooooring!)
			}*/
		}
	}
	
	private double getPieceDifference(Board board) {
		int NrMyPieces = board.getPosition().getPieces().getPieces(unicorn_id).size();
		int NrOpponentPieces = board.getPosition().getPieces().getPieces(opponent_id).size();
		return (NrMyPieces - NrOpponentPieces) * 312; // return points in the range of [-10000, 10000]
	}
	
	private double possibleMoves(Board board) {
		if(unicorn_id == board.getActiveColor())
			return board.getPossibleMoves().size();
		else
			return board.getPossibleMoves().size() * -1;
	}

}

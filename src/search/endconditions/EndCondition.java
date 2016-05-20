package search.endconditions;

import java.util.List;

import board.Board;
import board.Position;

public interface EndCondition
{
	/**
	 * given a board, and the outcome of the actions in the current tick,
	 * determine whether the game has ended
	 * @param board
	 * @param evaporated
	 * @param sailing
	 * @return the state of the game
	 */
	public boolean hasEnded(Board board, Position position);
	
	/**
	 * 
	 * @return -1 if draw, id of winning unicorn otherwise
	 */
	public int getWinner();
	
	public boolean equals(Object obj);
	
	public int hashCode();

	public EndCondition copy();

	public String getOutcome();
	
	//public Outcome getEnumOutcome();
}

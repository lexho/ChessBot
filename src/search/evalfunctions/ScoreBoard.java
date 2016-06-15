package search.evalfunctions;

import java.util.function.Function;

import board.Board;

public interface ScoreBoard extends Function<Board, Double> {
	public int getScoreCounter();
}

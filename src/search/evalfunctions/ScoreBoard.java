package search.evalfunctions;

import java.util.function.Function;

import board.Board;
import board.position.Position;

public interface ScoreBoard extends Function<Position, Integer> {
	public int getScoreCounter();
}

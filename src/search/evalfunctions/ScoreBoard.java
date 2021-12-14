package search.evalfunctions;

import java.util.function.Function;

import board.position.Position;

public interface ScoreBoard<V> extends Function<Position, V> {
	public int getScoreCounter();
}

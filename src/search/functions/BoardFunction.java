package search.functions;

import java.util.function.Function;

import board.Board;
import board.position.Position;
import search.Node;

public class BoardFunction<V> implements Function<Node, V> {
	private Function<Position, V> f;

	public BoardFunction(Function<Position, V> f) {
		this.f = f;
	}

	@Override
	public V apply(Node node) {
		Position board = (Position) node.getState();
		return f.apply(board);
	}
}

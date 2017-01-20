package search.functions;

import java.util.function.Function;

import board.Board;
import board.position.Position;
import search.Node;

public class BoardFunction implements Function<Node, Integer> {
	private Function<Position, Integer> f;

	public BoardFunction(Function<Position, Integer> f) {
		this.f = f;
	}

	@Override
	public Integer apply(Node node) {
		Position board = (Position) node.getState();
		return f.apply(board);
	}
}

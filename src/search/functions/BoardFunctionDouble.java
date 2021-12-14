package search.functions;

import java.util.function.Function;

import board.Board;
import board.position.Position;
import search.Node;

public class BoardFunctionDouble implements Function<Node, Double> {
	private Function<Position, Double> f;

	public BoardFunctionDouble(Function<Position, Double> f) {
		this.f = f;
	}

	@Override
	public Double apply(Node node) {
		Position board = (Position) node.getState();
		return f.apply(board);
	}
}

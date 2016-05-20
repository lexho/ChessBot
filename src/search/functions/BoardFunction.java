package search.functions;

import java.util.function.Function;

import board.Board;
import search.Node;

public class BoardFunction implements Function<Node, Double> {
	private Function<Board, Double> f;

	public BoardFunction(Function<Board, Double> f) {
		this.f = f;
	}

	@Override
	public Double apply(Node node) {
		Board board = (Board) node.getState();
		return f.apply(board);
	}
}

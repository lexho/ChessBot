package search.functions;

import java.util.function.Function;

import board.Board;
import search.Node;

public class BoardFunction implements Function<Node, Integer> {
	private Function<Board, Integer> f;

	public BoardFunction(Function<Board, Integer> f) {
		this.f = f;
	}

	@Override
	public Integer apply(Node node) {
		Board board = (Board) node.getState();
		return f.apply(board);
	}
}

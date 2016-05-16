package search.functions;

import java.util.function.Predicate;

import board.Board;
import search.Node;

public class BoardPredicate implements Predicate<Node> {
	private Predicate<Board> p;

	public BoardPredicate(Predicate<Board> p) {
		this.p = p;
	}

	@Override
	public boolean test(Node node) {
		Board board = (Board) node.getState();
		return p.test(board);
	}
}

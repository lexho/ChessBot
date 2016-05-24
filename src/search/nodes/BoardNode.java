package search.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import board.Board;
import board.Move;
import search.Node;

public class BoardNode implements Node
{
	private BoardNode parent;
	private Board board;
	private Move move;
	
	public BoardNode(Board board)
	{
		this(null, null, board);
	}

	public BoardNode(BoardNode parent, Move move, Board board)
	{
		this.parent = parent;
		this.move = move;
		this.board = board;
	}

	@Override
	public List<Node> adjacent()
	{
		if (!board.isRunning())
			return Collections.emptyList();

		List<Node> successors = new ArrayList<>();
		List<Move> possible = board.getPossibleMoves();

		for (Move move : possible)
		{
			Board next = board.copy();
			next.makeMove(move);
			successors.add(new BoardNode(this, move, next));
		}
	
		return successors;
	}

	@Override
	public Node parent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		//if(board.getPossibleMoves().size() == 0)
		if(board.isMate()) {
			/*board.print();
			System.out.println("Player is Mate");*/
			return true;
		} else {
			return false;
		//return !board.isRunning();
		}
	}
	
	@Override
	public boolean isRoot() {
		return parent == null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <State> State getState() {
		return (State) board;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <Action> Action getAction() {
		return (Action) move;
	}

	@Override
	public String toString() {
		return board.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoardNode other = (BoardNode) obj;
		if (board == null) {
			if (other.board != null)
				return false;
		} else if (!board.equals(other.board))
			return false;
		return true;
	}
	
}

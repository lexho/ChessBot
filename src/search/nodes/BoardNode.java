package search.nodes;

import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.Move;
import search.Node;
import search.evalfunctions.MoveComparator;
import search.hashfunctions.ZobristHash;

public class BoardNode implements Node
{
	protected BoardNode parent;
	protected Board board;
	protected Move move;
	
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
		/*if (!board.isRunning())
			return Collections.emptyList();*/

		List<Node> successors = new ArrayList<>();
		List<Move> possible = board.getPossibleMoves();

		/* Move Ordering */
		int[] board_raw = board.getPosition12x10().get12x10Board();
		possible.sort(new MoveComparator(board_raw));
		
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

	//TODO isLeaf() is faulty
	@Override
	public boolean isLeaf() {
		//if(board.getPossibleMoves().size() == 0) // too slow
		//board.isMate() // incomplete
		if(!board.isRunning()) {
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
	
	public int getDepth() {
		int depth = 0;
		Node node = new BoardNode(this.board);
		while(!node.isRoot()) {
			depth++;
			node = node.parent();
		}
		return depth;
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
	/** @return a 32-Bit hash-code */
	public int hashCode() {
		/*final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		return result;*/
		return (int) ZobristHash.hash(board.getPosition12x10());
	}
	
	/** @return a 64-Bit hash-code */
	public long hashCode64() {
		/*final int prime = 31;
		int result = 1;
		result = prime * result + ((board == null) ? 0 : board.hashCode());
		return result;*/
		return ZobristHash.hash(board.getPosition12x10());
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
	
	public String getFullAction() {
		String movestr = new String();
		Node node = new BoardNode(this, this.getAction(), this.board);
		while(!node.isRoot()) {
			movestr = node.getAction().toString() + " " + movestr;
			node = node.parent();
		}
		return movestr;
	}
}

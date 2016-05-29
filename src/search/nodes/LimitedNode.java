package search.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import board.Board;
import board.Move;
import board.Position12x10;
import board.pieces.Piece;
import search.Node;

public class LimitedNode extends BoardNode
{
	
	public LimitedNode(Board board)
	{
		this(null, null, board);
	}

	public LimitedNode(LimitedNode parent, Move move, Board board)
	{
		super(parent, move, board);
		this.parent = parent;
		/*this.move = move;
		this.board = board;*/
	}

	@Override
	public List<Node> adjacent()
	{
		/*if (!board.isRunning())
			return Collections.emptyList();*/

		List<Node> successors = new ArrayList<>();
		List<Move> possible = board.getPossibleMoves();

		for (Move move : possible)
		{
			//Piece piece = board.getPosition().getPieces().getPieceAt(move.getSource());
			Piece piece = ((Position12x10)board.getPosition()).getPieceAt(move.getSourceIndex());
			
			/* Do not explore pawn moves in deep */
			//System.out.println("depth in Node: " + depth);
			if(getDepth() > 2 && piece.getID() == Piece.BLACK_PAWN || piece.getID() == Piece.WHITE_PAWN) {
				if(getAction() != null)
				//System.out.println("skip " + getFullAction());
				continue;	
			}
			
			Board next = board.copy();
			next.makeMove(move);
			successors.add(new LimitedNode(this, move, next));
		}
	
		return successors;
	}

	public String getFullAction() {
		String movestr = new String();
		Node node = new LimitedNode(this, this.getAction(), this.board);
		while(!node.isRoot()) {
			movestr = node.getAction().toString() + " " + movestr;
			node = node.parent();
		}
		return movestr;
	}
}

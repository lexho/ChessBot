package board.pieces;

import java.util.ArrayList;
import java.util.List;

import board.Move;
import board.MoveValidator;
import board.Rule;
import board.actions.Action;

public class Piece {
	String rep;
	int[] coord; // x / y on the board
	protected int ID = 0;
	
	public static final int WHITE_PAWN = 3;
	public static final int BLACK_PAWN = 4;
	public static final int KING = 1;
	public static final int QUEEN = 2;
	public static final int BISHOP = 6;
	public static final int KNIGHT = 5;
	public static final int ROOK = 7;
	
	/* actions */
	protected List<Action> actions;
	final static int LEFT = -1;
	final static int RIGHT = 1;
	final static int UP = 1;
	final static int DOWN = -1;
	protected int direction_x;
	protected int direction_y;
	int range;
	
	public Piece(String rep, int[] coord) {
		this.coord = new int[2];
		this.rep = rep;
		this.coord[0] = coord[0];
		this.coord[1] = coord[1];
		actions = new ArrayList<Action>();
	}
	
	public Piece(Piece p) {
		this.coord = new int[2];
		this.rep = new String(p.rep);
		this.coord[0] = p.coord[0];
		this.coord[1] = p.coord[1];
		actions = new ArrayList<Action>();
		for(Action a : p.actions) {
			actions.add(a);
		}
	}
	
	public int getID() {
		return ID;
	}
	
	public String getRepresentation() {
		return rep;
	}
	
	public int[] getPosition() {
		return coord;
	}
	
	public char getColor() {
		if((int) rep.charAt(0) < 'Z') return 'w';
		else return 'b';
	}
	
	public List<Move> getPossibleMoves() {
		//System.out.println("possible moves piece");
		//System.out.println("actions: " + actions.size());
		List<Move> moves = new ArrayList<Move>();
		//System.out.println(rep + " actions: " + actions.size());
		for(Action action : actions) {
			int[] target = action.apply(coord);
			
			/* Validate target position */
			//System.out.print(new Move(coord, action.apply(coord)));
			boolean isValid = MoveValidator.validateSquare(target);
			if(!isValid) System.out.print("invalid ");
			//System.out.println();

			if(isValid) moves.add(new Move(coord, action.apply(coord)));
		}
		return moves;
	}
	
	public List<Action> getActions() {
		return actions;
	}
	
	public boolean setPosition(int x, int y) {
		/* Out of board range */
		if(x < 0 || x > 7 || y < 0 || y > 7) return false;
		
		/* Set position coordinates */
		coord[0] = x;
		coord[1] = y;
		
		return true;
	}
	
	public boolean setPosition(int[] xy) {
		return setPosition(xy[0], xy[1]);
	}
	
	public String toString() {
		return rep;
	}
}

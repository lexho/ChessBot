package board.pieces;

import java.util.ArrayList;
import java.util.List;

import board.Move;
import board.MoveValidator;
import board.Position12x10;
import board.Rule;
import board.actions.Action;

public class Piece {
	char rep;
	int[] coord; // x / y on the board
	int index; // 12x10 board index
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
	
	//static final int KNIGHT_ = -9;
	// TODO add knight move constants
	
	protected int direction_x;
	protected int direction_y;
	int range;
	
	public Piece(char rep, int index) {
		this.rep = rep;
		this.index = index;
		actions = new ArrayList<Action>();
	}
	
	public Piece(char rep, int[] coord) {
		this.coord = new int[2];
		this.rep = rep;
		this.coord[0] = coord[0];
		this.coord[1] = coord[1];
		this.index = Position12x10.coordToIndex(coord);
		actions = new ArrayList<Action>();
	}
	
	public Piece(String rep, int[] coord) {
		this.coord = new int[2];
		this.rep = rep.charAt(0);
		this.coord[0] = coord[0];
		this.coord[1] = coord[1];
		this.index = Position12x10.coordToIndex(coord);
		actions = new ArrayList<Action>();
	}
	
	public Piece(Piece p) {
		this.coord = new int[2];
		this.rep = p.rep;
		this.coord[0] = p.coord[0];
		this.coord[1] = p.coord[1];
		this.index = p.index;
		actions = new ArrayList<Action>();
		for(Action a : p.actions) {
			actions.add(a);
		}
	}
	
	public int getID() {
		return ID;
	}
	
	public String getRep() {
		return Character.toString(rep);
	}
	
	public char getCharRep() {
		return rep;
	}
	
	public int[] getPosition() {
		return Position12x10.indexToCoord(index);
	}
	
	public char getColor() {
		if((int) rep < 'Z') return 'w';
		else return 'b';
	}
	
	public List<Move> getPossibleMoves() {

		List<Move> moves = new ArrayList<Move>();
		
		/*for(Action action : actions) {
			int target = action.apply(index);
			
			if(target != -1) moves.add(new Move(coord, action.apply(coord)));
		}*/
		
		for(Action action : actions) {
			//int[] target = action.apply(coord);
			
			/* Validate target position */
			//boolean isValid = MoveValidator.validateSquare(index);
			moves.add(new Move(index, action.apply(index)));
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
		index = Position12x10.coordToIndex(coord);
		
		return true;
	}
	
	public boolean setPosition(int[] xy) {
		return setPosition(xy[0], xy[1]);
	}
	
	public String toString() {
		return Character.toString(rep);
	}
}

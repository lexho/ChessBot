package board.pieces;

import java.util.ArrayList;
import java.util.List;

import board.Move;
import board.actions.Action;
import board.position.Position12x10;
import board.position.PositionBB;

public class Piece {
	char rep;
	int[] coord; // x / y on the board
	int index; // 12x10 board index
	
	public static final int nPieceTypes = 13;
	public static final int WHITE_PAWN = 3;
	public static final int BLACK_PAWN = 4;
	public static final int KING = 1;
	public static final int QUEEN = 2;
	public static final int BISHOP = 6;
	public static final int KNIGHT = 5;
	public static final int ROOK = 7;
	
	public static final int KING_V = 10000;
	public static final int QUEEN_V = 900;
	public static final int ROOK_V = 465;
	public static final int BISHOP_V = 325;
	public static final int KNIGHT_V = 275;
	public static final int PAWN_V = 100;
	
	/* actions */
	protected List<Action> actions;
	
	protected int direction_x;
	protected int direction_y;
	int range;
	
	public Piece(char rep, int index) {
		//if(index == -1) throw new InvalidIndexException(index);
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
		//if(index == -1) throw new InvalidIndexException(index);
		actions = new ArrayList<Action>();
	}
	
	public Piece(String rep, int[] coord) {
		
		this.coord = new int[2];
		this.rep = rep.charAt(0);
		this.coord[0] = coord[0];
		this.coord[1] = coord[1];
		this.index = Position12x10.coordToIndex(coord);
		//if(index == -1) throw new InvalidIndexException(index);
		actions = new ArrayList<Action>();
	}
	
	public Piece(Piece p) {
		this.coord = new int[2];
		this.rep = p.rep;
		this.coord[0] = p.coord[0];
		this.coord[1] = p.coord[1];
		this.index = p.index;
		//if(index == -1) throw new InvalidIndexException(index);
		actions = new ArrayList<Action>();
		for(Action a : p.actions) {
			actions.add(a);
		}
	}
	
	public int getID() {
		return 0;
	}
	
	/** the scoring value of the piece */
	public int getValue() {
		return 0;
	}
	
	public static int getValue(int piece) {
		switch(piece) {
		case PositionBB.WPAWNS: return PAWN_V;
		case PositionBB.WKNIGHTS: return KNIGHT_V;
		case PositionBB.WBISHOPS: return BISHOP_V;
		case PositionBB.WROOKS: return ROOK_V;
		case PositionBB.WQUEENS: return QUEEN_V;
		case PositionBB.WKING: return KING_V;
		case PositionBB.BPAWNS: return PAWN_V;
		case PositionBB.BKNIGHTS: return KNIGHT_V;
		case PositionBB.BBISHOPS: return BISHOP_V;
		case PositionBB.BROOKS: return ROOK_V;
		case PositionBB.BQUEENS: return QUEEN_V;
		case PositionBB.BKING: return KING_V;
		default: return 0;
		}
	}
	
	public String getRep() {
		return Character.toString(rep);
	}
	
	public char getCharRep() {
		return rep;
	}
	
	public int getPosIndex() {
		return index;
	}
	
	public int[] getPosition() {
		return Position12x10.indexToCoord(index);
	}
	
	public char getColor() {
		if((int) rep < 'Z') return 'w';
		else return 'b';
	}
	
	public boolean isMoveable(Position12x10 pos) {
		/* test all actions for validity */
		for(Action action : actions) {
			/* Validate target position */
			if(action.possible(pos, index)) return true;
		}
		return false;
	}
	
	public List<Move> getPossibleMoves(Position12x10 pos) {

		List<Move> moves = new ArrayList<Move>();
		
		/* Create move targets for all possible actions */
		for(Action action : actions) {
			List<Integer> targets = action.apply(pos, index);
			for(Integer target : targets) {
				moves.add(new Move(index, target));
			}
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
		int[] coord = new int[2];
		coord[0] = x;
		coord[1] = y;
		index = Position12x10.coordToIndex(coord);
		//if(index == -1) throw new InvalidIndexException(index);
		return true;
	}
	
	public boolean setPosition(int[] xy) {
		return setPosition(xy[0], xy[1]);
	}
	
	public static boolean isWhite(int p) {
		return p < 7 ? true : false;
		/*if(Character.isUpperCase(p)) return true;
		else return false;*/
	}
	
	public String toString() {
		return Character.toString(rep);
	}
}

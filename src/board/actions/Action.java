package board.actions;

import java.util.ArrayList;
import java.util.List;

import board.Move;
import board.Position;
import board.Position12x10;
import board.PositionInterface;
import board.pieces.Piece;

public class Action {
	protected int delta_x;
	protected int delta_y;
	protected int trans; // 12x10 board transformation
	protected boolean takes;
	protected boolean blockable = true; // piece is blockable by other pieces
	protected boolean repeatable = false; // the move is repeatable unti it's blocked by another piece
	
	/* move constants */
	public static final int UP = -10;
	public static final int DOWN = 10;
	public static final int LEFT = -1;
	public static final int RIGHT = 1;
	
	public static final int UP_LEFT = -11;
	public static final int UP_RIGHT = -9;
	public static final int DOWN_LEFT = 9;
	public static final int DOWN_RIGHT = 11;
	
	public Action(int trans, boolean repeat, boolean takes) {
		this.trans = trans;
		this.repeatable = repeat;
		this.takes = takes;
		blockable = true;
	}
	
	public Action(int trans, boolean takes) {
		this.trans = trans;
		this.takes = takes;
		blockable = true;
	}
	
	public Action(int[] delta, boolean takes) {
		this.delta_x = delta[0];
		this.delta_y = delta[1];
		this.takes = takes;
		blockable = true;
	}
	public Action(int dx, int dy, boolean takes) {
		this.delta_x = dx;
		this.delta_y = dy;
		this.takes = takes;
		blockable = true;
	}
	
	public void setBlockable(boolean value) {
		this.blockable = value;
	}
	
	public void setTakes(boolean takes) {
		this.takes = takes;
	}
	
	public boolean validate(int source, Position12x10 pos) {
		boolean isValid = false;
		int[] src = Position12x10.indexToCoord(source);
		List<Integer> targets = apply(pos, source);
		for(Integer target : targets) {
			boolean targetIsFree = pos.isFree(target);
			Piece targetPiece = pos.getPieceAt(target);
			
			Piece srcPiece = pos.getPieceAt(source);
			
			/* Is the source piece's color of the active player? */
			//System.out.println(srcPiece.getColor() + " != " + pos.getActiveColor());
			if(srcPiece.getColor() != pos.getActiveColor()) continue;
			
			if(!takes && targetIsFree) return true;
			if(takes && !targetIsFree && targetPiece.getColor() != pos.getActiveColor()) return true;
			else {
				continue; // action is not allowed
			}
		}
		return isValid;
		//return validate(src, pos);
	}
	
	/* Validate action in the given context */
	public boolean validate(int[] source, PositionInterface pos) {
		int[] target = apply(source);
		
		boolean targetIsFree = pos.isFree(target);
		Piece targetPiece = pos.getPieceAt(target);
		
		Piece srcPiece = pos.getPieceAt(source);
		
		/* Is the source piece's color of the active player? */
		//System.out.println(srcPiece.getColor() + " != " + pos.getActiveColor());
		if(srcPiece.getColor() != pos.getActiveColor()) return false;
		
		// TODO remove
		/*System.out.print(new Move(source, target) + " ");
		if(targetIsFree) System.out.print("is Free ");
		if(takes) System.out.print("takes ");
		System.out.print(pos.getActiveColor() + " != ");
		if(!targetIsFree) System.out.print(targetPiece.getColor() + " ");
		if(!targetIsFree && targetPiece.getColor() != pos.getActiveColor()) System.out.print("target is opponent ");
		System.out.println();*/

		/* Check if any piece is blocking the action */
		boolean distance = false;
		int dx = Math.abs(target[0] - source[0]);
		int dy = Math.abs(target[1] - source[1]);
		if(blockable && (dx > 1 || dy > 1)) {	
			Move move = new Move(source, target);
			/*System.out.print(move + ": ");
			System.out.print("blockable check " + source[0] + "/" + source[1]);
			System.out.println(" --> " + target[0] + "/" + target[1]);*/
			
			if(srcPiece.getID() == Piece.ROOK || (srcPiece.getID() == Piece.QUEEN && dx != dy)) {
				// Up
				if(target[1] > source[1]) {
					//System.out.print("dir: UP, ");
					for(int y = source[1] + 1; y < target[1]; y++) {
						//System.out.print(y + ", ");
						if(!(pos.getPieceAt(new int[]{source[0], y}) == null)) {
							//System.out.println();
							return false;
						}
						//if(!pos.getSquareAt(new int[]{source[0], y}).isFree()) return false;
					}
				}
				
				// Down
				if(target[1] < source[1]) {
					//System.out.print("dir: DOWN, ");
					for(int y = source[1] - 1; y > target[1]; y--) {
						//System.out.print(y + ", ");
						if(!(pos.getPieceAt(new int[]{source[0], y}) == null)) {
							//System.out.println();
							return false;
						}
						//if(!pos.getSquareAt(new int[]{source[0], y}).isFree()) return false;
					}
	
				}
				
				// Left
				if(target[0] < source[0]) {
					//System.out.print("dir: LEFT, ");
					for(int x = source[0] - 1; x > target[0]; x--) {
						//System.out.print(x + ", ");
						if(!(pos.getPieceAt(new int[]{x, source[1]}) == null)) {
							//System.out.println();
							return false;
						}
						//if(!pos.getSquareAt(new int[]{x, source[1]}).isFree()) return false;
					}
				}
				
				// Right
				if(target[0] > source[0]) {
					//System.out.print("dir: RIGHT, ");
					for(int x = source[0] + 1; x < target[0]; x++) {
						//System.out.print(x + ", ");
						if(!(pos.getPieceAt(new int[]{x, source[1]}) == null)) {
							//System.out.println();
							return false;
						}
						//if(!pos.getSquareAt(new int[]{x, source[1]}).isFree()) return false;
					}
				}
			}
			if(srcPiece.getID() == Piece.BISHOP || (srcPiece.getID() == Piece.QUEEN && dx == dy)) {
				// Up Right
				if(target[0] > source[0] && target[1] > source[1]) {
					//System.out.print("dir: UP, ");
					for(int x = source[0] + 1, y = source[1] + 1; x < target[0]; x++,y++) {
						//System.out.print(y + ", ");
						if(!(pos.getPieceAt(new int[]{x, y}) == null)) {
							//System.out.println();
							return false;
						}
						//if(!pos.getSquareAt(new int[]{source[0], y}).isFree()) return false;
					}
				}
				
				// Down Right
				if(target[0] > source[0] && target[1] < source[1]) {
					for(int x = source[0] + 1, y = source[1] - 1; x < target[0]; x++,y--) {
						//System.out.print(y + ", ");
						if(!(pos.getPieceAt(new int[]{x, y}) == null)) {
							//System.out.println();
							return false;
						}
						//if(!pos.getSquareAt(new int[]{source[0], y}).isFree()) return false;
					}
				}
				
				// Down Left
				if(target[0] < source[0] && target[1] < source[1]) {
					for(int x = source[0] - 1, y = source[1] - 1; x > target[0]; x--,y--) {
						//System.out.print(y + ", ");
						if(!(pos.getPieceAt(new int[]{x, y}) == null)) {
							//System.out.println();
							return false;
						}
						//if(!pos.getSquareAt(new int[]{source[0], y}).isFree()) return false;
					}
				}
				
				// Up Left
				if(target[0] < source[0] && target[1] > source[1]) {
					for(int x = source[0] - 1, y = source[1] + 1; x > target[0]; x--,y++) {
						//System.out.print(y + ", ");
						if(!(pos.getPieceAt(new int[]{x, y}) == null)) {
							//System.out.println();
							return false;
						}
						//if(!pos.getSquareAt(new int[]{source[0], y}).isFree()) return false;
					}
				}
			}
		}
		
		if(!takes && targetIsFree) return true;
		if(takes && !targetIsFree && targetPiece.getColor() != pos.getActiveColor()) return true;
		else {
			return false; // action is not allowed
		}
	}
	
	/** Is any action possible in the given position */
	public boolean possible(Position12x10 pos, int index) {
		List<Integer> targets = new ArrayList<Integer>();
		int i = index + trans;
		if(pos.isValid(i) && ((!takes && pos.isFree(i)) || takes) ) return true;
		else return false;
	}
	
	public int[] apply(int[] coord) {
		int[] coord_new = new int[2];
		
		/* transform coordinates */
		coord_new[0] = coord[0] + delta_x;
		coord_new[1] = coord[1] + delta_y;
		//System.out.println(coord[0] + " " + coord[1] + " " + coord_new[0] + " " + coord_new[1]);
		return coord_new;
	}
	
	/** apply action 
	 * @param source current index
	 * @return new index after performed action */
	public List<Integer> apply(Position12x10 pos, int source) {
		List<Integer> targets = new ArrayList<Integer>();
		int i = source + trans;
		//TODO fix this loop
		while(pos.isValid(i) && ((!takes && pos.isFree(i)) || takes) ) {
			boolean takesPiece = takes && !pos.isFree(i);
			if(takesPiece && pos.isColor(i, pos.getColor(source))) break; // do not take your own pieces
			targets.add(i);
			if(!repeatable) break;
			if(takesPiece) break; // take and action is done
			i += trans;
		}
		
		return targets;
	}
	
	public String toString() {
		String takesstr = new String();
		if(takes) takesstr = "takes";
		return delta_x + " " + delta_y + " "+ takesstr;
	}
}

package board.actions;

import java.util.ArrayList;
import java.util.List;

import board.Position12x10;
import board.pieces.Piece;

public class Action {
	/*protected int delta_x;
	protected int delta_y;*/
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
	
	/*public Action(int[] delta, boolean takes) {
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
	}*/
	
	public void setBlockable(boolean value) {
		this.blockable = value;
	}
	
	public void setTakes(boolean takes) {
		this.takes = takes;
	}
	
	/** Validate action in the given context */
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
	
	/** Is any action possible in the given position */
	public boolean possible(Position12x10 pos, int index) {
		List<Integer> targets = new ArrayList<Integer>();
		int i = index + trans;
		if(pos.isValid(i) && ((!takes && pos.isFree(i)) || takes) ) return true;
		else return false;
	}
	
	/*public int[] apply(int[] coord) {
		int[] coord_new = new int[2];
		
		/* transform coordinates 
		coord_new[0] = coord[0] + delta_x;
		coord_new[1] = coord[1] + delta_y;
		//System.out.println(coord[0] + " " + coord[1] + " " + coord_new[0] + " " + coord_new[1]);
		return coord_new;
	}*/
	
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
		return trans + " "+ takesstr;
	}
}

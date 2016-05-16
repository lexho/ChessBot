package board.actions;

import board.Position;
import board.pieces.Piece;

public class Action {
	protected int delta_x;
	protected int delta_y;
	protected boolean takes;
	
	public Action(int[] delta, boolean takes) {
		this.delta_x = delta[0];
		this.delta_y = delta[1];
		this.takes = takes;
	}
	public Action(int dx, int dy, boolean takes) {
		this.delta_x = dx;
		this.delta_y = dy;
		this.takes = takes;
	}
	
	public void setTakes(boolean takes) {
		this.takes = takes;
	}
	
	/* Validate action in the given context */
	public boolean validate(int[] coord, Position pos) {
		int[] target = apply(coord);
		
		boolean isFree = pos.getSquareAt(target).isFree();
		Piece targetPiece = pos.getSquareAt(target).getPiece();

		if(!takes && isFree) return true;
		if(takes && !isFree && targetPiece.getColor() != pos.getActiveColor()) return true;
		else return false; // action is not allowed
	}
	
	public int[] apply(int[] coord) {
		int[] coord_new = new int[2];
		
		/* transform coordinates */
		coord_new[0] = coord[0] + delta_x;
		coord_new[1] = coord[1] + delta_y;
		//System.out.println(coord[0] + " " + coord[1] + " " + coord_new[0] + " " + coord_new[1]);
		return coord_new;
	}
	
	public String toString() {
		String takesstr = new String();
		if(takes) takesstr = "takes";
		return delta_x + " " + delta_y + " "+ takesstr;
	}
}

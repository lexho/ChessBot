package board.actions;

import board.Move;
import board.Position;
import board.pieces.Piece;
import exceptions.InvalidMoveException;

public class Action {
	protected int delta_x;
	protected int delta_y;
	protected boolean takes;
	protected boolean blockable = true; // piece is blockable by other pieces
	
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
	
	/* Validate action in the given context */
	public boolean validate(int[] source, Position pos) {
		int[] target = apply(source);
		
		boolean targetIsFree = pos.getSquareAt(target).isFree();
		Piece targetPiece = pos.getSquareAt(target).getPiece();
		
		Piece srcPiece = pos.getSquareAt(source).getPiece();

		/* Check if any piece is blocking the action */
		boolean distance = false;
		int dx = Math.abs(target[0] - source[0]);
		int dy = Math.abs(target[1] - source[1]);
		if(blockable && (dx > 1 || dy > 1)) {	
			Move move = new Move(source, target);
			//System.out.print(move + ": ");
			/*//System.out.print("blockable check " + source[0] + "/" + source[1]);
			//System.out.println(" --> " + target[0] + "/" + target[1]);*/
			
			if(srcPiece.getID() == Piece.ROOK || (srcPiece.getID() == Piece.QUEEN && dx != dy)) {
				// Up
				if(target[1] > source[1]) {
					//System.out.print("dir: UP, ");
					for(int y = source[1] + 1; y < target[1]; y++) {
						//System.out.print(y + ", ");
						if(!(pos.getPieces().getPieceAt(new int[]{source[0], y}) == null)) {
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
						if(!(pos.getPieces().getPieceAt(new int[]{source[0], y}) == null)) {
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
						if(!(pos.getPieces().getPieceAt(new int[]{x, source[1]}) == null)) {
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
						if(!(pos.getPieces().getPieceAt(new int[]{x, source[1]}) == null)) {
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
						if(!(pos.getPieces().getPieceAt(new int[]{x, y}) == null)) {
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
						if(!(pos.getPieces().getPieceAt(new int[]{x, y}) == null)) {
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
						if(!(pos.getPieces().getPieceAt(new int[]{x, y}) == null)) {
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
						if(!(pos.getPieces().getPieceAt(new int[]{x, y}) == null)) {
							//System.out.println();
							return false;
						}
						//if(!pos.getSquareAt(new int[]{source[0], y}).isFree()) return false;
					}
				}
			}
			
			////System.out.println("blockable");
			/*int x; int y;
			if(source[0] == target[0]) { // x is constant; up/down
				x = 0;
				y = 1;
			} else if(source[1] == target[1]){ // y is constant left/right
				x = 1;
				y = 0;
			} else {
				return false;
			}

			// move direction
			int dir;
			if(target[y] > source[y]) {
				dir = 1; // up
			} else {
				dir = -1; // down
			}
			
			int test = source[y] + dir; // exclude start position
			
			while(dir * test < target[y]) {
				test += dir;
				if(test < dir*target[y]) break;
				////System.out.println(test + " " + source[y]);
				if(!pos.getSquareAt(new int[]{test, source[y]}).isFree()) {
					//System.out.println(pos.getPieces().getPieceAt(new int[]{test, source[y]}) + " ("+ test + "/" + source[y] + ") is not Free");
					return false;
				}
			}
			/*for(int d = 1; d <= 8; d++) {
				int[] test = new int[2];
				test[0] = source[0] + delta_x * d;
				test[1] = source[1] + delta_y * d;
				if(test[0] == target[0] && test[1] == target[1]) break;
				if(!pos.getSquareAt(target).isFree()) return false;
			}*/
		}
		
		if(!takes && targetIsFree) return true;
		if(takes && !targetIsFree && targetPiece.getColor() != pos.getActiveColor()) return true;
		else {
			return false; // action is not allowed
		}
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

package board.position.bitboard;

import board.position.PositionBB;

public class Move extends board.Move{

	public Move(int src8x8, int trg8x8, int promPiece) {
		super(src8x8, trg8x8, 1);
	}
	
	public Move(int src8x8, int trg8x8) {
		super(src8x8, trg8x8, 1);
		
	}
	
	/*@Override
	public String toString() {
		if(command != null) return command;
		/* create long algebraic move string 
		int[] src = {PositionBB.getX(i_src), PositionBB.getY(i_src)}; //Position12x10.indexToCoord(this.i_src);
		int[] trg = {PositionBB.getX(i_trg), PositionBB.getY(i_trg)}; //Position12x10.indexToCoord(this.i_trg);
		char alpha = (char) ((char) src[0] + 'a');
		char num = (char) ((char) src[1] + '0' + 1);
		char alpha1 = (char) ((char) trg[0] + 'a');
		char num1 = (char) ((char) trg[1] + '0' + 1);
		command = Character.toString(alpha) + Character.toString(num) + Character.toString(alpha1) + Character.toString(num1);
		return command;
	}*/

}

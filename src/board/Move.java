package board;

import java.util.List;

public class Move {
	private String command;
	//int src[];
	//int[] trg;
	private int i_src;
	private int i_trg;
	public Move (String cmd) {
		this.command = cmd;
		
		/* Convert ascii to int source */
		int x = (int) command.charAt(0) - 'a';
		int y = (int) command.charAt(1) - '0' - 1;
		int[] src = new int[2];
		src[0] = x;
		src[1] = y;
		
		/* Convert ascii to int target */
		x = (int) command.charAt(2) - 'a';
		y = (int) command.charAt(3) - '0' - 1;
		int[] trg = new int[2];
		trg[0] = x;
		trg[1] = y;
		
		/* Create 12x10 board index */
		this.i_src = Position12x10.coordToIndex(src);
		this.i_trg = Position12x10.coordToIndex(trg);
		//System.out.println("i_src " + i_src);
		
	}
	
	/**
	 * create a new move by source and target coordinates of a 8x8 board
	 * @param src the source coordinates
	 * @param trg the target coordinates
	 * */
	public Move(int[]src, int[] trg) {
		
		/*this.src = new int[2];
		this.trg = new int[2];
		this.src[0] = src[0];
		this.src[1] = src[1];
		this.trg[0] = trg[0];
		this.trg[1] = trg[1];*/
		
		this.i_src = Position12x10.coordToIndex(src);
		this.i_trg = Position12x10.coordToIndex(trg);
		/*System.out.print(src[0] + "/"+ src[1] + " --> ");
		System.out.print(i_src);
		
		int[] coord = Position12x10.indexToCoord(i_src);
		System.out.println(" --> " + coord[0] + "/"+ coord[1]);
		if(coord[0] != src[0] || coord[1] != src[1]) System.exit(-1);*/
		
		/* create command string */
		/*char alpha = (char) ((char) src[0] + 'a');
		char num = (char) ((char) src[1] + '0' + 1);
		char alpha1 = (char) ((char) trg[0] + 'a');
		char num1 = (char) ((char) trg[1] + '0' + 1);
		this.command = Character.toString(alpha) + Character.toString(num) + Character.toString(alpha1) + Character.toString(num1);
		*/
	}
	
	/** Create a Move by source and target indexes of a 12x10 board 
	 * @param src 12x10 board index of move's source
	 * @param trg 12x10 board index of move'S target
	 * */
	public Move(int src, int trg) {
		this.i_src = src;
		this.i_trg = trg;
	}
	
	/**
	 * get the source coordinates of the move
	 * @return the source coordinates
	 */
	public int[] getSource() {
		return Position12x10.indexToCoord(i_src);
	}
	
	/**
	 * get the target coordinates of the move
	 * @return the target coordinates
	 */
	public int[] getTarget() {
		return Position12x10.indexToCoord(i_trg);
	}
	
	/**
	 * get the source index of the move
	 * @return the source index
	 */
	public int getSourceIndex() {
		return i_src;
	}
	
	/**
	 * get the target index of the move
	 * @return the target index
	 */
	public int getTargetIndex() {
		return i_trg;
	}
	
	/**
	 * convert the move into an action
	 * @return an action that can be applied
	 */
	//TODO remove this
	/*public Action getAction() {
		int[] src = getSource();
		int[] trg = getTarget();
		int[] delta = {trg[0] - src[0], trg[1] - src[1]};
		
		return new Action(delta, false);
	}*/
	
	/**
	 * @return the move in long algebraic notation
	 */
	public String toString() {
		if(command != null) return command;
		/* create long algebraic move string */
		int[] src = Position12x10.indexToCoord(this.i_src);
		int[] trg = Position12x10.indexToCoord(this.i_trg);
		char alpha = (char) ((char) src[0] + 'a');
		char num = (char) ((char) src[1] + '0' + 1);
		char alpha1 = (char) ((char) trg[0] + 'a');
		char num1 = (char) ((char) trg[1] + '0' + 1);
		command = Character.toString(alpha) + Character.toString(num) + Character.toString(alpha1) + Character.toString(num1);
		return command;
	}
	
	/** Does the moveList contain the Move move? 
	 * @return contains move */
	public static boolean contains(List<Move> moveList, Move move) {
		for(Move m : moveList) {
			if(m.toString().equals(move.toString())) {
				return true;
			}
		}
		return false;
	}
}

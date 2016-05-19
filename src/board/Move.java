package board;

import board.actions.Action;

public class Move {
	String command;
	public Move (String cmd) {
		this.command = cmd;
	}
	
	/**
	 * create a new move
	 * @param src the source coordinates
	 * @param trg the target coordinates
	 * */
	public Move(int[]src, int[] trg) {
		char alpha = (char) ((char) src[0] + 'a');
		char num = (char) ((char) src[1] + '0' + 1);
		char alpha1 = (char) ((char) trg[0] + 'a');
		char num1 = (char) ((char) trg[1] + '0' + 1);
		
		this.command = Character.toString(alpha) + Character.toString(num) + Character.toString(alpha1) + Character.toString(num1);
	}
	
	/**
	 * get the source coordinates of the move
	 * @return the source coordinates
	 */
	public int[] getSource() {
		/* Convert ascii to int */
		int x = (int) command.charAt(0) - 'a';
		int y = (int) command.charAt(1) - '0' - 1;
		int[] coord = new int[2];
		coord[0] = x;
		coord[1] = y;
		return coord;
	}
	
	/**
	 * get the target coordinates of the move
	 * @return the target coordinates
	 */
	public int[] getTarget() {
		/* Convert ascii to int */
		int x = (int) command.charAt(2) - 'a';
		int y = (int) command.charAt(3) - '0' - 1;
		int[] coord = new int[2];
		coord[0] = x;
		coord[1] = y;
		return coord;
	}
	
	/**
	 * convert the move into an action
	 * @return an action that can be applied
	 */
	//TODO fix this: this method always returns actions where taking is not allowed
	public Action getAction() {
		int[] src = getSource();
		int[] trg = getTarget();
		int[] delta = {trg[0] - src[0], trg[1] - src[1]};
		
		return new Action(delta, false);
	}
	
	/**
	 * @return the move in long algebraic notation
	 */
	public String toString() {
		return command;
	}
}

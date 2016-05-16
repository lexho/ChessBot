package board;

import board.actions.Action;

public class Move {
	String command;
	public Move (String cmd) {
		this.command = cmd;
	}
	
	public Move(int[]src, int[] trg) {
		char alpha = (char) ((char) src[0] + 'a');
		char num = (char) ((char) src[1] + '0' + 1);
		char alpha1 = (char) ((char) trg[0] + 'a');
		char num1 = (char) ((char) trg[1] + '0' + 1);
		
		this.command = Character.toString(alpha) + Character.toString(num) + Character.toString(alpha1) + Character.toString(num1);
	}
	
	public int[] getSource() {
		/* Convert ascii to int */
		int x = (int) command.charAt(0) - 'a';
		int y = (int) command.charAt(1) - '0' - 1;
		int[] coord = new int[2];
		coord[0] = x;
		coord[1] = y;
		return coord;
	}
	
	public int[] getTarget() {
		/* Convert ascii to int */
		int x = (int) command.charAt(2) - 'a';
		int y = (int) command.charAt(3) - '0' - 1;
		int[] coord = new int[2];
		coord[0] = x;
		coord[1] = y;
		return coord;
	}
	
	public Action getAction() {
		int[] src = getSource();
		int[] trg = getTarget();
		int[] delta = {trg[0] - src[0], trg[1] - src[1]};
		
		return new Action(delta, false);
	}
	
	public String toString() {
		return command;
	}
}

package board.position;

import java.util.List;

import util.StringUtils;

public class Fen {

	List<String> fenstr;
	
	/** @param fenstr a particular game position in Forsyth-Edwards Notation like 
	 * "position fen rnbqkbnr/pppp1ppp/8/8/8/8/PPPPQPPP/RNB1KBNR b KQkq - 0 2" */
	public Fen(String fenstr) {
		//System.out.println(fenstr);
		this.fenstr = StringUtils.splitString(fenstr, ' ');
	}
	
	public String getPiecePlacement() {
		return fenstr.get(0);
	}
	
	public char getActiveColor() {
		return fenstr.get(1).charAt(0);
	}
	
	public String getCastling() {
		return fenstr.get(2);
	}
	
	public String getEnPassant() {
		return fenstr.get(3);
	}
	
	public String getHalfMove() {
		return fenstr.get(4);
	}
	
	public String getFullMove() {
		return fenstr.get(5);
	}
	
	public String toString() {
		return fenstr.toString();
	}
}

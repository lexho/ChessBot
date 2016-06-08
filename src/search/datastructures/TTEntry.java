package search.datastructures;

import search.Node;

public class TTEntry {
	/*int depth;
	double score;
	boolean ancient;
	short NodeType;*/
	public static short hashfEXACT = 0;
	public static short hashfALPHA = 1;
	public static short hashfBETA = 2;
	
	Pair<Node, Double> pair;
	short flag;
	
	public TTEntry(Pair<Node, Double> p, short flag) {
		this.pair = p;
		this.flag = flag;
	}
	
	public Pair<Node, Double> getPair() {
		return pair;
	}
	
	public boolean checkFlag(short val) {
		return flag == val;
	}
}

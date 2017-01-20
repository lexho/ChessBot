package search.hashtables;

import search.Node;
import search.datastructures.Pair;

public class HashTableEntry {
	/*int depth;
	double score;
	boolean ancient;
	short NodeType;*/
	public static short hashfEXACT = 0;
	public static short hashfALPHA = 1;
	public static short hashfBETA = 2;
	
	//Pair<Node, Double> pair;
	int score;
	int depth;
	short flag;
	
	public HashTableEntry(Pair<Node, Integer> p, int depth, short flag) {
		//this.pair = p;
		this.score = p.s;
		//this.depth = p.f.getDepth();
		this.depth = depth;
		this.flag = flag;
	}
	
	public HashTableEntry(int score, int depth, short flag) {
		//this.pair = p;
		this.score = score;
		this.depth = depth;
		this.flag = flag;
	}
	
	/*public Pair<Node, Double> getPair() {
		return pair;
	}*/
	
	public int getDepth() {
		return depth;
	}
	
	public Integer getScore() {
		return score;
	}
	
	public boolean checkFlag(short val) {
		return flag == val;
	}
}

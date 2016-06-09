package search.hashtables;

import java.util.HashMap;

import search.Node;
import search.datastructures.Pair;

public class MainTranspositionTable extends HashMap<Integer, HashTableEntry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5061293320753325926L;
	public static final int valUNKNOWN = -1;
	
	public Pair<Node, Double> ProbeHash(Node current, int depth, Pair<Node, Double> alpha, Pair<Node, Double> beta) {
		if(this.containsKey(current.hashCode())) {
			//System.out.println("hashmap contains key: " + current.hashCode());
			HashTableEntry entry = (HashTableEntry) this.get(current.hashCode());
			if(entry.getDepth() >= depth) {
				if(entry.checkFlag(HashTableEntry.hashfEXACT))
					return new Pair<Node, Double>(current, entry.getScore());
					//return entry.getPair();
				if(entry.checkFlag(HashTableEntry.hashfALPHA) && entry.getScore() <= alpha.s )
					return alpha;
				if(entry.checkFlag(HashTableEntry.hashfALPHA) && entry.getScore() >= beta.s )
					return beta;
			}
			//TODO RememberBestMove();
		}
		return null;
	}
}

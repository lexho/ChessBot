package search.hashtables;

import java.util.HashMap;

import search.Node;
import search.datastructures.Pair;
import search.datastructures.TTEntry;

public class MainTranspositionTable extends HashMap<Integer, TTEntry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5061293320753325926L;
	public static final int valUNKNOWN = -1;
	
	public Pair<Node, Double> ProbeHash(Node current, int depth, Pair<Node, Double> alpha, Pair<Node, Double> beta) {
		if(this.containsKey(current.hashCode())) {
			//System.out.println("hashmap contains key: " + current.hashCode());
			TTEntry entry = (TTEntry) this.get(current.hashCode());
			if(entry.getPair().f.getDepth() >= depth) {
				if(entry.checkFlag(TTEntry.hashfEXACT))
					return entry.getPair();
				if(entry.checkFlag(TTEntry.hashfALPHA) && entry.getPair().s <= alpha.s )
					return alpha;
				if(entry.checkFlag(TTEntry.hashfALPHA) && entry.getPair().s >= beta.s )
					return beta;
			}
			//TODO RememberBestMove();
		}
		return null;
	}
}

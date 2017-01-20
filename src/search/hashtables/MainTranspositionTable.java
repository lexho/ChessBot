package search.hashtables;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import search.Node;
import search.datastructures.Pair;

public class MainTranspositionTable extends ConcurrentHashMap<Long, HashTableEntry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5061293320753325926L;
	public static final int valUNKNOWN = -1;
	public static int PROBE = 0;
	public static int READ = 0;
	public static int WRITE = 0;
	
	public Pair<Node, Integer> ProbeHash(Node current, int depth, Pair<Node, Integer> alpha, Pair<Node, Integer> beta) {
		//PROBE++;
		if(this.containsKey(current.hashCode64())) {
			//READ++;
			//System.out.println("hashmap contains key: " + current.hashCode64());
			HashTableEntry entry = (HashTableEntry) this.get(current.hashCode64());
			if(entry.getDepth() >= depth) {
				if(entry.checkFlag(HashTableEntry.hashfEXACT))
					return new Pair<Node, Integer>(current, entry.getScore());
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
	
	public void put(long key, HashTableEntry value) {
		if(super.putIfAbsent(key, value) == null) {
			HashTableEntry value_stored = this.get(key);
			this.replace(key, value); // replace all older hash values
			//System.out.println("replaced: " + value_stored.getDepth() + " / new: " + value.getDepth());
		} else {
			//System.out.println("stored: " + value.getDepth() + ", score: " + value.getScore());
		}
		//super.putIfAbsent(key, value);
		/*if(!this.contains(key)) {
			super.putIfAbsent(key, value);
		} else {
			HashTableEntry value_stored = this.get(key);
			System.out.println("stored: " + value_stored + " / new: " + value.getDepth());
			if(value.getDepth() > value_stored.getDepth()) {
				System.out.println("replace hash entry with deeper one");
				super.putIfAbsent(key, value);
			}
		}*/
		//super.putIfAbsent(key, value);
		//WRITE++;
	}
}

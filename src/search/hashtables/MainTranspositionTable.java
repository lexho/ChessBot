package search.hashtables;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import board.Move;
import board.position.Position;
import board.position.bitboard.PositionBB;
import search.Node;
import search.algorithms.AlphaBetaHashSearch;
import search.algorithms.AlphaBetaSearchDouble;
import search.algorithms.integer.AlphaBetaSearchInt;
import search.datastructures.Pair;
import search.evalfunctions.ScoreBitBoard;
import search.evalfunctions.ScoreBitBoardDouble;
import search.evalfunctions.ScoreBoard;
import search.functions.BoardFunction;
import search.functions.BoardFunctionDouble;
import search.hashfunctions.ZobristHash;
import search.nodes.BoardNode;

public class MainTranspositionTable extends ConcurrentHashMap<Long, HashTableEntry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5061293320753325926L;
	public static final int valUNKNOWN = -1;
	public static int PROBE = 0;
	public static int READ = 0;
	public static int WRITE = 0;
	
	public static MainTranspositionTable readHashMapFromFile(String filename) {
		System.out.println("loading hashmap from " + filename);
		MainTranspositionTable hashmap;
		try {
            FileInputStream fileInput = new FileInputStream(
                filename);
  
            ObjectInputStream objectInput
                = new ObjectInputStream(fileInput);
            
            //System.out.println("read hashmap from file");
            hashmap = (MainTranspositionTable)objectInput.readObject();
    		System.out.println("hashmap size: " + hashmap.size());
  
            objectInput.close();
            fileInput.close();
        }
  
        catch (IOException obj1) {
            obj1.printStackTrace();
            System.out.println(obj1);
            return null;
        }
  
        catch (ClassNotFoundException obj2) {
            System.out.println("Class not found");
            obj2.printStackTrace();
            return null;
        }
		System.out.println("hashmap loaded from " + filename);
		return hashmap;
	}
	
	public boolean writeHashMapToFile(String filename) {
		// try catch block
        try {
            FileOutputStream myFileOutStream
                = new FileOutputStream(
                    filename);
  
            ObjectOutputStream myObjectOutStream
                = new ObjectOutputStream(myFileOutStream);
  
            myObjectOutStream.writeObject(this);
            myObjectOutStream.flush();
  
            // closing FileOutputStream and
            // ObjectOutputStream
            myObjectOutStream.close();
            myFileOutStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public Pair<Node, Double> ProbeHash(Node current, int depth, Pair<Node, Double> alpha, Pair<Node, Double> beta) {
		//PROBE++;
		//System.out.println("hashmap contains?: " + current.hashCode64());
		if(this.containsKey(current.hashCode64())) {
			//READ++;
			//System.out.println("hashmap contains key: " + current.hashCode64());
			HashTableEntry entry = (HashTableEntry) this.get(current.hashCode64());
			//return new Pair<Node, Integer>(current, entry.getScore());
			if(entry.getDepth() >= depth) {
				if(entry.checkFlag(HashTableEntry.hashfEXACT))
					return new Pair<Node, Double>(current, entry.getScore());
					//return entry.getPair();
				if(entry.checkFlag(HashTableEntry.hashfALPHA) && entry.getScore() <= alpha.s )
					return alpha;
				if(entry.checkFlag(HashTableEntry.hashfBETA) && entry.getScore() >= beta.s )
					return beta;
			}
			//TODO RememberBestMove();
		}
		return null;
	}
	
	public void put(Node node, HashTableEntry value) {
		//((PositionBB)((BoardNode) node).getState()).printSquares();
		put(node.hashCode64(), value);
	}
	public void put(Pair<Node, Double> pair, HashTableEntry value) {
		Node node = pair.f;
		//((PositionBB)((BoardNode) node).getState()).printSquares();
		put(pair.f.hashCode64(), value);
	}
	
	private void put(long key, HashTableEntry value) {
		//System.out.println("put " + key + " " + value.getScore());
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
	
	public static void main(String[] args) {
		int depth = 12;
		
		PositionBB board = new PositionBB();
		MainTranspositionTable hashmap = new MainTranspositionTable();
		ZobristHash.init(); // init hashing function
		
		// AlphaBeta Search 
		ScoreBoard scoreboard = new ScoreBitBoardDouble(board.copy());
		Function<Node, Double> evalFunction = new BoardFunctionDouble(scoreboard);

		List<Long> times = new ArrayList<Long>();

		long starttime = System.currentTimeMillis();
		MainTranspositionTable.PROBE = 0;
		MainTranspositionTable.READ = 0;
		//MainTranspositionTable.WRITE = 0;
		AlphaBetaSearchDouble alphabeta = new AlphaBetaHashSearch(depth, hashmap, starttime);

		Pair<Node, Double> result = null;
		alphabeta.setBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
		alphabeta.setTime(3000000);
		result = alphabeta.search(
				new BoardNode(board.copy()),
				evalFunction);	
		long time = System.currentTimeMillis() - starttime;
		times.add(time);
		System.out.println("hashmap size: " + hashmap.size());
		System.out.println("search time: " + time + " ms");
		System.out.println("probe: " + MainTranspositionTable.PROBE);
		System.out.println("read: " + MainTranspositionTable.READ);
		System.out.println("write: " + MainTranspositionTable.WRITE);
		System.out.println();
		
		Pair<Node, Double> alpha = new Pair<Node, Double>(null, Double.NEGATIVE_INFINITY);
		Pair<Node, Double> beta = new Pair<Node, Double>(null, Double.POSITIVE_INFINITY);
		
		board = new PositionBB();
		
		// feed hashtable
		//hashmap = new MainTranspositionTable();
		/*for (Move m : board.getPossibleMoves()) {
			Position b = board.copy();
			b.makeMove(m);
			BoardNode current = new BoardNode(b);
			Pair<Node, Integer> val = new Pair<Node, Integer>(current, evalFunction.apply(current));
			hashmap.put(current.hashCode64(), new HashTableEntry(val, depth, HashTableEntry.hashfEXACT)); 
			
		}*/

		// search hashtable
		starttime = System.currentTimeMillis();
		board = new PositionBB();
		System.out.println("HashTable Search");
		Position b = board.copy();
		BoardNode bn = new BoardNode(b);
		for (Node node : bn.adjacent()) {
			//for (Node node : node1.adjacent()) {
				//System.out.println(node.hashCode64());
				//((PositionBB)((BoardNode) node).getState()).printSquares();
				//System.out.println(node.hashCode64());
				Pair<Node, Double> resultHashTable = hashmap.ProbeHash(node, depth, alpha, beta);
				if(resultHashTable != null) {
					//System.out.println(resultHashTable + " ");
					System.out.print(node.getAction() + " " + resultHashTable.s);
					System.out.println();
				}
			//}
		}
		time = System.currentTimeMillis() - starttime;
		System.out.println();
		System.out.println("hashmap size: " + hashmap.size());
		System.out.println("search time: " + time + " ms");
		System.out.println("probe: " + MainTranspositionTable.PROBE);
		System.out.println("read: " + MainTranspositionTable.READ);
		System.out.println("write: " + MainTranspositionTable.WRITE);
		System.out.println();
		
		System.out.println("writing hashmap to file...");
		hashmap.writeHashMapToFile("hashmap.ser");
		System.out.println("finished writing hashmap fo file");
	}
}

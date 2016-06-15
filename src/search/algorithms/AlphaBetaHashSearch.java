package search.algorithms;

import search.Node;
import search.datastructures.Pair;
import search.hashtables.HashTableEntry;
import search.hashtables.MainTranspositionTable;

public class AlphaBetaHashSearch extends AlphaBetaSearch {

	public AlphaBetaHashSearch(int depthLimit, long starttime) {
		super(depthLimit, starttime);
		hashmap = new MainTranspositionTable();
		// TODO Auto-generated constructor stub
	}
	
	public AlphaBetaHashSearch(int depthLimit, MainTranspositionTable hashmap, long starttime) {
		super(depthLimit, starttime);
		this.hashmap = hashmap;
		// TODO Auto-generated constructor stub
	}
	
	private MainTranspositionTable hashmap;
	
	@Override
	Pair<Node, Double> alphaBetaMax( Node current,  Pair<Node, Double> alpha,  Pair<Node, Double> beta, int depthleft ) {
		nodecount++;
		/* Return value from hashmap */
		Pair<Node, Double> val;
		if((val = hashmap.ProbeHash(current, depthleft, alpha, beta)) != null) {
			return val; //hashmap.get(current.hashCode64()).getPair();
		}
		if ( depthleft == 0 || current.isLeaf() ) {
			NrOfLeafNodes++;
			val = new Pair<Node, Double>(current, evalFunction.apply(current));
			// store current node in hashmap
			hashmap.put(current.hashCode64(), new HashTableEntry(val, HashTableEntry.hashfEXACT)); 
			return val;
		}
		   int current_depth = depth;
		   int i = 0;
		   for ( Node node : current.adjacent() ) {
			   depth = current_depth;
			   depth++;
		      double score = alphaBetaMin( node, alpha, beta, depthleft - 1 ).s;
		      printInfoMessage(node, score);
		      if( score >= beta.s ) {
		    	  betaCount += current.adjacent().size() - i;
		    	  // store beta node in hashmap
		    	  try {
		    		  hashmap.put(beta.hashCode64(), new HashTableEntry(beta.s, beta.f.getDepth(), HashTableEntry.hashfBETA));
		    	  } catch (NullPointerException e) {}
		         return beta;   // fail hard beta-cutoff
		      }
		      if( score > alpha.s ) {
		         alpha = new Pair<Node, Double>(node, score); // alpha acts like max in MiniMax
		      }
		   i++;
		   }
		   try {
			   hashmap.put(alpha.hashCode64(), new HashTableEntry(alpha, HashTableEntry.hashfALPHA)); // store alpha node in hashmap
		   } catch (NullPointerException e) {}
		   return alpha;
		}
	
	@Override
	Pair<Node, Double> alphaBetaMin( Node current, Pair<Node, Double> alpha, Pair<Node, Double> beta, int depthleft ) {
		nodecount++;
		/* Return value from hashmap */
		Pair<Node, Double> val;
		if((val = hashmap.ProbeHash(current, depthleft, alpha, beta)) != null) {
			return val; //hashmap.get(current.hashCode64()).getPair();
		}
		if ( depthleft == 0 || current.isLeaf() ) {
			NrOfLeafNodes++;
			val = new Pair<Node, Double>(current, evalFunction.apply(current));
			// store current node in hashmap
			hashmap.put(current.hashCode64(), new HashTableEntry(val, HashTableEntry.hashfEXACT)); 
			return val;
		}
		   int current_depth = depth;
		   int i = 0;
		   for ( Node node : current.adjacent() ) {
			   depth = current_depth;
			   depth++;
			  double score = alphaBetaMax( node, alpha, beta, depthleft - 1 ).s;
			  printInfoMessage(node, score);
		      if( score <= alpha.s ) {
		    	  //System.out.println("alpha: " + score + " <= " + alpha.s);
		    	  // store beta node in hashmap
		    	  alphaCount += current.adjacent().size() - i;
		    	  try {
		    		  hashmap.put(alpha.hashCode64(), new HashTableEntry(alpha, HashTableEntry.hashfALPHA));
		    	  } catch (NullPointerException e) {}
		    	  return alpha; // fail hard alpha-cutoff
		      }
		      if( score < beta.s ) {
		    	  /*System.out.print("set new beta: " + beta.s);
		    	  System.out.println(" at depth: " + depth);*/
		         beta = new Pair<Node, Double>(node, score); // beta acts like min in MiniMax
		      }
		      i++;
		   }
		   try {
			   hashmap.put(beta.hashCode64(), new HashTableEntry(beta, HashTableEntry.hashfBETA)); // store alpha node in hashmap
		   } catch (NullPointerException e) {}
		   return beta;
		}
}

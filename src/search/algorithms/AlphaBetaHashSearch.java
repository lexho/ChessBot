package search.algorithms;

import java.util.HashMap;

import search.Node;
import search.datastructures.Pair;
import search.datastructures.TTEntry;
import search.hashfunctions.ZobristHash;
import search.hashtables.MainTranspositionTable;

public class AlphaBetaHashSearch extends AlphaBetaSearch {

	public AlphaBetaHashSearch(int depthLimit) {
		super(depthLimit);
		hashmap = new MainTranspositionTable();
		// TODO Auto-generated constructor stub
	}
	
	public AlphaBetaHashSearch(int depthLimit, MainTranspositionTable hashmap) {
		super(depthLimit);
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
			return val; //hashmap.get(current.hashCode()).getPair();
		}
		if ( depthleft == 0 || current.isLeaf() ) {
			NrOfLeafNodes++;
			val = new Pair<Node, Double>(current, evalFunction.apply(current));
			// store current node in hashmap
			hashmap.put(current.hashCode(), new TTEntry(val, TTEntry.hashfEXACT)); 
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
		    	  hashmap.put(beta.hashCode(), new TTEntry(beta, TTEntry.hashfBETA));
		         return beta;   // fail hard beta-cutoff
		      }
		      if( score > alpha.s ) {
		         alpha = new Pair<Node, Double>(node, score); // alpha acts like max in MiniMax
		      }
		   i++;
		   }
		   hashmap.put(alpha.hashCode(), new TTEntry(alpha, TTEntry.hashfALPHA)); // store alpha node in hashmap
		   return alpha;
		}
	
	@Override
	Pair<Node, Double> alphaBetaMin( Node current, Pair<Node, Double> alpha, Pair<Node, Double> beta, int depthleft ) {
		nodecount++;
		/* Return value from hashmap */
		Pair<Node, Double> val;
		if((val = hashmap.ProbeHash(current, depthleft, alpha, beta)) != null) {
			return val; //hashmap.get(current.hashCode()).getPair();
		}
		if ( depthleft == 0 || current.isLeaf() ) {
			NrOfLeafNodes++;
			val = new Pair<Node, Double>(current, evalFunction.apply(current));
			// store current node in hashmap
			hashmap.put(current.hashCode(), new TTEntry(val, TTEntry.hashfEXACT)); 
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
		    	  hashmap.put(alpha.hashCode(), new TTEntry(alpha, TTEntry.hashfALPHA));
		         return alpha; // fail hard alpha-cutoff
		      }
		      if( score < beta.s ) {
		    	  /*System.out.print("set new beta: " + beta.s);
		    	  System.out.println(" at depth: " + depth);*/
		         beta = new Pair<Node, Double>(node, score); // beta acts like min in MiniMax
		      }
		      i++;
		   }
		   hashmap.put(beta.hashCode(), new TTEntry(beta, TTEntry.hashfBETA)); // store alpha node in hashmap
		   return beta;
		}
}

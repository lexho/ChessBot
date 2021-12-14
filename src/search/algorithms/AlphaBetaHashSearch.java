package search.algorithms;

import search.Node;
import search.datastructures.Pair;
import search.hashtables.HashTableEntry;
import search.hashtables.MainTranspositionTable;

public class AlphaBetaHashSearch extends AlphaBetaSearchDouble {

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
	Pair<Node, Double> alphaBetaMax( Node current,  Pair<Node, Double> alpha,  Pair<Node, Double> beta, int depthleft  ) {
		nodecount++;
		/* Return value from hashmap */
		Pair<Node, Double> val;
		if((val = hashmap.ProbeHash(current, depthleft, alpha, beta)) != null) {
			return val; //hashmap.get(current.hashCode64()).getPair();
		}
		if ( depthleft == 0 || current.isLeaf() || timeIsUp() || interrupted) {
			resultState = 0; // reset result state;
			if(depthleft == 0) resultState += 4;
			if(current.isLeaf()) resultState += 8;
			if(timeIsUp()) resultState += 16;
			if(interrupted) resultState +=32;
			NrOfLeafNodes++;
			val = new Pair<Node, Double>(current, evalFunction.apply(current));
			// store current node in hashmap
			if(!current.isLeaf())
				hashmap.put(current, new HashTableEntry(val, this.depthLimit, HashTableEntry.hashfEXACT)); 
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
		    		  hashmap.put(beta.f, new HashTableEntry(beta.s, this.depthLimit, HashTableEntry.hashfBETA));
		    	  } catch (NullPointerException e) {
		    		 e.printStackTrace();
		    	  }
		         return beta;   // fail hard beta-cutoff
		      }
		      if( score > alpha.s ) {
		         alpha = new Pair<Node, Double>(node, score); // alpha acts like max in MiniMax
		      }
		   }
		   i++;
		   try {
			   hashmap.put(alpha.f, new HashTableEntry(alpha, this.depthLimit, HashTableEntry.hashfALPHA)); // store alpha node in hashmap
		   } catch (NullPointerException e) {
			   e.printStackTrace();
		   }
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
		if ( depthleft == 0 || current.isLeaf() || timeIsUp() || interrupted) {
			resultState = 0; // reset result state;
			if(depthleft == 0) resultState += 4;
			if(current.isLeaf()) resultState += 8;
			if(timeIsUp()) resultState += 16;
			if(interrupted) resultState +=32;
			NrOfLeafNodes++;
			val = new Pair<Node, Double>(current, evalFunction.apply(current));
			// store current node in hashmap
			if(!current.isLeaf())
				hashmap.put(current, new HashTableEntry(val, this.depthLimit, HashTableEntry.hashfEXACT)); 
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
		    		  hashmap.put(alpha, new HashTableEntry(alpha, this.depthLimit, HashTableEntry.hashfALPHA));
		    	  } catch (NullPointerException e) {
		    		  e.printStackTrace();
		    	  }
		    	  return alpha; // fail hard alpha-cutoff
		      }
		      if( score < beta.s ) {
		    	  /*System.out.print("set new beta: " + beta.s);
		    	  System.out.println(" at depth: " + depth);*/
		         beta = new Pair<Node, Double>(node, score); // beta acts like min in MiniMax
		      }
		   }
		   i++;
		   try {
			   hashmap.put(beta, new HashTableEntry(beta, this.depthLimit, HashTableEntry.hashfBETA)); // store alpha node in hashmap
		   } catch (NullPointerException e) {
			   e.printStackTrace();
		   }
		   return beta;
		}
}

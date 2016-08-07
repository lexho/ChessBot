package search.algorithms;

import java.util.ArrayList;
//import java.io.Printwriter;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import exceptions.SearchFailException;
import search.AdversarialSearch;
import search.Node;
import search.datastructures.Pair;
import search.nodes.BoardNode;

public class AlphaBetaSearchInt implements AdversarialSearch {
	@SuppressWarnings("unused")
	private BiPredicate<Integer, Node> searchLimitingPredicate;
	private int depthLimit;
	private final boolean VERBOSE = true;

	/**
	 * To limit the extent of the search, this implementation should honor a
	 * limiting predicate. The predicate returns 'true' as long as we are below the limit,
	 * and 'false', if we exceed the limit.
	 * 
	 * @param searchLimitingPredicate
	 */
	public AlphaBetaSearchInt(BiPredicate<Integer, Node> searchLimitingPredicate)
	{
		this.searchLimitingPredicate = searchLimitingPredicate;
		/*try {
			writer = new Print//writer(new FileOutputStream(new File("/home/alex/Code/workspace_java/aiws15_2/assets/log/" + "alphabeta.log"),false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		lastinfo = System.currentTimeMillis();
		interrupted = false; // reset interrupt
		alphaBound = Integer.MIN_VALUE;
		betaBound = Integer.MAX_VALUE;
	}
	
	public AlphaBetaSearchInt(int depthLimit, long starttime)
	{
		this.depthLimit = depthLimit;
		this.starttime = starttime;
		/*try {
			writer = new Print//writer(new FileOutputStream(new File("/home/alex/Code/workspace_java/aiws15_2/assets/log/" + "alphabeta.log"),false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		lastinfo = System.currentTimeMillis();
		interrupted = false; // reset interrupt
		alphaBound = Integer.MIN_VALUE;
		betaBound = Integer.MAX_VALUE;
	}
	
	//private double alpha = Double.NEGATIVE_INFINITY;
	//private double beta = Double.POSITIVE_INFINITY;
	
	protected int depth = 0;
	private boolean interrupted;
	
	public long nodecount = 0;
	private long starttime;
	private long lastinfo = 0; // the last time a info message was printed
	private long lastnodes = 0; // the number of processed nodes at the last time a info message was printed
	private int lastdepth = 0; // the depth at last depth message
	protected int NrOfLeafNodes = 0;
	
	public int getLeafNodeStatistics() {
		return NrOfLeafNodes;
	}
	
	/* Debug*/
	//Printwriter writer;
	
	private int alphaBound;
	private int betaBound;
	
	public void setBounds(int alpha, int beta) {
		this.alphaBound = alpha;
		this.betaBound = beta;
	}
	
	public double[] getBounds() {
		return new double[]{alphaBound, betaBound};
	}
	
	Function<Node, Integer> evalFunction;

	public Pair<Node, Integer> search(Node start, Function<Node, Integer> evalFunction) throws SearchFailException {
		lastinfo = System.currentTimeMillis();
		
		Pair<Node, Integer> alpha = new Pair<Node, Integer>(null, alphaBound); //Double.POSITIVE_INFINITY;
		Pair<Node, Integer> beta =  new Pair<Node, Integer>(null, betaBound); // Double.NEGATIVE_INFINITY;
		depth = 0;
		Node current = start;
		this.evalFunction = evalFunction;

		Pair<Node, Integer> result = alphaBetaMax( start, alpha, beta, depthLimit);
		
		/* Alpha- and Beta-Fails*/
		if(result.f == null) {
			if(result.s == alphaBound) throw new SearchFailException(true);
			else throw new SearchFailException(false);
		}
		return result;
	}
	
	protected int alphaCount = 0;
	protected int betaCount = 0;
	
	public List<Integer> getPruningStats() {
		List<Integer> stats = new ArrayList<Integer>();
		stats.add(alphaCount);
		stats.add(betaCount);
		return stats;
	}
	
	Pair<Node, Integer> alphaBetaMax( Node current,  Pair<Node, Integer> alpha,  Pair<Node, Integer> beta, int depthleft ) {
		nodecount++;   
		if ( depthleft == 0 || current.isLeaf() ) {
			NrOfLeafNodes++;
			return new Pair<Node, Integer>(current, evalFunction.apply(current));
		}
		   int current_depth = depth;
		   int i = 0;
		   for ( Node node : current.adjacent() ) {
			   depth = current_depth;
			   depth++;
		      int score = alphaBetaMin( node, alpha, beta, depthleft - 1 ).s;
		      printInfoMessage(node, score);
		      if( score >= beta.s ) {
		    	  //System.out.println("beta: " + score + " >= " + beta.s);
		    	  betaCount += current.adjacent().size() - i;
		    	  //System.out.println("beta prune " + (current.adjacent().size() - i) + " of " + current.adjacent().size());
		         return beta;   // fail hard beta-cutoff
		      }
		      if( score > alpha.s ) {
		    	  /*System.out.print("set new alpha: " + alpha.s);
		    	  System.out.println(" at depth: " + depth);*/
		         alpha = new Pair<Node, Integer>(node, score); // alpha acts like max in MiniMax
		      }
		   i++;
		   }
		   return alpha;
		}
		 
	Pair<Node, Integer> alphaBetaMin( Node current, Pair<Node, Integer> alpha, Pair<Node, Integer> beta, int depthleft ) {
		nodecount++;   
		if ( depthleft == 0 || current.isLeaf() ) {
			NrOfLeafNodes++;
			return new Pair<Node, Integer>(current, -evalFunction.apply(current));
		}
		   int current_depth = depth;
		   int i = 0;
		   for ( Node node : current.adjacent() ) {
			   depth = current_depth;
			   depth++;
			  int score = alphaBetaMax( node, alpha, beta, depthleft - 1 ).s;
			  printInfoMessage(node, score);
		      if( score <= alpha.s ) {
		    	  //System.out.println("alpha: " + score + " <= " + alpha.s);
		    	  alphaCount += current.adjacent().size() - i;
		         return alpha; // fail hard alpha-cutoff
		      }
		      if( score < beta.s ) {
		    	  /*System.out.print("set new beta: " + beta.s);
		    	  System.out.println(" at depth: " + depth);*/
		         beta = new Pair<Node, Integer>(node, score); // beta acts like min in MiniMax
		      }
		      i++;
		   }
		   return beta;
		}

	/** interrupt a running alphabeta search */
	public void interrupt() {
		interrupted = true;
	}
	
	protected void printInfoMessage(Node node, double score) {
		if(VERBOSE) {
			if(System.currentTimeMillis() - lastinfo  > 1000) {
				long nps = (nodecount - lastnodes); // Nodes per second
				lastnodes = nodecount;
				long time = System.currentTimeMillis() - starttime;
				// info depth 8 seldepth 9 multipv 1 score cp 334 nodes 4943 nps 617875 tbhits 0 time 8 pv e4f3 d1f3 e7e5 b1c3 f8d6 e1g1 d6c5 c4b3
				System.out.println("info " + "depth " + depth + " seldepth " + depth + " multipv 1 " + "score cp " + score + " nodes " + nodecount + " nps " + nps + " tbhits 0 " + "time " + time + " pv " + ((BoardNode) node).getFullAction()); 
				//System.out.println("info " + "currmove "+ ((BoardNode) node).getFullAction() + "score " + score + " depth " + depth + " nodes " + nodecount + " nps " + nps);
				lastinfo = System.currentTimeMillis();
			}
		}
	}
}

package search.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
//import java.io.Printwriter;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import board.Move;
import exceptions.SearchFailException;
import search.AdversarialSearch;
import search.Node;
import search.datastructures.Pair;
import search.nodes.BoardNode;
import search.nodes.LimitedNode;

public class AlphaBetaSearch implements AdversarialSearch {
	@SuppressWarnings("unused")
	private BiPredicate<Integer, Node> searchLimitingPredicate;
	private int depthLimit;

	/**
	 * To limit the extent of the search, this implementation should honor a
	 * limiting predicate. The predicate returns 'true' as long as we are below the limit,
	 * and 'false', if we exceed the limit.
	 * 
	 * @param searchLimitingPredicate
	 */
	public AlphaBetaSearch(BiPredicate<Integer, Node> searchLimitingPredicate)
	{
		this.searchLimitingPredicate = searchLimitingPredicate;
		/*try {
			writer = new Print//writer(new FileOutputStream(new File("/home/alex/Code/workspace_java/aiws15_2/assets/log/" + "alphabeta.log"),false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		lastinfo = System.currentTimeMillis();
		interrupted = false; // reset interrupt
		alphaBound = Double.NEGATIVE_INFINITY;
		betaBound = Double.POSITIVE_INFINITY;
	}
	
	public AlphaBetaSearch(int depthLimit)
	{
		this.depthLimit = depthLimit;
		/*try {
			writer = new Print//writer(new FileOutputStream(new File("/home/alex/Code/workspace_java/aiws15_2/assets/log/" + "alphabeta.log"),false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		lastinfo = System.currentTimeMillis();
		interrupted = false; // reset interrupt
		alphaBound = Double.NEGATIVE_INFINITY;
		betaBound = Double.POSITIVE_INFINITY;
	}
	
	//private double alpha = Double.NEGATIVE_INFINITY;
	//private double beta = Double.POSITIVE_INFINITY;
	
	private int getDepth(Node node) {
		int depth = 0;
		while(!node.isRoot()) {
			node = node.parent();
			depth++;
		}
		return depth;
	}
	
	private void indent(int depth) {
		for(int i = 0; i < depth; i++) {
			//writer.print(" ");
		}
	}
	
	protected int depth = 0;
	private boolean interrupted;
	
	public long nodecount = 0;
	private long lastinfo = 0; // the last time a info message was printed
	private long lastnodes = 0; // the number of processed nodes at the last time a info message was printed
	private int lastdepth = 0; // the depth at last depth message
	protected int NrOfLeafNodes = 0;
	
	public int getLeafNodeStatistics() {
		return NrOfLeafNodes;
	}
	
	/* Debug*/
	//Printwriter writer;
	
	private double alphaBound;
	private double betaBound;
	
	public void setBounds(double alpha, double beta) {
		this.alphaBound = alpha;
		this.betaBound = beta;
	}
	
	public double[] getBounds() {
		return new double[]{alphaBound, betaBound};
	}
	
	Function<Node, Double> evalFunction;

	public Pair<Node, Double> search(Node start, Function<Node, Double> evalFunction) throws SearchFailException {
		Pair<Node, Double> alpha = new Pair<Node, Double>(null, alphaBound); //Double.POSITIVE_INFINITY;
		Pair<Node, Double> beta =  new Pair<Node, Double>(null, betaBound); // Double.NEGATIVE_INFINITY;
		depth = 0;
		Node current = start;
		lastinfo = System.currentTimeMillis();
		this.evalFunction = evalFunction;

		Pair<Node, Double> result = alphaBetaMax( start, alpha, beta, depthLimit);
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
	
	Pair<Node, Double> alphaBetaMax( Node current,  Pair<Node, Double> alpha,  Pair<Node, Double> beta, int depthleft ) {
		nodecount++;   
		if ( depthleft == 0 || current.isLeaf() ) {
			NrOfLeafNodes++;
			return new Pair<Node, Double>(current, evalFunction.apply(current));
		}
		   int current_depth = depth;
		   int i = 0;
		   for ( Node node : current.adjacent() ) {
			   depth = current_depth;
			   depth++;
		      double score = alphaBetaMin( node, alpha, beta, depthleft - 1 ).s;
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
		         alpha = new Pair<Node, Double>(node, score); // alpha acts like max in MiniMax
		      }
		   i++;
		   }
		   return alpha;
		}
		 
	Pair<Node, Double> alphaBetaMin( Node current, Pair<Node, Double> alpha, Pair<Node, Double> beta, int depthleft ) {
		nodecount++;   
		if ( depthleft == 0 || current.isLeaf() ) {
			NrOfLeafNodes++;
			return new Pair<Node, Double>(current, evalFunction.apply(current));
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
		    	  alphaCount += current.adjacent().size() - i;
		         return alpha; // fail hard alpha-cutoff
		      }
		      if( score < beta.s ) {
		    	  /*System.out.print("set new beta: " + beta.s);
		    	  System.out.println(" at depth: " + depth);*/
		         beta = new Pair<Node, Double>(node, score); // beta acts like min in MiniMax
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
		if(System.currentTimeMillis() - lastinfo  > 1000) {
			long nps = (nodecount - lastnodes); // Nodes per second
			lastnodes = nodecount;
			System.out.println("info " + "currmove "+ ((BoardNode) node).getFullAction() + "score " + score + " depth " + depth + " nodes " + nodecount + " nps " + nps);
			lastinfo = System.currentTimeMillis();
		}
	}
}

package search.algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.Printwriter;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import search.AdversarialSearch;
import search.Node;
import search.datastructures.Pair;

public class AlphaBetaSearch implements AdversarialSearch {
	@SuppressWarnings("unused")
	private BiPredicate<Integer, Node> searchLimitingPredicate;

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
	
	private int depth = 0;
	public long nodecount = 0;
	
	/* Debug*/
	//Printwriter writer;

	public Pair<Node, Double> search(Node start, Function<Node, Double> evalFunction) {
		double alpha = Double.POSITIVE_INFINITY;
		double beta = Double.NEGATIVE_INFINITY;
		Node current = start;
		nodecount++;
		
		//if(depth != d) System.out.println("Depth-Error!");
		//System.out.println("alphabeta search depth: " + depth);
		
		/* Recursively score nodes up to start node */
		if(current.isLeaf() || !searchLimitingPredicate.test(depth, current)) {
			double score = evalFunction.apply(current);
			
			return new Pair(current,score); // return evaluated leaf node
		} else {
			List<Node> adjacent = current.adjacent();
			Pair<Node, Double> bestNode = null; // init best score
			int current_depth = depth;
			for(int i = 0; i < adjacent.size(); i++) {
				/* Get MinMax-Score of child node */
				depth = current_depth;
				depth++;
				double score = search(adjacent.get(i), evalFunction).s;
				depth = current_depth;
				//if(depth != getDepth(current)) System.out.println("Depth-Error! " + depth + " != " + getDepth(current));
				
				/* Set alpha/beta */
				if(depth % 2 == 0) {
					if(score < alpha) alpha = score; 
				} else {
					if(score > beta) beta = score;
				}
				
				if(i == 0) bestNode = new Pair(adjacent.get(i),score); // init best score with first node score
				
				/* Check who's turn it is and minimize / maximize minmax score */
				if(depth % 2 == 0) {
					if(score > bestNode.s) bestNode = new Pair(adjacent.get(i),score); // get MAX score
				} else {
					if(score < bestNode.s) bestNode = new Pair(adjacent.get(i),score); // get MIN score
				}
				
				/* Alpha-Beta Pruning */
				if(depth % 2 == 0) {
					//System.out.println(score + ", alpha " + alpha);
					if(score < alpha) {
						return bestNode;
					}
				} else {
					//System.out.println(score + ", beta " + beta);
					if(score > beta) {
						return bestNode;
					}
				}
			}
			return bestNode; // return node with best minmax score
		}
	}
}

package search.algorithms;
import java.util.function.BiPredicate;
import java.util.function.Function;

import search.AdversarialSearch;
import search.Node;
import search.datastructures.Pair;

public class AlphaBetaSearch2 implements AdversarialSearch<Double> {
	@SuppressWarnings("unused")
	private BiPredicate<Integer, Node> searchLimitingPredicate;

	/**
	 * To limit the extent of the search, this implementation should honor a
	 * limiting predicate. The predicate returns 'true' as long as we are below the limit,
	 * and 'false', if we exceed the limit.
	 * 
	 * @param searchLimitingPredicate
	 */
	public AlphaBetaSearch2(BiPredicate<Integer, Node> searchLimitingPredicate)
	{
		this.searchLimitingPredicate = searchLimitingPredicate;
	}
	
	private int depth = 0;

	public Pair<Node, Double> search(Node start, Function<Node, Double> evalFunction) {
		double alpha = Double.POSITIVE_INFINITY;
		double beta = Double.NEGATIVE_INFINITY;
		Node current = start;
		
		//if(depth != d) System.out.println("Depth-Error!");
		
		/* Recursively score nodes up to start node */
		if(current.isLeaf() || !searchLimitingPredicate.test(depth, current)) {
			double score = evalFunction.apply(current);
			//System.out.println("score: " + score);
			return new Pair(current,score); // return evaluated leaf node
		} else {
			Pair<Node, Double> bestNode = null; // init best score
			int current_depth = depth;
			for(int i = 0; i < current.adjacent().size(); i++) {
				//System.out.println("adj");
				/* Get MinMax-Score of child node */
				depth = current_depth;
				depth++;
				double score = search(current.adjacent().get(i), evalFunction).s;
				depth = current_depth;
				//if(depth != getDepth(current)) System.out.println("Depth-Error! " + depth + " != " + getDepth(current));
				
				/* Set alpha/beta */
				if(depth % 2 == 0) {
					if(score < alpha) alpha = score; 
				} else {
					if(score > beta) beta = score;
				}
				
				if(i == 0) bestNode = new Pair(current.adjacent().get(i),score); // init best score with first node score
				
				// Check who's turn it is and minimize / maximize minmax score
				if(depth % 2 == 0) {
					if(score > bestNode.s) bestNode = new Pair(current.adjacent().get(i),score); // get MAX score
				} else {
					if(score < bestNode.s) bestNode = new Pair(current.adjacent().get(i),score); // get MIN score
				}
				
				/* Alpha-Beta Pruning */
				/*if(depth % 2 == 0) {
					//System.out.println(score + ", alpha " + alpha);
					if(score <= alpha) {
						return new Pair<Node, Double>(current.adjacent().get(i), score);
					}
					if(score < beta) {
						return new Pair<Node, Double>(current.adjacent().get(i), score);
					}
				} else {
					//System.out.println(score + ", beta " + beta);
					if(score >= beta) {
						return new Pair<Node, Double>(current.adjacent().get(i), score);
					}
					if(score > alpha) {
						return new Pair<Node, Double>(current.adjacent().get(i), score);
					}
				}*/
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
			//System.out.println("alpha " + current + " " + alpha);
			return bestNode; // return node with best minmax score
		}
	}
}

package search.algorithms;

import java.util.function.BiPredicate;
import java.util.function.Function;

import board.position.bitboard.PositionBB;
import exceptions.SearchFailException;
import search.AdversarialSearch;
import search.Node;
import search.datastructures.Pair;
import search.nodes.BoardNode;

public class MinMaxSearchDouble implements AdversarialSearch<Double> {
	@SuppressWarnings("unused")
	private BiPredicate<Double, Node> searchLimitingPredicate;
	protected int depthLimit;
	protected final boolean VERBOSE = true;

	/**
	 * To limit the extent of the search, this implementation should honor a
	 * limiting predicate. The predicate returns 'true' as long as we are below the limit,
	 * and 'false', if we exceed the limit.
	 * 
	 * @param searchLimitingPredicate
	 */
	public MinMaxSearchDouble(BiPredicate<Double, Node> searchLimitingPredicate)
	{
		this.searchLimitingPredicate = searchLimitingPredicate;
		lastinfo = System.currentTimeMillis();
		interrupted = false; // reset interrupt
		this.starttime = System.currentTimeMillis();
	}
	
	public MinMaxSearchDouble(int depthLimit, long starttime)
	{
		this.depthLimit = depthLimit;
		this.starttime = starttime;
		lastinfo = System.currentTimeMillis();
		interrupted = false; // reset interrupt
	}
	
	protected int depth = 0;
	protected boolean interrupted;
	
	public static long nodecount = 0;
	protected long starttime;
	protected static long lastinfo = 0; // the last time a info message was printed
	protected static long lastnodes = 0; // the number of processed nodes at the last time a info message was printed
	private int lastdepth = 0; // the depth at last depth message
	protected static int NrOfLeafNodes = 0;
	
	public int getLeafNodeStatistics() {
		return NrOfLeafNodes;
	}
	
	/* Debug*/
	//Printwriter writer;
	
	/** remaining time to calculate the move */
	private int time;
	
	/** set the remaining time to calculate the move */
	public void setTime(int time) {
		this.time = time;
	}
	
	Function<Node, Double> evalFunction;

	public Pair<Node, Double> search(Node start, Function<Node, Double> evalFunction) throws SearchFailException {
		nodecount = 0;
		lastinfo = System.currentTimeMillis();
		
		depth = 0;
		Node current = start;
		
		PositionBB b = (PositionBB) current.getState();
		moveNr = b.getMoveNr();
		
		this.evalFunction = evalFunction;

		if(start == null) System.err.println("start node is null");
		if(start.getAction() == null) System.err.println("start node action is null");

		Pair<Node, Double> result = max( start, depthLimit);
		
		if(result == null) System.err.println("result node is null");
		if(result.f.getAction() == null) System.err.println("result node action is null");

		return result;
	}

	int moveNr;
	
	public static final int approxRemainingNrOfMoves = 50; // 100 - moveNr;
	
	/** to determine the time where it is best to stop calculating and return bestmove */
	public boolean timeIsUp() {
		//TODO improve algo
		
		int timePerMove = time / approxRemainingNrOfMoves;
		//System.out.println("time: " + time);
		//System.out.println("approxRemainingNrOfMoves: " + approxRemainingNrOfMoves);
		//System.out.println("time: " + time);
		long timePassed = System.currentTimeMillis() - starttime;
		if(timePassed >= timePerMove) {
			//System.out.println(time + " time is up! timePassed: " + timePassed + "ms " + timePerMove + " ms/move");
			return true;
		}
		return false;
	}
	
	int resultState = 0;
	
	
	public int getResultState() {
		return resultState;
	}
	
	Pair<Node, Double> max( Node current,  int depthleft ) {
		//System.out.println("max " +  current.getAction());
		nodecount++;   
		if ( depthleft == 0 || current.isLeaf() || timeIsUp() || interrupted) {
			resultState = 0; // reset result state;
			if(depthleft == 0) resultState += 4;
			if(current.isLeaf()) resultState += 8;
			if(timeIsUp()) resultState += 16;
			if(interrupted) resultState +=32;
			//System.out.println("result state: " + getResultState());
			//System.exit(-1);
			NrOfLeafNodes++;
			//System.out.print("leaf ");
			//System.out.println(" score: " + evalFunction.apply(current));
			if(current.getAction() == null) System.err.println("leaf node action is null");
			return new Pair<Node, Double>(current, evalFunction.apply(current));
		}
		int current_depth = depth;
		int i = 0;
		double bestScore;
		PositionBB pos = (PositionBB) current.getState();
		//System.out.println("depth left " + depthleft + " init with MIN");
		bestScore = Integer.MIN_VALUE;
		Node bestNode = null;
		for ( Node node : current.adjacent() ) {
			//System.out.println("adj");
			depth = current_depth;
			depth++;
			double score;
			pos = (PositionBB) current.getState();
			score = min( node, depthleft - 1 ).s;
			//System.out.println("maximize " + node.getAction() + " " + score);

			if(score > bestScore) {// replace best node		
				bestNode = node;
				bestScore = score;
				//System.out.println("new best: " + bestNode.getAction() + " " + bestScore);
			}
					
			printInfoMessage(node, score);
			i++;
		}
		return new Pair<Node, Double>(bestNode, bestScore);
	}

	Pair<Node, Double> min( Node current,  int depthleft ) {
		//System.out.println("max " +  current.getAction());
		nodecount++;   
		if ( depthleft == 0 || current.isLeaf() || timeIsUp() || interrupted) {
			resultState = 0; // reset result state;
			if(depthleft == 0) resultState += 4;
			if(current.isLeaf()) resultState += 8;
			if(timeIsUp()) resultState += 16;
			if(interrupted) resultState +=32;
			//System.out.println("result state: " + getResultState());
			//System.exit(-1);
			NrOfLeafNodes++;
			//System.out.print("leaf ");
			//System.out.println(" score: " + evalFunction.apply(current));
			if(current.getAction() == null) System.err.println("leaf node action is null");
			return new Pair<Node, Double>(current, evalFunction.apply(current));
		}
		int current_depth = depth;
		int i = 0;
		double bestScore;
		PositionBB pos = (PositionBB) current.getState();
		//System.out.println("depth left " + depthleft + " init with MAX");
		bestScore = Integer.MAX_VALUE; 
		Node bestNode = null;
		for ( Node node : current.adjacent() ) {
			//System.out.println("adj");
			depth = current_depth;
			depth++;
			double score;
			pos = (PositionBB) current.getState();
			score = max( node, depthleft - 1 ).s;
			//System.out.println("minimize " + node.getAction() + " " + score);
			
			if(score < bestScore) {// replace best node		
				bestNode = node;
				bestScore = score;
				//System.out.println("new best: " + bestNode.getAction() + " " + bestScore);
			}
					
			printInfoMessage(node, score);
			i++;
		}
		return new Pair<Node, Double>(bestNode, bestScore);
	}

	/** interrupt a running minmax search */
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
	
	public void printInfoMessage() {
		long nps = (nodecount - lastnodes); // Nodes per second
		lastnodes = nodecount;
		long time = System.currentTimeMillis() - starttime;
		System.out.println("info " + "depth " + depth + " seldepth " + depth + " multipv 1 " + "score cp " + "--" + " nodes " + nodecount + " nps " + nps + " tbhits 0 " + "time " + time + " pv " + " ----");
		lastinfo = System.currentTimeMillis();
	}
}

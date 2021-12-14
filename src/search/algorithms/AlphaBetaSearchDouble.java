package search.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiPredicate;
import java.util.function.Function;

import board.Board;
import board.Move;
import board.position.Position;
import board.position.bitboard.PositionBB;
import exceptions.SearchFailException;
import search.AdversarialSearch;
import search.Node;
import search.datastructures.Pair;
import search.evalfunctions.ScoreBitBoard;
import search.evalfunctions.ScoreBoard;
import search.functions.BoardFunction;
import search.nodes.BoardNode;

public class AlphaBetaSearchDouble extends MinMaxSearchDouble implements AdversarialSearch<Double> {

	/**
	 * To limit the extent of the search, this implementation should honor a
	 * limiting predicate. The predicate returns 'true' as long as we are below the limit,
	 * and 'false', if we exceed the limit.
	 * 
	 * @param searchLimitingPredicate
	 */
	public AlphaBetaSearchDouble(BiPredicate<Double, Node> searchLimitingPredicate)
	{
		super(searchLimitingPredicate);
		alphaBound = Double.NEGATIVE_INFINITY;
		betaBound = Double.POSITIVE_INFINITY;
	}
	
	public AlphaBetaSearchDouble(int depthLimit, long starttime)
	{
		super(depthLimit, starttime);
		this.depthLimit = depthLimit;
		this.starttime = starttime;
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
	
	
	/* Debug*/
	//Printwriter writer;
	
	private double alphaBound;
	private double betaBound;
	
	public void setBounds(double d, double e) {
		this.alphaBound = d;
		this.betaBound = e;
	}

	public double[] getBounds() {
		return new double[]{alphaBound, betaBound};
	}

	@Override
	public Pair<Node, Double> search(Node start, Function<Node, Double> evalFunction) throws SearchFailException {
		nodecount = 0;
		lastinfo = System.currentTimeMillis();
		
		/*System.out.println("init random node");
		Random rnd = new Random();
		int r = rnd.nextInt(start.adjacent().size());
		Node randomNode = start.adjacent().get(r);
		if(randomNode.getAction() == null) System.err.println("rnd node action is null");
		Move rndMove = randomNode.getAction();*/
		
		Pair<Node, Double> alpha = new Pair<Node, Double>(null, alphaBound); //Double.POSITIVE_INFINITY;
		Pair<Node, Double> beta =  new Pair<Node, Double>(null, betaBound); // Double.NEGATIVE_INFINITY;
		depth = 0;
		Node current = start;
		//start = new BoardNode(null, rndMove, start.getState());
		
		PositionBB b = (PositionBB) current.getState();
		moveNr = b.getMoveNr();
		
		this.evalFunction = evalFunction;

		if(start == null) System.err.println("start node is null");
		//if(start.getAction() == null) System.err.println("start node action is null");
		//if(alpha.f.getAction() == null) System.err.println("alpha node action is null");
		//if(beta.f.getAction() == null) System.err.println("beta node action is null");
		
		PositionBB pos = (PositionBB) current.getState();
		
		Pair<Node, Double> result = alphaBetaMax( start, alpha, beta, depthLimit);
		//Pair<Node, Integer> result = max( start, depthLimit);
		
		System.err.println("search result state: " + getResultState());
		if((getResultState() & 0b10000) == 16) System.out.println("search failed on time");
		//if(getResultState() != 0) System.exit(-1);
		
		//Pair<Node, Integer> result = new Pair(randomNode, 0);
		if(result == null) System.err.println("result node is null");
		if(result.f.getAction() == null) System.err.println("result node action is null");
		//System.out.println("result: " + result);
		//System.out.println("result: " + result.f);
		
		/* Alpha- and Beta-Fails*/
		if(result.f == null) {
			System.out.println("Alpha-Beta-Fail");
			if(result.s == alphaBound) throw new SearchFailException(true);
			else throw new SearchFailException(false);
		}
		return result;
	}
	
	protected int alphaCount = 0;
	protected int betaCount = 0;
	int moveNr;
	
	public List<Integer> getPruningStats() {
		List<Integer> stats = new ArrayList<Integer>();
		stats.add(alphaCount);
		stats.add(betaCount);
		return stats;
	}
	

	Pair<Node, Double> alphaBetaMax( Node current,  Pair<Node, Double> alpha,  Pair<Node, Double> beta, int depthleft ) {
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
			if(current.getAction() == null) System.err.println("leaf node action is null");
			//System.out.println(depth + " " + current.getAction() + " " + evalFunction.apply(current));
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
				resultState += 2;
				//System.out.println("beta fail");
				//System.exit(-1);
				return new Pair<Node, Double>(node, score);   // fail hard beta-cutoff
			}
			if( score > alpha.s ) {
				/*System.out.print("set new alpha: " + alpha.s);
		    	  System.out.println(" at depth: " + depth);*/
				alpha = new Pair<Node, Double>(node, score); // alpha acts like max in MiniMax
			}
			i++;
		}
		resultState += 1;
		return alpha;
	}

	Pair<Node, Double> alphaBetaMin( Node current, Pair<Node, Double> alpha, Pair<Node, Double> beta, int depthleft ) {
		nodecount++;
		if ( depthleft == 0 || current.isLeaf() || timeIsUp() || interrupted) {
			resultState = 0; // reset result state;
			if(depthleft == 0) resultState += 4;
			if(current.isLeaf()) resultState += 8;
			if(timeIsUp()) resultState += 16;
			if(interrupted) resultState +=32;
			NrOfLeafNodes++;
			if(current.getAction() == null) System.err.println("leaf node action is null");
			//System.out.println(depth + " " + current.getAction() + " " + evalFunction.apply(current));
			//int scorei = evalFunction.apply(current);
			//double scored = evalFunction.apply(current);
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
				resultState += 1;
				return new Pair<Node, Double>(node, score); // fail hard alpha-cutoff
			}
			if( score < beta.s ) {
				/*System.out.print("set new beta: " + beta.s);
		    	  System.out.println(" at depth: " + depth);*/
				beta = new Pair<Node, Double>(node, score); // beta acts like min in MiniMax
			}
			i++;
		}
		resultState += 2;
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
	
	public void printInfoMessage() {
		long nps = (nodecount - lastnodes); // Nodes per second
		lastnodes = nodecount;
		long time = System.currentTimeMillis() - starttime;
		System.out.println("info " + "depth " + depth + " seldepth " + depth + " multipv 1 " + "score cp " + "--" + " nodes " + nodecount + " nps " + nps + " tbhits 0 " + "time " + time + " pv " + " ----");
		lastinfo = System.currentTimeMillis();
	}
}

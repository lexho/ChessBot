package search;

import java.util.List;

public interface Node {
	<State> State getState();
	<Action> Action getAction();
	
	Node parent();
	List<Node> adjacent();

	boolean isRoot();
	boolean isLeaf();
	
	public int getDepth();

	public int hashCode();
	public long hashCode64();
	public boolean equals(Object obj);
}

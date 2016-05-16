package search;

import java.util.function.Predicate;

public interface Search
{
	Node search(Node start, Predicate<Node> isEnd);
}
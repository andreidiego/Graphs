package improvedgraph.util;

import java.util.Collection;

public interface Stack<E> extends Collection<E> {
	void push(E object);
	E pop();
}
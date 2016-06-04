package firstgraph;

import java.util.LinkedList;
import java.util.List;

public abstract class Graph {

	private int numVertices;
	private int numEdges;

	public Graph() {
		numVertices = 0;
		numEdges = 0;
	}

	public int getNumVertices() {
		return numVertices;
	}

	public int getNumEdges() {
		return numEdges;
	}

	public int addVertex() {
		implementAddVertex();
		numVertices++;
		return numVertices - 1;
	}

	public abstract void implementAddVertex();

	public void addEdge(int v, int w) {
		numEdges++;
		if (v < numVertices && w < numVertices) {
			implementAddEdge(v, w);
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	public abstract void implementAddEdge(int v, int w);

	public abstract List<Integer> getNeighbors(int v);

	public abstract List<Integer> getInNeighbors(int v);

	public List<Integer> degreeSequence() {
		List<Integer> degreeSequence = new LinkedList<>();

		for (int i = 0; i < getNumVertices(); i++) {
			degreeSequence.add(getNeighbors(i).size() + getInNeighbors(i).size());
		}

		degreeSequence.sort((o1, o2) -> o2.compareTo(o1));

		return degreeSequence;
	}

	public abstract List<Integer> getDistance2(int v);

	@Override
	public String toString() {
		String s = "\nGraph with " + numVertices + " vertices and " + numEdges + " edges.\n";
		s += "Degree sequence: " + degreeSequence() + ".\n";
		if (numVertices <= 20)
			s += adjacencyString();
		return s;
	}

	public abstract String adjacencyString();

}
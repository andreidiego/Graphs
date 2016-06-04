package improvedgraph;

import java.util.HashSet;
import java.util.Set;

public class GraphNode {

	private double xCoordinate;
	private double yCoordinate;
	private Set<GraphEdge> edges = new HashSet<>();

	public GraphNode(double xCoordinate, double yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	public GraphNode(GraphNode toBeCopied) {
		this.xCoordinate = toBeCopied.xCoordinate;
		this.yCoordinate = toBeCopied.yCoordinate;
		this.edges = new HashSet<>(toBeCopied.edges);
	}

	public double xCoordinate() {
		return xCoordinate;
	}

	public double yCoordinate() {
		return yCoordinate;
	}

	public Set<GraphEdge> edges() {
		return edges;
	}

	// TODO Add test for the defensive return copy
	public GraphEdge addEdgeTo(GraphNode destination) {

		if (destination == null) {
			throw new IllegalArgumentException("You are trying to add an edge to a null node. That is not allowed as null nodes simply doesn't exist.");
		}

		GraphEdge edge = new GraphEdge(this, destination);
		
		if (!edges.add(edge)) {
			throw new IllegalArgumentException(
					"You are trying to add an edge to a node that is already connected with this node. Multiple edges are no allowed.");
		}

//		return new GraphEdge(edge);
		return edge;
	}

	public boolean hasEdgeTo(GraphNode destination) {
		return edges.stream().anyMatch(edge -> edge.destination().equals(destination));
	}

	public GraphEdge getEdgeTo(GraphNode destination) {
		return edges.stream().filter(edge -> edge.destination().equals(destination)).findAny().get();
	}

	public GraphEdge removeEdgeTo(GraphNode destination) {

		if (destination == null) {
			throw new IllegalArgumentException("You are trying to remove an edge to a null node. That is not allowed as null nodes simply doesn't exist.");
		}

		GraphEdge edge = new GraphEdge(this, destination);
		
		if (!edges.remove(edge)) {
			throw new IllegalArgumentException("You are trying to remove an inexistent edge. That is impossible!");
		}

		return edge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(xCoordinate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yCoordinate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphNode other = (GraphNode) obj;
		if (Double.doubleToLongBits(xCoordinate) != Double.doubleToLongBits(other.xCoordinate))
			return false;
		if (Double.doubleToLongBits(yCoordinate) != Double.doubleToLongBits(other.yCoordinate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GraphNode [xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + ", edges=" + edges + "]";
	}
}
package improvedgraph;

public class GraphEdge {

	private GraphNode origin;
	private GraphNode destination;
	private String label;
	private EdgeType type;
	private Direction direction;
	private double weight;

	public GraphEdge(GraphNode origin, GraphNode destination) {

		if (origin == null || destination == null) {
			throw new IllegalArgumentException(
					"You are trying to create an edge with a null " + (origin == null ? "origin" : "destination")
							+ ". An edge, by definition, must have both an origin and a destination.");
		}

		this.origin = origin;
		this.destination = destination;
	}

	public GraphEdge(GraphEdge toBeCopied) {
		this.origin = new GraphNode(toBeCopied.origin);
		this.destination =  new GraphNode(toBeCopied.destination);
	}

	public GraphNode origin() {
		return origin;
	}

	public GraphNode destination() {
		return destination;
	}

	public void label(String label) {
		this.label = label;
	}

	public String label() {
		return label;
	}

	public EdgeType type() {
		return type;
	}

	public void type(EdgeType type) {
		this.type = type;
	}

	public void direction(Direction direction) {
		this.direction = direction;
	}

	public Direction direction() {
		return direction;
	}

	public void weight(double weight) {
		this.weight = weight;
	}

	public double weight() {
		return weight;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphEdge other = (GraphEdge) obj;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Origin: " + origin.xCoordinate() + ", " + origin.yCoordinate() + " - Destination: "
				+ destination.xCoordinate() + ", " + destination.yCoordinate();
	}

	public GraphNode traverseFrom(GraphNode node) {
		GraphNode otherSide;
		
		if(node == null) {
			throw new IllegalArgumentException("You are trying to traverse an edge from a null node. Null nodes do not exist!.");
		
		} else if(node.equals(origin)) {
			otherSide = destination;
			
		} else if(node.equals(destination)) {
			otherSide = origin;
			
		} else {
			throw new IllegalArgumentException("You are trying to traverse an edge from a node that is different from both the origin and destination nodes of the edge.");
		}
		
		return otherSide;
	}

}

enum EdgeType {
	AVE, LANE, STREET
	
}

enum Direction {
	SOUTH

}
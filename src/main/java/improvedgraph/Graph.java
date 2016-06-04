package improvedgraph;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import improvedgraph.util.DequeStack;
import improvedgraph.util.Stack;

public class Graph {

	private Set<GraphNode> nodes;

	public Graph(Set<GraphNode> nodeSet) {

		if (nodeSet == null || nodeSet.isEmpty())
			throw new IllegalArgumentException(
					"You are trying to create a graph with " + (nodeSet == null ? "a null" : "an empty")
							+ " set of nodes. There is no graph without nodes and so, at least one node is required.");

		this.nodes = nodeSet;
	}

	public Set<GraphNode> nodes() {
		return new HashSet<>(nodes);
	}

	public GraphNode addNode(GraphNode node) {

		if (node == null)
			throw new IllegalArgumentException(
					"You are trying to add a null node to the graph and that is nor allowed. Please, check it out.");

		if (!nodes.add(new GraphNode(node)))
			throw new IllegalArgumentException(
					"You are trying to add a repeated node to the graph. That is not allowed.");

		return new GraphNode(node);
	}

	public GraphNode createNodeFromCoordinates(double xCoordinate, double yCoordinate) {
		nodes.add(new GraphNode(xCoordinate, yCoordinate));

		return new GraphNode(xCoordinate, yCoordinate);
	}

	public GraphEdge createAnEdgeBetweenTheFollowingNodes(GraphNode origin, GraphNode destination) {

		if (origin == null || destination == null)
			throw new IllegalArgumentException("You are trying to create an edge "
					+ (origin == null ? "from a null origin" : "to a null destination")
					+ " node. Please, check it out.");

		if(!nodes.contains(origin))
			throw new IllegalArgumentException(
					"You are trying to create an edge from an origin node that is not in the graph. Please, check it out.");

		if(!nodes.contains(destination))
			throw new IllegalArgumentException(
					"You are trying to create an edge to a destination node that is not in the graph. Please, check it out.");

		GraphNode originInGraph = nodes.stream().filter(node -> node.equals(origin)).findFirst().get();

		if (originInGraph.hasEdgeTo(destination)) {
			throw new IllegalArgumentException(
					"You are trying to create an edge that already exists. Multiple edges from the same origin to the same destination are not allowed in this graph.");
		}

		return new GraphEdge(originInGraph.addEdgeTo(destination));
	}

	public GraphEdge removeTheEdgeBetweenTheFollowingNodes(GraphNode origin, GraphNode destination) {

		if (origin == null || destination == null)
			throw new IllegalArgumentException("You are trying to remove an edge "
					+ (origin == null ? "from a null origin" : "to a null destination")
					+ " node. Please, check it out.");

		if(!nodes.contains(origin))
			throw new IllegalArgumentException(
					"You are trying to remove an edge from an origin node that is not in the graph. Please, check it out.");

		if(!nodes.contains(destination))
			throw new IllegalArgumentException(
					"You are trying to remove an edge to a destination node that is not in the graph. Please, check it out.");

		return new GraphEdge(nodes.stream().filter(node -> node.equals(origin)).findFirst().get().removeEdgeTo(destination));
	}

	public Stack<GraphEdge> navigate(GraphNode origin, GraphNode destination) {
		
		if(origin == null || destination == null)
			throw new IllegalArgumentException("You are trying to navigate " + (origin == null ? "from a null origin" : "to a null destination") + ". Please, check it out.");
		
		if(!nodes.contains(origin))
			throw new IllegalArgumentException("You are trying to navigate from an origin that is not in the graph. Please, check it out.");
		
		if(!nodes.contains(destination))
			throw new IllegalArgumentException("You are trying to navigate to a destination that is not in the graph. Please, check it out.");
		
		Queue<GraphNode> scheduler = new ArrayDeque<>();
		Set<GraphNode> visits = new HashSet<>();
		Map<GraphNode, GraphNode> tracker = new HashMap<>();
		Stack<GraphEdge> shortestPath = new DequeStack<>();
		GraphNode currentNode;
		
		scheduler.add(origin);
		
		while (!scheduler.isEmpty()) {
			currentNode = scheduler.remove();
//			explorer.nodeVisited(currentNode); // NOSONAR
			
			for (GraphEdge edge : currentNode.edges()) {				
				GraphNode outNeighbor = edge.traverseFrom(currentNode);
				
				if(!visits.contains(outNeighbor) && !scheduler.contains(outNeighbor)) {
//					explorer.nodeVisited(currentNode); // NOSONAR
					tracker.put(outNeighbor, currentNode);
					
					if(outNeighbor.equals(destination)) {
//						explorer.endOfJourney(); // NOSONAR
						GraphNode previousNodeInJourney = tracker.get(outNeighbor);
						shortestPath.push(edge);
						
						while(previousNodeInJourney != origin) {
							shortestPath.push(tracker.get(previousNodeInJourney).getEdgeTo(previousNodeInJourney));
							previousNodeInJourney = tracker.get(previousNodeInJourney);
						}
						
						return shortestPath;
						
					} else {
						scheduler.add(outNeighbor);
					}
					
				}
			}
			
			visits.add(currentNode);
		}
		
		return shortestPath;
	}
}
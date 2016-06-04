package improvedgraph;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import improvedgraph.util.DequeStack;
import improvedgraph.util.Stack;

public class GraphTest {

	private Graph graph;
	GraphNode origin;
	GraphNode destination;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		graph = new Graph(getFakeSetOfNodes());
		origin = new GraphNode(20.0, 14.0);
		destination = new GraphNode(20.0, 12.0);
	}

	private Set<GraphNode> getFakeSetOfNodes() {
		return new HashSet<GraphNode>(Arrays.asList(
				new GraphNode(20.0, 14.0), 
				new GraphNode(20.0, 12.0),
				new GraphNode(20.0, 16.0), 
				new GraphNode(16.0, 14.0)));
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void validNodeSetIsCorrectlySetByTheConstructor() {
		assertThat(graph.nodes(), is(getFakeSetOfNodes()));
	}

	@Test
	public void theStateOfTheGraphCannotBeHackedThroughTheNodeSetReturnedByTheNodesAccessor() {
		Set<GraphNode> nodes = graph.nodes();
		nodes.remove(new GraphNode(20.0, 14.0));

		int sizeOfTheHackedNodesSet = nodes.size();
		int sizeOfTheNodesSet = graph.nodes().size();

		assertThat(sizeOfTheNodesSet, greaterThan(sizeOfTheHackedNodesSet));
	}

	@Test
	public void nullNodeSetIsRefusedByTheConstructorAndAnExceptionIsThrown() {
		Graph graphWithNullNodesSet = null;

		try {
			graphWithNullNodesSet = new Graph(null);

			fail("Trying to create a graph with a null set of nodes should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to create a graph with a null set of nodes. There is no graph without nodes and so, at least one node is required."));
			assertThat(graphWithNullNodesSet, is(nullValue()));
		}
	}

	@Test
	public void emptyNodeSetIsRefusedByTheConstructorAndAnExceptionIsThrown() {
		Graph graphWithEmptyNodesSet = null;

		try {
			graphWithEmptyNodesSet = new Graph(new HashSet<>());

			fail("Trying to create a graph with an empty set of nodes should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to create a graph with an empty set of nodes. There is no graph without nodes and so, at least one node is required."));
			assertThat(graphWithEmptyNodesSet, is(nullValue()));
		}
	}

	@Test
	public void validNodesAreCorrectlyAddedToTheNodesSet() {
		GraphNode nodeAdded = new GraphNode(0.0, 1.0);

		assertThat(graph.addNode(nodeAdded), is(nodeAdded));
		assertThat(graph.nodes(), hasItem(nodeAdded));
		assertThat(graph.nodes().size(), is(5));
	}

	@Test
	public void repeatedNodesAreNotAddedAndAnExceptionIsThrown() {
		GraphNode repeatedNode = null;

		try {
			repeatedNode = graph.addNode(new GraphNode(20.0, 14.0));

			fail("Trying to add a repeated node to the graph should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to add a repeated node to the graph. That is not allowed."));
			assertThat(repeatedNode, is(nullValue()));
			assertThat(graph.nodes().size(), is(4));
		}
	}

	@Test
	public void nullNodeIsNotAddedAndAnExceptionIsThrown() {
		GraphNode nodeAdded = null;

		try {
			nodeAdded = graph.addNode(null);

			fail("Trying to add a null node to the graph should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to add a null node to the graph and that is nor allowed. Please, check it out."));
			assertThat(nodeAdded, is(nullValue()));
		}
	}

	@Test
	public void theReferenceReturnedByTheAddNodeMethodIsNotTheOriginal() {
		GraphNode nodeToAdd = new GraphNode(1.0, 1.0);
		GraphNode nodeAdded = graph.addNode(nodeToAdd);
		
		assertTrue(nodeAdded != nodeToAdd);
		
		nodeAdded.addEdgeTo(new GraphNode(2.0, 1.0));
		
		graph.nodes().forEach(node -> assertThat(node.edges().size(), equalTo(0)));
	}

	@Test
	public void createNodeFromCoordinatesReturnANodeWithTheGivenCoordinates() {
		assertThat(graph.createNodeFromCoordinates(0.0, 1.0), is(new GraphNode(0.0, 1.0)));
	}

	@Test
	public void nodesCreatedFromCoordinatesAreCorrectlyAddedToTheNodesSet() {
		graph.createNodeFromCoordinates(0.0, 1.0);
		assertThat(graph.nodes(), hasItem(new GraphNode(0.0, 1.0)));
	}

	@Test
	public void nodeReturnedByCreateNodeFromCoordinatesIsADefensiveCopy() {
		GraphNode newNode = graph.createNodeFromCoordinates(0.0, 1.0);
		newNode.addEdgeTo(new GraphNode(2.0, 1.0));
		
		graph.nodes().forEach(e -> assertThat(e.edges().size(), equalTo(0)));
	}

	@Test
	public void createEdgesBetweenNodesReturnAnEdgeBetweenTheGivenNodes() {
		assertThat(graph.createAnEdgeBetweenTheFollowingNodes(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0)), is(new GraphEdge(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0))));
	}

	@Test
	public void validEdgesAreCorrectlyAddedToTheNodesSet() {
		graph.createAnEdgeBetweenTheFollowingNodes(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0));

		assertThat(graph.nodes().stream().filter(node -> node.equals(new GraphNode(20.0, 14.0))).findFirst().get().edges(), hasItem(new GraphEdge(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0))));
	}

	@Test
	public void edgeReturnedByCreateEdgeBetweenNodesIsADefensiveCopy() {
		GraphEdge newEdge = graph.createAnEdgeBetweenTheFollowingNodes(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0));
		newEdge.label("Hacked label");
		
		assertThat(newEdge.origin().edges(), hasItem(new GraphEdge(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0))));
		
		newEdge.origin().removeEdgeTo(new GraphNode(20.0, 12.0));
		
		assertThat(newEdge.origin().edges(), not(hasItem(new GraphEdge(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0)))));
		
		GraphNode originOfTheEdgeAdded = graph.nodes()
								.stream()
								.filter(node -> node.equals(new GraphNode(20.0, 14.0)))
								.findFirst()
								.get();
		
		assertThat(originOfTheEdgeAdded.edges(), hasItem(new GraphEdge(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0))));
		
		GraphEdge addedEdge = originOfTheEdgeAdded
								.edges()
								.stream()
								.filter(edge -> edge.equals(new GraphEdge(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0))))
								.findFirst()
								.get();
		
		assertThat(addedEdge.label(), not("Hacked label"));
	}
	
	@Test
	public void edgesFromNullOriginsAreNotAddedAndAnExceptionIsThrown() {
		GraphEdge newEdge = null;

		try {
			newEdge = graph.createAnEdgeBetweenTheFollowingNodes(null, new GraphNode(20.0, 12.0));

			fail("Trying to create an edge from a null origin should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to create an edge from a null origin node. Please, check it out."));
			assertThat(newEdge, is(nullValue()));
		}
	}

	@Test
	public void edgesToNullDestinationsAreNotAddedAndAnExceptionIsThrown() {
		GraphEdge newEdge = null;

		try {
			newEdge = graph.createAnEdgeBetweenTheFollowingNodes(new GraphNode(20.0, 14.0), null);

			fail("Trying to create an edge to a null destination should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to create an edge to a null destination node. Please, check it out."));
			assertThat(newEdge, is(nullValue()));
		}
	}

	@Test
	public void edgesFromInexistentOriginsAreNotAddedAndAnExceptionIsThrown() {
		GraphEdge newEdge = null;

		try {
			newEdge = graph.createAnEdgeBetweenTheFollowingNodes(new GraphNode(0.0, 1.0), new GraphNode(20.0, 12.0));

			fail("Trying to create an edge from an inexistent origin should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to create an edge from an origin node that is not in the graph. Please, check it out."));
			assertThat(newEdge, is(nullValue()));
		}
	}
	
	@Test
	public void edgesToInexistentDestinationsAreNotAddedAndAnExceptionIsThrown() {
		GraphEdge newEdge = null;

		try {
			newEdge = graph.createAnEdgeBetweenTheFollowingNodes(new GraphNode(20.0, 14.0), new GraphNode(0.0, 1.0));

			fail("Trying to create an edge to an inexistent destination should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to create an edge to a destination node that is not in the graph. Please, check it out."));
			assertThat(newEdge, is(nullValue()));
		}
	}
	
	@Test
	public void repeatedEdgesAreNotAddedAndAnExceptionIsThrown() {
		GraphEdge repeatedEdge = null;

		graph.createAnEdgeBetweenTheFollowingNodes(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0));

		try {
			repeatedEdge = graph.createAnEdgeBetweenTheFollowingNodes(new GraphNode(20.0, 14.0), new GraphNode(20.0, 12.0));

			fail("Trying to create multiple edges from the same origin to the same destination should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to create an edge that already exists. Multiple edges from the same origin to the same destination are not allowed in this graph."));
			assertThat(repeatedEdge, is(nullValue()));
		}
	}

	private void createAFakeEdge() {
		assertThat(graph.createAnEdgeBetweenTheFollowingNodes(origin, destination).origin().edges(), hasItem(new GraphEdge(origin, destination)));
	}
	
	@Test
	public void removeEdgeBetweenNodesReturnAEdgeWithTheGivenNodes() {
		createAFakeEdge();
		assertThat(graph.removeTheEdgeBetweenTheFollowingNodes(origin, destination), is(new GraphEdge(origin, destination)));
	}
	
	@Test
	public void validEdgesAreCorrectlyRemoved() {
		createAFakeEdge();
		assertThat(graph.removeTheEdgeBetweenTheFollowingNodes(origin, destination).origin().edges(), not(hasItem(new GraphEdge(origin, destination))));
	}

	@Test
	public void anExceptionIsThrownWhenEdgesFromNullOriginsAreAskedToBeRemoved() {
		createAFakeEdge();
		
		GraphEdge removedEdge = null;

		try {
			removedEdge = graph.removeTheEdgeBetweenTheFollowingNodes(null, destination);

			fail("Trying to remove an edge from a null origin should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to remove an edge from a null origin node. Please, check it out."));
			assertThat(removedEdge, is(nullValue()));
		}
	}

	@Test
	public void anExceptionIsThrownWhenEdgesToNullDestinationsAreAskedToBeRemoved() {
		createAFakeEdge();
		
		GraphEdge removedEdge = null;

		try {
			removedEdge = graph.removeTheEdgeBetweenTheFollowingNodes(origin, null);

			fail("Trying to remove an edge to a null destination should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to remove an edge to a null destination node. Please, check it out."));
			assertThat(removedEdge, is(nullValue()));
		}
	}

	@Test
	public void anExceptionIsThrownWhenEdgesFromInexistentOriginsAreAskedToBeRemoved() {
		createAFakeEdge();
		
		GraphEdge removedEdge = null;

		try {
			removedEdge = graph.removeTheEdgeBetweenTheFollowingNodes(new GraphNode(0.0, 1.0), destination);

			fail("Trying to remove an edge from an inexistent origin should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to remove an edge from an origin node that is not in the graph. Please, check it out."));
			assertThat(removedEdge, is(nullValue()));
		}
	}

	@Test
	public void anExceptionIsThrownWhenEdgesToInexistentDestinationsAreAskedToBeRemoved() {
		createAFakeEdge();
		
		GraphEdge removedEdge = null;

		try {
			removedEdge = graph.removeTheEdgeBetweenTheFollowingNodes(origin, new GraphNode(0.0, 1.0));

			fail("Trying to remove an edge to an inexistent destination should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to remove an edge to a destination node that is not in the graph. Please, check it out."));
			assertThat(removedEdge, is(nullValue()));
		}
	}

	@Test
	public void edgeReturnedByRemoveEdgeBetweenNodesIsADefensiveCopy() {
		createAFakeEdge();
		
		GraphEdge removedEdge = graph.removeTheEdgeBetweenTheFollowingNodes(origin, destination);
		removedEdge.origin().addEdgeTo(destination);
		
		assertThat(graph.nodes().stream().filter(node -> node.equals(origin)).findFirst().get().hasEdgeTo(destination), is(false));
		
	}

	private Graph loadNodesAndEdges() {
		BufferedReader reader = null;
		Set<GraphNode> kitsilanoNodeSet = new HashSet<>();
		Set<GraphNode> originNodeSet = new HashSet<>();
		Set<GraphNode> destinationNodeSet = new HashSet<>();
		
        try {
            String nextLine;
            reader = new BufferedReader(new FileReader("test/maps/kitsilanotest.map"));

            while ((nextLine = reader.readLine()) != null) {
            	String[] edges = nextLine.split(",");
            	
            	double originXCoordinate = Double.parseDouble(edges[0]);
            	double originYCoordinate = Double.parseDouble(edges[1]);
            	GraphNode originNode = new GraphNode(originXCoordinate, originYCoordinate);
            	
            	double destinationXCoordinate = Double.parseDouble(edges[2]);
            	double destinationYCoordinate = Double.parseDouble(edges[3]);
            	GraphNode destinationNode = new GraphNode(destinationXCoordinate, destinationYCoordinate);

            	String edgeType = edges[4];
            	String edgeLabel = edges[5];
            	double edgeWeight = Double.parseDouble(edges[6]);
            	
            	GraphEdge edge = originNode.addEdgeTo(destinationNode);
            	edge.type(edgeType.equals("Ave") ? EdgeType.AVE : edgeType.equals("Lane") ? EdgeType.LANE : EdgeType.STREET);
            	edge.label(edgeLabel);
            	edge.weight(edgeWeight);
            	
            	originNodeSet.add(originNode);
            	destinationNodeSet.add(destinationNode);
            }
            
            reader.close();
            
        } catch (IOException e) {
            System.err.println("Problem loading graph file: maps/kitsilanotest.map");
            e.printStackTrace();
        }
        
        kitsilanoNodeSet.addAll(originNodeSet);
        kitsilanoNodeSet.addAll(destinationNodeSet);
        
        return new Graph(kitsilanoNodeSet);
	}

	@Test
	public void anExceptionIsThrownWhenTheNavigatorIsAskedToNavigateFromANullNode() {
		Stack<GraphEdge> shortestPath = null;

		try {
			shortestPath = loadNodesAndEdges().navigate(null, new GraphNode(2.0, 8.0));

			fail("Trying to navigate from a null origin should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to navigate from a null origin. Please, check it out."));
			assertThat(shortestPath, is(nullValue()));
		}
	}

	@Test
	public void anExceptionIsThrownWhenTheNavigatorIsAskedToNavigateToANullNode() {
		Stack<GraphEdge> shortestPath = null;

		try {
			shortestPath = loadNodesAndEdges().navigate(new GraphNode(0.0, 0.0), null);

			fail("Trying to navigate to a null destination should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to navigate to a null destination. Please, check it out."));
			assertThat(shortestPath, is(nullValue()));
		}
	}

	@Test
	public void anExceptionIsThrownWhenTheNavigatorIsAskedToNavigateFromAnInexistentNode() {
		Stack<GraphEdge> shortestPath = null;

		try {
			shortestPath = loadNodesAndEdges().navigate(new GraphNode(0.0, 0.1), new GraphNode(2.0, 8.0));

			fail("Trying to navigate from an inexistent origin should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to navigate from an origin that is not in the graph. Please, check it out."));
			assertThat(shortestPath, is(nullValue()));
		}
	}

	@Test
	public void anExceptionIsThrownWhenTheNavigatorIsAskedToNavigateToAnInexistentNode() {
		Stack<GraphEdge> shortestPath = null;

		try {
			shortestPath = loadNodesAndEdges().navigate(new GraphNode(0.0, 0.0), new GraphNode(0.0, 0.1));

			fail("Trying to navigate to an inexistent destination should throw an IllegalArgumentException.");

		} catch (Exception e) {
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is(
					"You are trying to navigate to a destination that is not in the graph. Please, check it out."));
			assertThat(shortestPath, is(nullValue()));
		}
	}

	@Test
	public void theSetOfNodesComprisingTheShortestPathIsReturnedByTheNavigator() {
		Stack<GraphEdge> shortestPah = new DequeStack<>();
		shortestPah.push(new GraphEdge(new GraphNode(15.0, 20.0), new GraphNode(14.0, 20.0)));
		shortestPah.push(new GraphEdge(new GraphNode(14.0, 20.0), new GraphNode(14.0, 16.0)));
		shortestPah.push(new GraphEdge(new GraphNode(14.0, 16.0), new GraphNode(12.0, 16.0)));
		shortestPah.push(new GraphEdge(new GraphNode(12.0, 16.0), new GraphNode(11.0, 16.0)));
		
		assertThat(loadNodesAndEdges().navigate(new GraphNode(15.0, 20.0), new GraphNode(11.0, 16.0)), is(shortestPah));
	}

	@Test
	public void theSetOfNodesReturnedByTheNavigatorIsADefensiveCopy() {
//		GraphNode newNode = graph.navigate(origin, null).stream().filter(node -> node.equals(origin)).findFirst().get();
//		newNode.addEdgeTo(new GraphNode(2.0, 1.0));
//		
//		graph.nodes().forEach(node -> assertThat(node.edges().size(), equalTo(0)));
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void theCorretNumberOfStepsIsReportedByTheNavigator() {
//		assertThat(graph.navigate(origin, null).size(), is(0));
		fail("Not yet implemented"); // TODO
	}

}

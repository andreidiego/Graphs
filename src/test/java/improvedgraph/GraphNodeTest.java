package improvedgraph;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphNodeTest {

	private GraphNode basicNode;
	private GraphNode nodeWithEdges;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		double xCoordinate = 20.0;
		double yCoordinate = 15.0;

		basicNode = new GraphNode(xCoordinate, yCoordinate);
		nodeWithEdges = new GraphNode(xCoordinate, yCoordinate);
		getFakeEdgesFrom(nodeWithEdges).forEach(e -> nodeWithEdges.addEdgeTo(e.destination()));
		
	}

	private Set<GraphEdge> getFakeEdgesFrom(GraphNode origin) {
		GraphNode destinationNode1 = new GraphNode(16.0, 15.0);
		GraphNode destinationNode2 = new GraphNode(20.0, 16.0);
		GraphNode destinationNode3 = new GraphNode(20.0, 14.0);
		
		Set<GraphEdge> nodeEdges = new HashSet<GraphEdge>(Arrays.asList( 
				new GraphEdge(origin, destinationNode1),
				new GraphEdge(origin, destinationNode2), 
				new GraphEdge(origin, destinationNode3) 
		));
		
		return nodeEdges;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void twoNodesWithTheSameCoordinatesAreEquals() {
		assertEquals(basicNode, new GraphNode(20.0, 15.0));
	}
	
	@Test
	public void coordinatesAreCorrectlySetByTheConstructor() {
		assertThat(basicNode.xCoordinate(), is(20.0));
		assertThat(basicNode.yCoordinate(), is(15.0));
	}

	@Test
	public void copyConstructor() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void defensiveCopyFromMethods() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void hasEdgeTo() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void getEdgeToReturnsTheCorrectNode() {
		assertThat(nodeWithEdges.getEdgeTo(new GraphNode(16.0, 15.0)), is(new GraphEdge(nodeWithEdges, new GraphNode(16.0, 15.0))));
	}
	
	@Test
	public void getEdgeToThrowsAnExceptionWhenThereIsNotSuchAnEdge() {
		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public void edgesAreCorrectlySetByTheConstructor() {
		assertThat(nodeWithEdges.xCoordinate(), is(20.0));
		assertThat(nodeWithEdges.yCoordinate(), is(15.0));
		assertEquals(nodeWithEdges.edges(), getFakeEdgesFrom(nodeWithEdges));
	}

	@Test
	public void edgesAreCorrectlyAdded() {
		GraphNode destinationNode1 = new GraphNode(16.0, 15.0);		
		GraphEdge graphEdge = new GraphEdge(basicNode, destinationNode1);
		
		assertThat(basicNode.addEdgeTo(destinationNode1), is(graphEdge));
		assertThat(basicNode.edges(), hasItem(graphEdge));
	}

	@Test
	public void multipleEdgesAreNotAllowedAndAnExceptionIsThrown() {
		GraphNode destinationNode1 = new GraphNode(16.0, 15.0);		
		
		basicNode.addEdgeTo(destinationNode1);
		
		int howManyEdgesBeforeTheFailedTrial = basicNode.edges().size();		
		GraphEdge repeatedEdgeAdded = null;
		
		try {
			repeatedEdgeAdded = basicNode.addEdgeTo(destinationNode1);
			
			fail("Trying to add an edge to a node that is already connected with this one should throw an IllegalArgumentException.");
			
		} catch (Exception e) {
			assertNull(repeatedEdgeAdded);
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertEquals("You are trying to add an edge to a node that is already connected with this node. Multiple edges are no allowed.", e.getMessage());
			assertThat(basicNode.edges().size(), is(howManyEdgesBeforeTheFailedTrial));
		}
	}
	
	@Test
	public void edgesToNullDestinationsAreNotAddedAndAnExceptionIsThrown() {
		int howManyEdgesBeforeTheFailedTrial = basicNode.edges().size();		
		GraphEdge nullEdgeAdded = null;
		
		try {
			nullEdgeAdded = basicNode.addEdgeTo(null);
			
			fail("Trying to add an edge to a null node should throw an IllegalArgumentException as null nodes doesn't exist.");
			
		} catch (Exception e) {
			assertNull(nullEdgeAdded);
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertEquals("You are trying to add an edge to a null node. That is not allowed as null nodes simply doesn't exist.", e.getMessage());
			assertThat(basicNode.edges().size(), is(howManyEdgesBeforeTheFailedTrial));
		}
	}
	
	@Test
	public void edgesAreCorrectlyRemoved() {
		GraphEdge graphEdge = new GraphEdge(nodeWithEdges, new GraphNode(16.0, 15.0));

		assertThat(nodeWithEdges.removeEdgeTo(new GraphNode(16.0, 15.0)), is(graphEdge));		
		assertThat(nodeWithEdges.edges(), not(hasItem(graphEdge)));
	}

	@Test
	public void anExceptionIsThrownWhenAswedToRemoveAnEdgeToANullDestination() {
		GraphEdge removeNullEdge = null;
		
		try {
			removeNullEdge = nodeWithEdges.removeEdgeTo(null);
			
			fail("Trying to remove an edge to a null node should throw an IllegalArgumentException as null nodes simply doesn't exist.");
			
		} catch (Exception e) {
			assertNull(removeNullEdge);
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertEquals("You are trying to remove an edge to a null node. That is not allowed as null nodes simply doesn't exist.", e.getMessage());
			assertThat(nodeWithEdges.edges(), is(getFakeEdgesFrom(nodeWithEdges)));
		}
	}

	@Test
	public void anExceptionIsThrownWhenAswedToRemoveAnInexistentEdge() {
		int howManyEdgesBeforeTheFailedTrial = basicNode.edges().size();		
		GraphEdge inexistentEdge = null;
		
		try {
			inexistentEdge = nodeWithEdges.removeEdgeTo(nodeWithEdges);
			
			fail("Trying to remove an inexistent edge from a node should throw an IllegalArgumentException.");
			
		} catch (Exception e) {
			assertNull(inexistentEdge);
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertEquals("You are trying to remove an inexistent edge. That is impossible!", e.getMessage());
			assertThat(basicNode.edges().size(), is(howManyEdgesBeforeTheFailedTrial));
			assertThat(nodeWithEdges.edges(), is(getFakeEdgesFrom(nodeWithEdges)));
		}
	}

	@Test
	public void testToString() {
		assertThat(nodeWithEdges.toString(), is("GraphNode [xCoordinate=" + nodeWithEdges.xCoordinate() + ", yCoordinate=" + nodeWithEdges.yCoordinate() + ", edges=" + nodeWithEdges.edges() + "]"));
	}
}
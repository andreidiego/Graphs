package improvedgraph;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GraphEdgeTest {

	private GraphNode origin;
	private GraphNode destination;
	private GraphEdge basicEdge;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		origin = new GraphNode(20.0, 15.0);
		destination = new GraphNode(16.0, 15.0);
		
		basicEdge = new GraphEdge(origin, destination);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void copyConstructor() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void validOriginAndDestinationAreCorrectlySetByTheConstructor() {
		assertThat(basicEdge.origin(), is(new GraphNode(20.0, 15.0)));
		assertThat(basicEdge.destination(), is(new GraphNode(16.0, 15.0)));
	}
	
	@Test
	public void nullOriginsAreRefusedByTheConstructorAndAnExceptionIsThrown() {
		GraphEdge graphEdge = null;
		
		try {
			graphEdge = new GraphEdge(null, new GraphNode(16.0, 15.0));
			
			fail("Trying to create an edge with a null origin must be refused with an IllegalArgumentException.");
			
		} catch (Exception e) {
			assertThat(graphEdge, is(nullValue()));
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is("You are trying to create an edge with a null origin. An edge, by definition, must have both an origin and a destination."));
		}
	}
	
	@Test
	public void nullDestinationsAreRefusedByTheConstructorAndAnExceptionIsThrown() {
		GraphEdge graphEdge = null;
		
		try {
			graphEdge = new GraphEdge(new GraphNode(16.0, 15.0), null);
			
			fail("Trying to create an edge with a null destination must be refused with an IllegalArgumentException.");
			
		} catch (Exception e) {
			assertThat(graphEdge, is(nullValue()));
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is("You are trying to create an edge with a null destination. An edge, by definition, must have both an origin and a destination."));
		}
	}
	
	@Test
	public void twoEdgesWithTheSameCoordinatesAreEquals() {
		assertEquals(basicEdge, new GraphEdge(new GraphNode(20.0, 15.0), new GraphNode(16.0, 15.0)));
	}
	
	@Test
	public void labelsAreCorrectlySetByTheMutator() {
		basicEdge.label("Vine St.");
		assertThat(basicEdge.label(), is("Vine St."));
	}
	
	@Test
	public void typesAreCorrectlySetByTheMutator() {
		basicEdge.type(EdgeType.STREET);
		assertThat(basicEdge.type(), is(EdgeType.STREET));;
	}
	
	@Test
	public void directionsAreCorrectlySetByTheMutator() {
		basicEdge.direction(Direction.SOUTH);
		assertThat(basicEdge.direction(), is(Direction.SOUTH));
	}
	
	@Test
	public void weightsAreCorrectlySetByTheMutator() {
		basicEdge.weight(0.5);
		assertThat(basicEdge.weight(), is(0.5));
	}
	
	@Test
	public void givenAValidNodeToTraverseFromTheCorrectEndPointIsReturned() {
		assertThat(basicEdge.traverseFrom(origin), is(destination));
		assertThat(basicEdge.traverseFrom(destination), is(origin));
	}

	@Test
	public void anExceptionIsThrownWhenGivenANullNodeToTraverseFrom() {
		GraphNode otherSide = null;
		
		try {
			otherSide = basicEdge.traverseFrom(null);
			
			fail("Trying to traverse an edge from a null node must be refused with an IllegalArgumentException.");
			
		} catch (Exception e) {
			assertThat(otherSide, is(nullValue()));
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is("You are trying to traverse an edge from a null node. Null nodes do not exist!."));
		}
	}

	@Test
	public void anExceptionIsThrownWhenGivenAnInvalidNodeToTraverseFrom() {
		GraphNode otherSide = null;
		
		try {
			otherSide = basicEdge.traverseFrom(new GraphNode(0.5, 1.5));
			
			fail("Trying to traverse an edge from an invalid node must be refused with an IllegalArgumentException.");
			
		} catch (Exception e) {
			assertThat(otherSide, is(nullValue()));
			assertThat(e, instanceOf(IllegalArgumentException.class));
			assertThat(e.getMessage(), is("You are trying to traverse an edge from a node that is different from both the origin and destination nodes of the edge."));
		}
	}
}
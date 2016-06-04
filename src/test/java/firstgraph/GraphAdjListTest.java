package firstgraph;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import firstgraph.GraphAdjList;

public class GraphAdjListTest {

	private GraphAdjList adjListGraph;
	private List<Integer> actualPath;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		adjListGraph = new GraphAdjList();
		adjListGraph.addVertex();
		adjListGraph.addVertex();
		adjListGraph.addVertex();
		adjListGraph.addVertex();
		adjListGraph.addVertex();
		adjListGraph.addVertex();
		adjListGraph.addVertex();
		adjListGraph.addVertex();
		
		actualPath = new ArrayList<Integer>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void dfsPathAlwaysChooseFirstNeighbor() {
		adjListGraph.addEdge(0, 1);
		adjListGraph.addEdge(1, 0);
		adjListGraph.addEdge(1, 2);
		adjListGraph.addEdge(2, 1);
		adjListGraph.addEdge(1, 3);
		adjListGraph.addEdge(3, 1);
		adjListGraph.addEdge(3, 4);
		adjListGraph.addEdge(4, 3);
		adjListGraph.addEdge(4, 0);
		adjListGraph.addEdge(0, 4);
		adjListGraph.addEdge(0, 5);
		adjListGraph.addEdge(5, 0);
		adjListGraph.addEdge(5, 6);
		adjListGraph.addEdge(6, 5);
		adjListGraph.addEdge(5, 7);
		adjListGraph.addEdge(7, 5);

		ArrayList<Integer> expected = new ArrayList<Integer>();
		expected.add(0);
		expected.add(1);
		expected.add(2);
		expected.add(1);
		expected.add(3);
		expected.add(4);
		expected.add(3);
		expected.add(1);
		expected.add(0);
		expected.add(5);
		expected.add(6);
		expected.add(5);
		expected.add(7);
		
		adjListGraph.searchDeep(0, 6);
		
//		Assert.assertEquals(expected, actualPath);
	}

}

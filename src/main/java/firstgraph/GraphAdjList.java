package firstgraph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GraphAdjList extends Graph {

	private Map<Integer, ArrayList<Integer>> adjListsMap;

	public GraphAdjList() {
		adjListsMap = new HashMap<>();
	}

	@Override
	public void implementAddVertex() {
		int v = getNumVertices();
		ArrayList<Integer> neighbors = new ArrayList<>();
		adjListsMap.put(v, neighbors);
	}

	@Override
	public void implementAddEdge(int v, int w) {
		(adjListsMap.get(v)).add(w);
	}

	@Override
	public List<Integer> getNeighbors(int v) {
		return new ArrayList<>(adjListsMap.get(v));
	}

	@Override
	public List<Integer> getInNeighbors(int v) {
		List<Integer> inNeighbors = new ArrayList<>();

		for (int u : adjListsMap.keySet()) {
			// iterate through all edges in u's adjacency list and
			// add u to the inNeighbor list of v whenever an edge
			// with startpoint u has endpoint v.
			for (int w : adjListsMap.get(u)) {
				if (v == w) {
					inNeighbors.add(u);
				}
			}
		}

		return inNeighbors;
	}

	@Override
	public List<Integer> getDistance2(int v) {
		List<Integer> twoHopsNeighbors = new ArrayList<>();

		for (Integer vNeighbor : getNeighbors(v)) {
			twoHopsNeighbors.addAll(getNeighbors(vNeighbor));
		}

		return twoHopsNeighbors;
	}

	@Override
	public String adjacencyString() {
		String s = "Adjacency list";
		s += " (size " + getNumVertices() + "+" + getNumEdges() + " integers):";

		for (int v : adjListsMap.keySet()) {
			s += "\n\t" + v + ": ";
			for (int w : adjListsMap.get(v)) {
				s += w + ", ";
			}
		}

		return s;
	}

	public boolean searchDeep(int start, int goal, List<Integer> path) {
		System.out.println("start: " + start + " goal: " + goal);
		System.out.println(Thread.currentThread().getStackTrace());
		
		path.add(start);
		
		if (start == goal) {
			return true;
		}
		
		for (Integer outNeighbor : getNeighbors(start)) {
			
			if (!path.contains(outNeighbor)) {
				boolean reachedDeadEnd = !searchDeep(outNeighbor, goal, path);
				
//				If a search returns false it means a dead end was reached. Otherwise, the goal has been reached
				if (reachedDeadEnd) {
					path.add(start);
					
				} else {
					return true;
				}
			}
		}
		
		return false;
	}

	public Map<Integer,Integer> searchDeep(int start, int goal) {
		Deque<Integer> whereToNext = new ArrayDeque<>();
		HashSet<Integer> visited = new HashSet<>();
		HashMap<Integer, Integer> parent = new HashMap<>();
		
		whereToNext.push(start);
		visited.add(start);
		
		while (!whereToNext.isEmpty()) {
			Integer currentNode = whereToNext.pop();
			if (currentNode == goal) {
				return parent;
			}
			
			for (Integer outNeighbor : getNeighbors(currentNode)) {
				if (!visited.contains(outNeighbor)) {
					visited.add(outNeighbor);
					parent.put(currentNode, outNeighbor);
					whereToNext.push(outNeighbor);
				}
			}
		}
		
		return parent;
	}
}
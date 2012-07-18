package pobo.cpnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import pobo.common.CPNETAdjList;
import pobo.common.CPNETAdjMatrix;
import pobo.common.ConjuctionTable;
import pobo.common.Util;

/**
 * PoBo CPNET
 */
public class CPNET {
	
	/**net name, if needed*/
	private String name;
	/** the CPNET data */
	private CPNETAdjMatrix cpnet_data;
	/** Vertex counter */
	private int vertexCounter;
	/** Conjunction Table collection */
	private ConjuctionTable[] vertex;
	/** List of vertex name */
	private ArrayList<String> vertexName;

	//private ArrayList<String> queueDFS;
	private HashMap<String, Util.Color> colors;
	private CPNETAdjList partialOrderSolution;

	/**
	 * Default constructor: creates an empty CPNET
	 */
	public CPNET(){ }

	/**
	 * One argument constructor: creates an empty CPNET with 'size' node
	 * @param size matrix size (Squared Matrix)
	 */
	public CPNET(int size){
		this.cpnet_data = new CPNETAdjMatrix(size);
		this.vertexCounter = 0;
		this.vertex = new ConjuctionTable[size];
		this.vertexName = new ArrayList<String>();
	}

	/**
	 * Adds a vertex to the CPNET
	 * @param v Vertex
	 * @return True if the insert works
	 */
	public boolean addVertex(String v) {
		if (vertexCounter < cpnet_data.getMatrix().length) {
			//vertex[vertexCounter] = new ConjuctionTable(v);
			vertexName.add(v);
			vertexCounter++;
			return true;
		} else 
			return false;
	}

	/**
	 * Add an edge to the CPNET
	 * @param v1 Start vertex
	 * @param v2 End vertex
	 * @return True if the insert works
	 */
	public boolean addEdge(String v1, String v2) {
		if (vertexName.contains(v1) && vertexName.contains(v2)) {
			cpnet_data.putCell(1, vertexName.indexOf(v1), vertexName.indexOf(v2));
			return true;
		} else
			return false;
	}

	/**
	 * Initializes the CPNET structure
	 */
	public void generate() {
		int count;
		ArrayList<String> parents;
		for (int col = 0; col < cpnet_data.getMatrix().length; col++) {
			count = 0;
			parents = new ArrayList<String>();
			for (int row = 0; row < cpnet_data.getMatrix().length; row++) 
				if (cpnet_data.getCell(row, col) == 1) {
					count++;
					parents.add(vertexName.get(row));
				}
			if (parents.size() == 0)
				parents.add("#");
			vertex[col] = new ConjuctionTable(vertexName.get(col), parents, Util.createBinaryEnumHashMap(count));
		}

	}

	/**
	 * Sets a specific preference
	 * @param v
	 * @param parentConf
	 * @param value
	 */
	public void setPreference(String v, String parentConf, Integer value) {
		vertex[vertexName.indexOf(v)].setPreference(parentConf, value);
	}

	/**
	 * Gets all conjunction table
	 * @return All conjunction table
	 */
	public ConjuctionTable[] getVertex() {
		return vertex;
	}

	/**
	 * Gets the CPNET graph
	 * @return The CPNET graph
	 */
	public CPNETAdjMatrix getCpnet_data() {
		return cpnet_data;
	}

	/**
	 * Gets the optimal solution of the CPNET
	 * @return The best solution
	 */
	public Solution getOptimalSolution() {
		// Vertex queue
		LinkedList<Integer> queue = cpnet_data.getIndependentVars();
		int nodeId, size = vertexName.size();

		int[] parentCounter = new int[size];
		// Initialize the parentCounter
		for (int i = 0; i < size; i++)
			parentCounter[i] = 0;

		Solution best = new Solution(size);
		while (queue.size() > 0) {

			// Take the first element of the queue
			nodeId = queue.pollFirst();

			// Update the solution

			// If is an independent vertex 
			if (vertex[nodeId].getParents().get(0) == "#") 
				best.setConfiguration(nodeId, vertex[nodeId].getPreference("0").toString().charAt(0));
			// Otherwise
			else {
				String parentConf = "";
				// Get the preference from conjunction table
				for (int i = 0; i < vertex[nodeId].getNumParents(); i++)
					parentConf += best.getSolutionAsChars()[vertexName.indexOf(vertex[nodeId].getParents().get(i))];
				best.setConfiguration(nodeId, vertex[nodeId].getPreference(parentConf).toString().charAt(0));
			}

			// Update the parent count
			for (int col = 0; col < size; col++)
				if (cpnet_data.getCell(nodeId, col) == 1)
					parentCounter[col]++;

			// Check if some vertex are ready to be processed
			// and put them in the queue
			for (int i = 0; i < size; i++)
				if (parentCounter[i] == vertex[i].getNumParents()) {
					queue.addLast(i);
					// "Sentinel" value
					parentCounter[i] = -1;
				}
		}
		return best;
	}

	/**
	 * Gets the partial order of the solutions
	 * @return The adjacent list that represent the partial order graph
	 */
	public CPNETAdjList getPartialOrder() {
		long start = System.currentTimeMillis();
		int size = vertexName.size();
		// Initialize the vertex color
		colors = Util.createBinaryColorHashMap(size);
		partialOrderSolution = new CPNETAdjList(size);
		// Start the DFS Visit from the best solution
		Solution best = getOptimalSolution();
		DFSVisit(best.getSolutionAsString());

		long end = System.currentTimeMillis();
		System.out.println("Tempo di calcolo: " + (end-start));
		return partialOrderSolution;
	}
	
	/**
	 * Method to check if is necessary to generate the partial order
	 */
	public boolean checkPartialOrderReady(){
		if(partialOrderSolution==null)return false;
		else return true;
	}

	/**
	 * Visits one node in the graph
	 * @param node The node to visit
	 */
	private void DFSVisit(String node) {
		// If the node isn't white (just visited)
		if (colors.get(node) != Util.Color.WHITE)
			return;
		// Otherwise put it grey
		ArrayList<String> neighbour = Util.getPreferenceNeighbours(node);
		colors.put(node, Util.Color.GREY);
		// Compare it to each neighbours and propagate the DFS visit
		for (int i = 0; i < neighbour.size(); i++)
			if (colors.get(neighbour.get(i)) != Util.Color.BLACK) {
				// If they are not already been compared
				if (!partialOrderSolution.getAdjacents(node).contains(neighbour.get(i)) &&
					!partialOrderSolution.getAdjacents(neighbour.get(i)).contains(node))	
					if (isBetterThan(node, neighbour.get(i)))
						partialOrderSolution.addAdj(node, neighbour.get(i));
					else
						partialOrderSolution.addAdj(neighbour.get(i), node);
				// Recursive call
				DFSVisit(neighbour.get(i));
			}
		// Put it black (the visit is finished)
		colors.put(node, Util.Color.BLACK);
	}

	/**
	 * Check if Solution 1 is better than Solution 2
	 * @param s1 Solution 1
	 * @param s2 Solution 2
	 * @return
	 */
	private boolean isBetterThan(String s1, String s2) {
		int i;
		boolean better;
		// Find the index of the missmatch character
		for (i = 0; i < s1.length(); i++)
			if (s1.charAt(i) != s2.charAt(i))
				break;
		ArrayList<String> parents = vertex[i].getParents();
		// If the node has no parent
		if (parents.get(0) == "#") {
			better = s1.charAt(i) == Character.forDigit(vertex[i].getPreference("0"), 10);
		}
		// Otherwise
		else {
			ArrayList<Integer> parentIndex = new ArrayList<Integer>();
			// Get the parents index
			for (int j = 0; j < parents.size(); j++)
				parentIndex.add(vertexName.indexOf(parents.get(j)));
			String conf = "";
			// Create the configuration
			for (int j = 0; j < parentIndex.size(); j++)
				conf += s1.charAt(parentIndex.get(j));
			// Get the preference
			int pref = vertex[i].getPreference(conf);
			better = s1.charAt(i) == Character.forDigit(pref, 10);
		}
//		if (better)
//			System.out.println(s1+" meglio di "+s2);
//		else
//			System.out.println(s2+" meglio di "+s1);
		return better;
	}
	
	/**
	 * Gets the relation between the two input strings
	 * @param s1 Solution 1
	 * @param s2 Solution 2
	 * @return The relation between the strings
	 */
	public Util.Relation compareSolutions(String s1, String s2) {
		// If they're the same solution
		if (s1.equals(s2))
			return Util.Relation.EQUAL;
		// Otherwise
		else {
			// If exists a path from s1 to s2
			if (existsPath(s1, s2))
				return Util.Relation.BETTER;
			// If exists a path from s2 to s1
			else if (existsPath(s2, s1))
				return Util.Relation.WORSE;
			// Otherwise they're not comparable
			else
				return Util.Relation.INCOMPARABLE;
		}
	}
	
	/**
	 * Checks if in the partial order graph exists a path from source to dest 
	 * @param source Source node
	 * @param dest Destination node
	 * @return True if exists a path
	 */
	private boolean existsPath(String source, String dest) {
		// If we are arrived to destination node
		if (source.equals(dest))
			return true;
		boolean found = false;
		// Calculate the path recursively 
		for (int i = 0; i < partialOrderSolution.getAdjacents(source).size() && !found; i++)
			found |= existsPath((String)partialOrderSolution.getAdjacents(source).get(i), dest);
		return found;
	}

	/**
	 * Gets the vertexName array
	 * @return The names of the vertex
	 */
	public ArrayList<String> getVertexName() {
		return vertexName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

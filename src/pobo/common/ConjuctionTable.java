package pobo.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic Conjunction Table
 */
public class ConjuctionTable {
	/** Table name */
	private String name;
	/** Parents list */ 
	private ArrayList<String> parents;
	/** Table */
	private HashMap<String, Integer> table;
	
	/**
	 * Default Constructor: creates an empty conjunction table
	 */
	public ConjuctionTable() {
		this.table = new HashMap<String, Integer>();
		this.name = null;
		this.parents = new ArrayList<String>();
	}
	
	/**
	 * One argument Constructor: creates an empty conjunction table with name
	 */
	public ConjuctionTable(String name) {
		this.table = new HashMap<String, Integer>();
		this.name = name;
		this.parents = new ArrayList<String>();
	}
	
	/**
	 * Three arguments constructor: creates a new conjunction table 
	 * @param name Table name
	 * @param parents Parents configuration
	 * @param table Conjunction Table
	 */
	public ConjuctionTable(String name, ArrayList<String> parents, HashMap<String, Integer> table) {
		this.name = name;
		this.parents = parents;
		this.table = table;
	}
	
	/**
	 * Adds a row (preference) to the conjunction table
	 * @param conj Conjunction of the parent's values
	 * @param pref Preference
	 */
	public void addPreference(String conj, Integer pref) {
		this.table.put(conj, pref);
	}
	
	/**
	 * Sets a specified preference
	 * @param conj Conjunction of the parent's values
	 * @param pref Preference 
	 */
	public void setPreference(String conj, Integer pref) {
		this.table.put(conj, pref);
	}
	
	/**
	 * Gets the preference based on the parent's values (null if not exist)
	 * @param conj Conjunction of the parent's values
	 * @return The preference
	 */
	public Integer getPreference(String conj) {
		return this.table.get(conj);
	}

	/**
	 * Checks if the table contains the specified key (conjunction)
	 * @param conj
	 * @return True if the preference exists
	 */
	public boolean checkPreference(String conj) {
		return this.table.containsKey(conj);
	}	
	
	/**
	 * Gets the name of the table
	 * @return The name of the node
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the table
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the parents configuration
	 * @return The parents of the node
	 */
	public ArrayList<String> getParents() {
		return parents;
	}

	/**
	 * Sets the parents configuration
	 */
	public void setParents(ArrayList<String> parents) {
		this.parents = parents;
	}
	
	/**
	 * Method toString override
	 * @return The conjunction table in string format
	 */
	public String toString() {
		String out = "";
		for (Map.Entry<String, Integer> m : table.entrySet()) {
			//out += m.getKey()+" "+m.getValue()+"\n";
			out += m.getKey()+" as "+getTextualConfiguration(m.getKey())+" "+m.getValue()+"\n";
		}
		return out;
	}
	
	/**
	 * Converts the binary configuration of the parents to text format
	 * @param binaryConf Parent's binary configuration
	 * @return The binary configuration in textual format
	 */
	public String getTextualConfiguration(String binaryConf) {
		String out = "";
		for (int i = 0; i < binaryConf.length(); i++) {
			if (binaryConf.charAt(i) == '0')
				out += "!";
			out += parents.get(i)+" ";
		}
		return out;
	}
	
	/**
	 * Gets the conjunction table
	 * @return The conjunction table 
	 */
	public HashMap<String, Integer> getTable() {
		return table;
	}

	/**
	 * Gets the number of the parents
	 * @return The number of parents
	 */
	public int getNumParents() {
		return parents.size();
	}
}

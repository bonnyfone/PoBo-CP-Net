package pobo.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Iterator;

/**
 * AdjacentList structure for CPNET solution representation
 */
public class CPNETAdjList {

	/**the adj list */
	private HashMap<String, ArrayList<String>> list;
	
	/**
	 * Build an adjacent list
	 * @param n create all 2^n configuration
	 */
	public CPNETAdjList(int n){
		list = Util.createBinaryArrayListHashMap(n);
	} 
	
	/**
	 * Add new adjacent element at specific configuration node
	 * @param key the configuration node 
	 * @param value the adjacent
	 * @return success of the operation
	 */
	public boolean addAdj(String key, String value){
		try{
			list.get(key).add(value);
			return true;
		}
		catch(Exception e){e.printStackTrace(); return false;}
	}
	
	/**
	 * Get the entire list
	 * @return list of adjacents
	 */
	public HashMap<String, ArrayList<String>> getList(){
		return list;
	}
	
	/**
	 * Get the list of the adjacent elements of a configuration node
	 * @param key configuration node
	 * @return list of adjacents
	 */
	public ArrayList<String> getAdjacents(String key){
		return list.get(key);
	}
	
	/**
	 * Get a specific adjacent element by id
	 * @param key configuration node
	 * @param i index of the element
	 * @return the element
	 */
	public String getAdjacentById(String key, int i){
		try{
			return list.get(key).get(i);
		}
		catch(Exception e){e.printStackTrace(); return "#";}
	}
	
	
	/**
	 * Remove all adjacent from specific node
	 * @param key the configuration node
	 * @return success of the operation
	 */
	public boolean removeAllAdjacents(String key){
		try{
			list.get(key).removeAll(list.get(key)); //per non richiamare clear..
			return true;
		}
		catch(Exception e){e.printStackTrace(); return false;}
	}
	
	/**
	 * Remove a specific adjacent by string value
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean removeAdjacent(String key, String value){
		try{
			list.get(key).remove(value);
			return true;
		}
		catch(Exception e){e.printStackTrace(); return false;}
	}
	
	/**
	 * Remove a specific adjacent by index value
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean removeAdjacent(String key, int value){
		try{
			list.get(key).remove(value);
			return true;
		}
		catch(Exception e){e.printStackTrace(); return false;}
	}
	
	/**
	 * Remove the redundant edges (multiply steps)
	 * @return
	 */
	public void graphPruning() {
		while (graphPruningStep());
	}
	
	/**
	 * Remove the redundant edges (one step)
	 * @return
	 */
	private boolean graphPruningStep() {
		Iterator<String> it = list.keySet().iterator();
		boolean mod = false;
		while(it.hasNext()) {
			String v = it.next();
			for (int i = 0; i < list.get(v).size(); i++) {
				if (getLongestPath(v, list.get(v).get(i), 0) > 1) {
					list.get(v).remove(i);
					mod = true;
				}
			}
		}
		return mod;
	}
	
	/**
	 * Get the edge count of the longest path between 2 vertex
	 * @param vSource
	 * @param vDest
	 * @param cPath
	 * @return
	 */
	private int getLongestPath(String vSource, String vDest, int cPath) {
		if (vSource.equals(vDest)) return cPath;
		int max = 0, temp = 0;
		for (int i = 0; i < list.get(vSource).size(); i++) {
			temp = getLongestPath(list.get(vSource).get(i), vDest, cPath + 1);
			if (temp > max) max = temp;
		}
		return max;
	}

	@Override
	public String toString() {
		
		String ris ="";
		
		for(Entry<String, ArrayList<String>> e : list.entrySet()){
			ris += e.getKey()+": ";
			for(int i=0; i<e.getValue().size(); i++){
				ris += e.getValue().get(i) + ", ";
			}
			ris+="\n";
		}
		
		return ris;
	}
	
}

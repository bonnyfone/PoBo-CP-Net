package pobo.common;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Variuous util
 */
public class Util {
	/** Color enumeration */
	public static enum Color { WHITE, GREY, BLACK };
	/** Relation enumeration */
	public static enum Relation { BETTER, WORSE, INCOMPARABLE, EQUAL };
	
	/**
	 * Create the binary enumeration for a specific exponent
	 * @param n (2^n)
	 */
	public static HashMap<String, Integer> createBinaryEnumHashMap(int n){
		
		HashMap<String, Integer> bin_enum = new HashMap<String, Integer>();
		Integer from = (int) (0+Math.pow(2, n)) ;		

		for(int i=0;i<from;i++)
			bin_enum.put(extendBinaryString(Integer.toBinaryString(i),n), -1);
		
		return bin_enum;
	}
	
	/** 
	 * Create the binary arrayList for a specific exponent
	 * @param n (2^n)
	 */
	public static HashMap<String, ArrayList<String>> createBinaryArrayListHashMap(int n){
		
		HashMap<String, ArrayList<String>> bin_enum = new HashMap<String, ArrayList<String>>();
		Integer from = (int) (0+Math.pow(2, n)) ;		

		for(int i=0;i<from;i++)
			bin_enum.put(extendBinaryString(Integer.toBinaryString(i),n), new ArrayList<String>());
		
		return bin_enum;
	}
	
	/** 
	 * Create the binary color for a specific exponent
	 * @param n (2^n)
	 */
	public static HashMap<String, Util.Color> createBinaryColorHashMap(int n){
		
		HashMap<String, Util.Color> bin_enum = new HashMap<String, Util.Color>();
		Integer from = (int) (0+Math.pow(2, n)) ;		

		for(int i=0;i<from;i++)
			bin_enum.put(extendBinaryString(Integer.toBinaryString(i),n), Util.Color.WHITE);
		
		return bin_enum;
	}
	 

	/**
	 * Support method for extends binary string to match correct lenght
	 * @param s
	 * @param lenght
	 * @return
	 */
	private static String extendBinaryString(String s, int lenght){
		while(s.length()<lenght) s = "0"+s;
		return s;
	}

	public static ArrayList<String> getPreferenceNeighbours(String pref) {
		ArrayList<String> neighbour = new ArrayList<String>();
		StringBuilder s= new StringBuilder(pref);
		for (int i = 0; i < pref.length(); i++) {
			
			if (pref.charAt(i) == '0') {
				s.setCharAt(i, '1');
				neighbour.add(s.toString());
				s.setCharAt(i, '0');
			} else {
				s.setCharAt(i, '0');
				neighbour.add(s.toString());
				s.setCharAt(i, '1');
			}
			
		}
		
		return neighbour;
	}
	
//	
//	public static void main(String args[]){
//		createBinaryEnumHashMap(5);
//	}
	
}

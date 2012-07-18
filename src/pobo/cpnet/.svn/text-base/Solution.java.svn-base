package pobo.cpnet;

import java.util.ArrayList;

/**
 * Represent a CPNET solution
 */
public class Solution {

	private char[] solution;
	
	public Solution(int n){
		solution = new char[n];
		for(int i = 0; i < n; i++)
			solution[i] = '#';
	}
	
	public void setConfiguration(int pos, char val){
		solution[pos] = val;
	}
	
	public char[] getSolutionAsChars(){
		return solution;
	}
	
	public String getSolutionAsString(){
		return new String(solution);
	}
	
	public static String buildComprensiveSolution(ArrayList<String> vars,char[] solution){
		String ris ="";
		for(int i=0;i<solution.length;i++){
			
			if(solution[i] == '0')ris += "!";
			ris += vars.get(i)+" ";
		}
		return ris;
	}
	
}

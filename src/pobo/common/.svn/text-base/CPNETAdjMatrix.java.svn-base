package pobo.common;

import java.util.LinkedList;

/**
 * AdjacentMatrix for CP-NET data
 */
public class CPNETAdjMatrix extends AdjMatrix {


	/**
	 * Create an n x n adjacent matrix for CPNET
	 * @param n dimension of the matrix
	 */
	public CPNETAdjMatrix(int n) {
		matrix = new Integer[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				matrix[i][j] = 0;
	}

	/**
	 * Return element at position posM-posN
	 * @param posM
	 * @param posN
	 * @return
	 */
	@Override
	public Integer getCell(int posM, int posN) {
		// TODO Auto-generated method stub
		return (Integer) super.getCell(posM, posN);
	}	


	public LinkedList<Integer> getIndependentVars(){
		 LinkedList<Integer> ris = new LinkedList<Integer>();
		
		for(int i=0;i<matrix.length;i++){
			boolean independent = true;
			
			for(int j=0;independent && j< matrix.length;j++){
				if(getCell(j, i)==1) independent = false;;
			}
			if(independent)ris.addLast(i);
		}
		
		return ris;
	}
	
	/**
	 * Put an element in the matrix
	 * @param element
	 * @param posM
	 * @param posN
	 * @return the success of the operation
	 */
	@Override
	public boolean putCell(Object element, int posM, int posN) {
		// TODO Auto-generated method stub
		return super.putCell((Integer)element, posM, posN);
	}


	@Override
	public String toString() {
		String t ="";

		for (int i = 0; i< matrix.length; i++){
			for(int j = 0; j<matrix.length; j++){
				t+=""+matrix[i][j];
			}
			t+="\n";
		}

		return t;
	}


}

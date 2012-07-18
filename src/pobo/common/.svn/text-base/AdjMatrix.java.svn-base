package pobo.common;

/**
 * Generic AdjacentsMatrix
 */
public class AdjMatrix {

	/** data Matrix */
	protected Object[][] matrix;

	protected AdjMatrix(){}
	
	/**
	 * Create an n x n adjacent matrix
	 * @param n dimension of the matrix
	 */
	public AdjMatrix(int n){
		matrix = new Object[n][n];
	}
	
	
	
	/**
	 * Create an n x n adjacent matrix
	 * @param n rows of the matrix
	 * @param n columns of the matrix
	 */
	public AdjMatrix(int m, int n){
		matrix = new Object[m][n];
	}
	
	
	/**
	 * Return element at position posM-posN
	 * @param posM
	 * @param posN
	 * @return
	 */
	public Object getCell(int posM, int posN){
		return matrix[posM][posN];
	}
	

	/**
	 * Check if a cell is filled.
	 * @param posM
	 * @param posN
	 * @return
	 */
	public boolean checkCellFilled(int posM, int posN){
		if(matrix[posM][posN].equals(null))return false;
		else return true;
	}
	
	
	/**
	 * Put an element in the matrix
	 * @param element
	 * @param posM
	 * @param posN
	 * @return the success of the operation
	 */
	public boolean putCell(Object element, int posM, int posN){
		try{
			matrix[posM][posN] = element;
			return true;
		}catch(Exception e){return false;}
	}


	
	public Object[][] getMatrix() {
		return matrix;
	}


	public void setMatrix(Object[][] matrix) {
		this.matrix = matrix;
	}
	
}

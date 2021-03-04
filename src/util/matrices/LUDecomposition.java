package util.matrices;

import util.matrices.exceptions.ZeroPivotException;

public class LUDecomposition {
	
	public Matrix decompose(Matrix m) {
		if (m.getNoOfCols()!=m.getNoOfRows()) throw new IllegalArgumentException("Matrica mora biti kvadratna!");
		
		Matrix A = new Matrix(m.getNoOfRows(), m.getNoOfCols());
		
		for(int i=0; i<m.getNoOfRows(); i++) {
			for (int j=0; j<m.getNoOfCols(); j++) {
				A.setValue(i, j, m.getValue(i, j));
			}
		}
		
		for(int i=0; i<A.getNoOfRows()-1; i++) {
			for(int j=i+1; j<A.getNoOfRows(); j++) {
				if (Math.abs(A.getValue(i, i))<Decomposition.epsilon) throw new ZeroPivotException("Elementi na dijagonali ne smiju biti nula za LU dekompoziciju!");
				
				A.setValue(j, i, A.getValue(j, i)/A.getValue(i, i));
				
				for(int k=i+1; k<A.getNoOfCols(); k++) {
					A.setValue(j, k, A.getValue(j, k)-A.getValue(j, i)*A.getValue(i, k));
				}
			}
		}
		
		return A;
	}

}

package util.matrices;

import util.matrices.exceptions.MatrixException;
import util.matrices.exceptions.ZeroPivotException;

public class LUPDecomposition {
	
	private Matrix P;
	private int swapCounter;
	
	public Matrix decompose(Matrix m) {
		if (m.getNoOfCols()!=m.getNoOfRows()) throw new IllegalArgumentException("Matrica mora biti kvadratna!");
		
		Matrix A = new Matrix(m.getNoOfRows(), m.getNoOfCols());
		
		swapCounter = 0;
		
		for(int i=0; i<m.getNoOfRows(); i++) { // kopiranje vrijednosti matrice m u novu matricu A
			for (int j=0; j<m.getNoOfCols(); j++) {
				A.setValue(i, j, m.getValue(i, j));
			}
		}
		
		P = new Matrix(m.getNoOfRows(), m.getNoOfCols());
		
		for (int i=0; i<P.getNoOfRows(); i++) { // popunjavanje permutacijske matrice (na početku jednaka jediničnoj)
			for (int j=0; j<P.getNoOfCols(); j++) {
				if (i==j) P.setValue(i, j, 1);
				else P.setValue(i, j, 0);
			}
		}
		
		
		for(int i=0; i<A.getNoOfRows()-1; i++) {
			int maxIndex = i;
			for (int j=i+1; j<A.getNoOfRows(); j++) {
				if(Math.abs(A.getValue(maxIndex, i)) < Math.abs(A.getValue(j, i))) {
					maxIndex = j;
				}
			}
			if (maxIndex != i) {
				A.swapRows(i, maxIndex);
				P.swapRows(i, maxIndex);
				swapCounter++;
			}

			if (Math.abs(A.getValue(i, i)) < Decomposition.epsilon) throw new ZeroPivotException("Stožerni element jednak nuli!");
			
			for (int j=i+1; j<A.getNoOfRows(); j++) {
				A.setValue(j, i, A.getValue(j, i)/A.getValue(i, i));
				for (int k=i+1; k<A.getNoOfCols(); k++) {
					A.setValue(j, k, A.getValue(j, k) - A.getValue(j, i)*A.getValue(i, k));
				}
			}
		}
		
		return A;
	}
	
	public Matrix getPermutationMatrix() {
		return this.P;
	}
	
	private int getSwapCounter() {
		return this.swapCounter;
	}
	
	public static Matrix calculateInverse(Matrix A) {
		try {
			LUPDecomposition ludp = new LUPDecomposition();
			Matrix LU = ludp.decompose(A);
			Matrix P = ludp.getPermutationMatrix();
			
			Matrix E = new Matrix(A.getNoOfRows(), A.getNoOfCols());
			for(int i=0; i<A.getNoOfRows(); i++) {
				for(int j=0; j<A.getNoOfCols(); j++) {
					if (j==i) E.setValue(i, j, 1);
					else E.setValue(i, j, 0);
				}
			}

			
			E = E.multiply(P);
			
			Matrix inverseA = new Matrix(A.getNoOfRows(), A.getNoOfCols());
			
			Matrix column = new Matrix(A.getNoOfRows(), 1);
			
			for (int i=0; i<A.getNoOfRows(); i++) {
				for (int j=0; j<A.getNoOfRows(); j++) {
					column.setValue(j, 0, E.getValue(j, i));
				}
				Matrix y = Substitutions.forwardSubstitution(LU, column);
				Matrix x = Substitutions.backSubstitution(LU, y);
				for (int j=0; j<A.getNoOfRows(); j++) {
					inverseA.setValue(j, i, x.getValue(j, 0));
				}
			}
			
			return inverseA;
			
		} catch (ArithmeticException e) {
			System.out.println("Ova matrica nema inverz");
			return null;
		}
	}
	
	public static double calculateDeterminant(Matrix A) {
		try {
			LUPDecomposition ludp = new LUPDecomposition();
			Matrix LU = ludp.decompose(A);
			Matrix P = ludp.getPermutationMatrix();
			double determinant = 1.0;
			int S = ludp.getSwapCounter();
			
			for (int i=0; i<P.getNoOfRows(); i++) {
				determinant *= LU.getValue(i, i);
			}
			
			return Math.pow(-1, S)*determinant;
		} catch (ZeroPivotException e) {
			throw new MatrixException("Determinantu ove matrice nije moguće izračunati preko LUP dekompozicije");
		}
	}
}

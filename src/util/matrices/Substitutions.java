package util.matrices;

import util.matrices.exceptions.MatrixCompatibilityException;

public class Substitutions {
	
	public static Matrix forwardSubstitution(Matrix L, Matrix b) {
		if (!compatibilityCheck(L,b)) throw new MatrixCompatibilityException("Nad ovim matricama se ne može provesti unapredna supstitucija zbog neodgovarajućih dimenzija!");
		
		Matrix y = new Matrix(L.getNoOfRows(),1);
		
		for (int i=0; i<y.getNoOfRows(); i++) {
			y.setValue(i, 0, b.getValue(i, 0));
		}
		
		for (int i=0; i<L.getNoOfRows()-1; i++) {
			for (int j=i+1; j<L.getNoOfRows(); j++) {
				y.setValue(j, 0, y.getValue(j, 0) - L.getValue(j, i)*y.getValue(i, 0)); // ne moramo dijeliti kao u unatražnoj supstituciji jer su elementi dijagonale matrice L jednaki 1
			}
		}
		
		return y;
	}
	
	public static Matrix backSubstitution(Matrix U, Matrix b) {
		if (!compatibilityCheck(U,b)) throw new MatrixCompatibilityException("Nad ovim matricama se ne može provesti unatražna supstitucija zbog neodgovarajućih dimenzija!");
		
		Matrix x = new Matrix(U.getNoOfRows(),1);
		
		for (int i=0; i<x.getNoOfRows(); i++) {
			x.setValue(i, 0, b.getValue(i, 0));
		}
		
		
		for (int i=U.getNoOfRows()-1; i>=0; i--) {
			if(Math.abs(U.getValue(i, i)) < Decomposition.epsilon) throw new ArithmeticException("Dijeljenje s nulom!");
			x.setValue(i, 0, x.getValue(i, 0)/U.getValue(i, i));
			
			for (int j=0; j<i; j++) {
				x.setValue(j, 0, x.getValue(j, 0) - U.getValue(j, i)*x.getValue(i, 0));
			}
		}
		
		return x;
	}
	
	private static boolean compatibilityCheck(Matrix L, Matrix b) {
		return L.getNoOfRows()==b.getNoOfRows() && L.getNoOfCols()==L.getNoOfRows();
	}

}

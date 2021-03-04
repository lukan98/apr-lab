package util.equations;

import util.matrices.LUDecomposition;
import util.matrices.LUPDecomposition;
import util.matrices.Matrix;
import util.matrices.Substitutions;

public class EquationSolver {
	
	public static Matrix solve(Matrix A, Matrix b) {
		try {
			System.out.println("Riješavam pomoću LU dekompozicije...");
			return LUD(A,b);
		} catch (EquationException e1) {
			try {
				System.out.println("Nisam uspio LU dekompozicijom, probat ću LUP dekompozicijom...");
				return LUPD(A,b);
			} catch (EquationException e2) {
				throw new EquationException("Ova jednadžba se ne može riješiti nijednom dekompozicijom, znam jer sam probao :(");
			}
		}
	}
	
	public static Matrix LUD(Matrix A, Matrix b) {
		try {
			LUDecomposition lud = new LUDecomposition();
			Matrix LU = lud.decompose(A);
			Matrix y = Substitutions.forwardSubstitution(LU, b);
			Matrix x = Substitutions.backSubstitution(LU, y);
			return x;
		} catch (Exception e) {
			throw new EquationException("Jednadžba se ne može riješiti LU dekompozicijom");
		}
	}
	
	public static Matrix LUPD(Matrix A, Matrix b) {
		try {
			LUPDecomposition ludp = new LUPDecomposition();
			Matrix LU = ludp.decompose(A);
			b = ludp.getPermutationMatrix().multiply(b);
			Matrix y = Substitutions.forwardSubstitution(LU, b);
			Matrix x = Substitutions.backSubstitution(LU, y);
			return x;
		} catch (Exception e) {
			throw new EquationException("Jednadžba se ne može riješiti LUP dekompozicijom");
		}
	}

}

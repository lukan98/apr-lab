package util.integration;

import util.matrices.Matrix;

public interface Corrector {
	
	public Matrix getNext(Matrix A, Matrix B, Matrix r, Matrix currentX, Matrix approxNextX, int timeDependency, double t, double step);
	
}

package util.integration;

import util.matrices.Matrix;

public interface Predictor {
	
	public Matrix getNext(Matrix A, Matrix B, Matrix r, Matrix currentX, int timeDependency, double t, double step);

}

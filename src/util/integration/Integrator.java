package util.integration;

import java.util.ArrayList;

import util.matrices.*;

public interface Integrator {
	
	public ArrayList<Matrix> integrate(Matrix A, Matrix B, Matrix r, int timeDependency, Matrix initialValues, double start, double end, double step);

}

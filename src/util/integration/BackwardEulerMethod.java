package util.integration;

import java.util.ArrayList;

import util.matrices.LUPDecomposition;
import util.matrices.Matrix;

public class BackwardEulerMethod implements Integrator, Corrector {
	
	private Integer outputStep;
	
	public BackwardEulerMethod() {
		this.outputStep = null;
	}
	
	public BackwardEulerMethod(int outputStep) {
		this.outputStep = outputStep;
	}

	@Override
	public ArrayList<Matrix> integrate(Matrix A, Matrix B, Matrix r, int timeDependency, Matrix initialValues, double start, double end, double step) {
		
		if (B==null)
			B = new Matrix(A.getNoOfRows(), A.getNoOfCols());
		if (r==null)
			r = new Matrix(initialValues.getNoOfRows(), 1);
		
		Matrix U = Matrix.unary(A.getNoOfCols());
		Matrix P = U.subtract(A.multiply(step));
		P = LUPDecomposition.calculateInverse(P);	
		Matrix Q = P.multiply(step).multiply(B);
		
		ArrayList<Matrix> calculatedValues = new ArrayList<>();
		calculatedValues.add(initialValues);

		double t = start;
		int iterationCounter = 0;
		
		while (t < end) {
			Matrix currentX = calculatedValues.get(calculatedValues.size()-1);
			if (this.outputStep!=null && iterationCounter%this.outputStep == 0) System.out.print(t+" "+currentX.transpose());
			t += step;
			
			Matrix nextX = P.multiply(currentX).add(Q.multiply(r.multiply(Math.pow(t+step, timeDependency))));
			
			calculatedValues.add(nextX);
			iterationCounter++;
		}
		
		return calculatedValues;
	}

	@Override
	public Matrix getNext(Matrix A, Matrix B, Matrix r, Matrix currentX, Matrix approxNextX, int timeDependency, double t, double step) {
		
		Matrix comp = A.multiply(approxNextX).add(B.multiply(r.multiply(Math.pow(t+step, timeDependency)))).multiply(step);
		
		return currentX.add(comp);
	}
}

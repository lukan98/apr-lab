package util.integration;

import java.util.ArrayList;

import util.matrices.*;

public class EulerMethod implements Integrator, Predictor {
	
	private Integer outputStep;
	
	public EulerMethod() {
		this.outputStep = null;
	}
	
	public EulerMethod(int outputStep) {
		this.outputStep = outputStep;
	}

	@Override
	public ArrayList<Matrix> integrate(Matrix A, Matrix B, Matrix r, int timeDependency, Matrix initialValues, double start, double end, double step) {
		
		if (B==null)
			B = new Matrix(A.getNoOfRows(), A.getNoOfCols());
		if (r==null)
			r = new Matrix(initialValues.getNoOfRows(), 1);
		
		ArrayList<Matrix> calculatedValues = new ArrayList<>();
		calculatedValues.add(initialValues);
		
		double t = start;
		int iterationCounter = 0;
		
		Matrix U = Matrix.unary(A.getNoOfRows());
		Matrix M = U.add(A.multiply(step));
		Matrix N = B.multiply(step);
		
		while (t < end) {
			Matrix currentX = calculatedValues.get(calculatedValues.size()-1);
			if (this.outputStep!=null && iterationCounter%this.outputStep == 0) System.out.print(t+" "+currentX.transpose());
			t += step;
			
			Matrix nextX = M.multiply(currentX).add(N.multiply(r.multiply(Math.pow(t, timeDependency))));
			
			calculatedValues.add(nextX);
			iterationCounter++;
		}
		
		return calculatedValues;
	}

	@Override
	public Matrix getNext(Matrix A, Matrix B, Matrix r, Matrix currentX, int timeDependency, double t, double step) {
		
		Matrix U = Matrix.unary(A.getNoOfRows());
		Matrix M = U.add(A.multiply(step));
		Matrix N = B.multiply(step);
		
		return M.multiply(currentX).add(N.multiply(r.multiply(Math.pow(t, timeDependency))));
	}

}

package util.integration;

import java.util.ArrayList;

import util.matrices.Matrix;

public class RungeKuttaMethod implements Integrator, Predictor {
	private Integer outputStep;
	
	public RungeKuttaMethod() {
		this.outputStep = null;
	}
	
	public RungeKuttaMethod(int outputStep) {
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
		
		while (t < end) {
			Matrix currentX = calculatedValues.get(calculatedValues.size()-1);
			if (this.outputStep!=null && iterationCounter%this.outputStep == 0) System.out.print(t+" "+currentX.transpose());
			t += step;
			
			Matrix m1 = A.multiply(currentX).add(B.multiply(r.multiply(Math.pow(t, timeDependency))));
			Matrix comp = currentX.add(m1.multiply(step/2.0));
			Matrix m2 = A.multiply(comp).add(B.multiply(r.multiply(Math.pow(t+step/2.0, timeDependency))));
			comp = currentX.add(m2.multiply(step/2.0));
			Matrix m3 = A.multiply(comp).add(B.multiply(r.multiply(Math.pow(t+step/2.0, timeDependency))));
			comp = currentX.add(m3.multiply(step));
			Matrix m4 = A.multiply(comp).add(B.multiply(r.multiply(Math.pow(t+step, timeDependency))));
			
			Matrix nextX = currentX.add(m1.add(m2.multiply(2)).add(m3.multiply(2)).add(m4).multiply(step/6.0));
			
			calculatedValues.add(nextX);
			iterationCounter++;
		}
		
		return calculatedValues;
	}

	@Override
	public Matrix getNext(Matrix A, Matrix B, Matrix r, Matrix currentX, int timeDependency, double t, double step) {
		
		Matrix m1 = A.multiply(currentX).add(B.multiply(r.multiply(Math.pow(t, timeDependency))));
		Matrix comp = currentX.add(m1.multiply(step/2.0));
		Matrix m2 = A.multiply(comp).add(B.multiply(r.multiply(Math.pow(t+step/2.0, timeDependency))));
		comp = currentX.add(m2.multiply(step/2.0));
		Matrix m3 = A.multiply(comp).add(B.multiply(r.multiply(Math.pow(t+step/2.0, timeDependency))));
		comp = currentX.add(m3.multiply(step));
		Matrix m4 = A.multiply(comp).add(B.multiply(r.multiply(Math.pow(t+step, timeDependency))));
		
		return currentX.add(m1.add(m2.multiply(2)).add(m3.multiply(2)).add(m4).multiply(step/6.0));
		
	}
}

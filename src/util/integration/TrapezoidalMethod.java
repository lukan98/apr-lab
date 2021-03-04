package util.integration;

import java.util.ArrayList;

import util.matrices.*;

public class TrapezoidalMethod implements Integrator, Corrector {
	
	private Integer outputStep;
	
	public TrapezoidalMethod() {
		this.outputStep = null;
	}
	
	public TrapezoidalMethod(int outputStep) {
		this.outputStep = outputStep;
	}

	@Override
	public ArrayList<Matrix> integrate(Matrix A, Matrix B, Matrix r, int timeDependency, Matrix initialValues, double start, double end, double step) {
		
		if (B==null)
			B = new Matrix(A.getNoOfRows(), A.getNoOfCols());
		if (r==null)
			r = new Matrix(initialValues.getNoOfRows(), 1);
		
		Matrix U = Matrix.unary(A.getNoOfCols());
		Matrix inverseFactor = LUPDecomposition.calculateInverse(U.subtract(A.multiply(step/2.0)));
		Matrix factor = U.add(A.multiply(step/2.0));
		Matrix R = inverseFactor.multiply(factor);
		Matrix S = inverseFactor.multiply(step/2.0).multiply(B);
		
		ArrayList<Matrix> calculatedValues = new ArrayList<>();
		calculatedValues.add(initialValues);

		double t = start;
		int iterationCounter = 0;
		
		while (t < end) {
			Matrix currentX = calculatedValues.get(calculatedValues.size()-1);
			if (this.outputStep!=null && iterationCounter%this.outputStep == 0) System.out.print(t+" "+currentX.transpose());
			t += step;
			
			Matrix nextX = R.multiply(currentX).add(S.multiply(r.multiply(Math.pow(t, timeDependency)).add(r.multiply(Math.pow(t+step, timeDependency)))));
			
			calculatedValues.add(nextX);
			iterationCounter++;
		}
		
		return calculatedValues;
	}

	@Override
	public Matrix getNext(Matrix A, Matrix B, Matrix r, Matrix currentX, Matrix approxNextX, int timeDependency, double t, double step) {
		
		Matrix comp1 = A.multiply(currentX).add(B.multiply(r.multiply(Math.pow(t, timeDependency))));
		Matrix comp2 = A.multiply(approxNextX).add(B.multiply(r.multiply(Math.pow(t+step, timeDependency))));
		
		return currentX.add(comp1.add(comp2).multiply(step/2.0));
	}

}

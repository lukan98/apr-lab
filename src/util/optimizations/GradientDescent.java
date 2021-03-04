package util.optimizations;

import java.util.ArrayList;

import util.functions.DerivableFunction;
import util.functions.Function;
import util.matrices.Matrix;

public class GradientDescent {
	
	private double epsilon;
	
	public GradientDescent(double epsilon) {
		this.epsilon = epsilon;
	}
	
	public Matrix findMinimum(Matrix startingPoint, DerivableFunction f, boolean optimalSteps, boolean verbose) {
		Matrix v, x;
		int iterationCount = 0;
		
		x = new Matrix(startingPoint);
		while(true) {
			Matrix xs = new Matrix(x);
			v = f.getDerivativeAt(xs);
			v = v.multiply(-1.0);
			
			if (optimalSteps) {
				DimensionWrapper dw = new DimensionWrapper(f, xs, v);
				GoldenSection gs = new GoldenSection(this.epsilon);
				ArrayList<Double> interval = gs.findMinimum(0.0, dw);
				double lambda = (interval.get(0) + interval.get(1))/2.0;
				x = x.add(v.multiply(lambda));
			}
			else {
				x = x.add(v);
			}
			iterationCount++;
		
			if (f.getDerivativeAt(x).getNorm() < this.epsilon) break;
			if (iterationCount>10000) break;
		}
		if (verbose) {
			System.out.println("Broj iteracija gradijetnog spusta: " + iterationCount);
			System.out.println("Broj evaluacija derivacije funkcije: " + f.getDerivativeCallCount());
		}
		
		f.resetFunction();
		
		return x;
	}
	
	private class DimensionWrapper extends Function {

		private Function wrappedFunction;
		private Matrix x;
		private Matrix v;
		
		public DimensionWrapper(Function f, Matrix x, Matrix v) {
			this.x = x;
			this.wrappedFunction = f;
			this.v = v;
		}
		
		public double getValueAt(Matrix lambda) {
			Matrix wrapperMatrix = new Matrix(x);
			for (int i=0; i<wrapperMatrix.getNoOfRows(); i++) {
				wrapperMatrix.setValue(i, 0, wrapperMatrix.getValue(i, 0)+lambda.getValue(0, 0)*v.getValue(i, 0));
			}
			return this.wrappedFunction.getValueAt(wrapperMatrix);
		}
		
	}
}

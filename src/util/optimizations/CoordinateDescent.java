package util.optimizations;

import java.util.ArrayList;

import util.functions.Function;
import util.matrices.Matrix;

public class CoordinateDescent {
	
	private double epsilon;
	
	public CoordinateDescent(double precision) {
		this.epsilon = precision;
	}
	
	public Matrix findMinimum(Matrix startingPoint, Function f, boolean verbose) {
		GoldenSection gs = new GoldenSection(this.epsilon);
		Matrix x = new Matrix(startingPoint);
		Matrix xs;
		do {
			xs = new Matrix(x);
			for (int i=0; i<startingPoint.getNoOfRows(); i++) {
				DimensionWrapper dw = new DimensionWrapper(f, x, i);
				ArrayList<Double> interval = gs.findMinimum(x.getValue(i, 0), dw);
				double lambda = (interval.get(0)+interval.get(1))/2.0;
				x.setValue(i, 0, x.getValue(i, 0)+lambda);
			}
		} while (x.subtract(xs).getNorm() <= this.epsilon);
		
		if (verbose) {
			System.out.println("Broj jedinstvenih poziva funkcije cilja: "+f.getCallCount());
		}
		
		f.resetFunction();
		return x;
	}
	
	private class DimensionWrapper extends Function {

		private Function wrappedFunction;
		private Matrix x;
		private int dimension;
		
		public DimensionWrapper(Function f, Matrix x, int dimension) {
			this.x = x;
			this.wrappedFunction = f;
			this.dimension = dimension;
		}
		
		public double getValueAt(Matrix lambda) {
			Matrix wrapperMatrix = new Matrix(x);
			wrapperMatrix.setValue(dimension, 0, wrapperMatrix.getValue(dimension, 0)+lambda.getValue(0, 0));
			return this.wrappedFunction.getValueAt(wrapperMatrix);
		}
		
	}
}

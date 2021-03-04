package util.optimizations;

import java.util.List;

import util.functions.Function;
import util.matrices.Matrix;

public class BarrierPenaltyTransform {
	
	private double epsilon, t;
	
	public BarrierPenaltyTransform(double epsilon, double t) {
		this.epsilon = epsilon;
		this.t = t;
	}
	
	public Matrix findMinimum(Matrix startingPoint, List<Function> inequalityConstraints, List<Function> equalityConstraints, Function f) {
		
		Matrix x;
		if (satisfiesConstraints(startingPoint, inequalityConstraints)) x = new Matrix(startingPoint);
		else {
			x = findInnerPoint(startingPoint, inequalityConstraints);
			System.out.println("Zadana početna točka ne zadovoljava nejednakosti, mijenjam ju u sljedeću: " + x.transpose());
		}
		
		Matrix xs;
		Matrix dX = new Matrix(x);
		Matrix epsilonHJ = new Matrix(x);
		
		for(int i=0; i<x.getNoOfRows(); i++) {
			dX.setValue(i, 0, 1.0);
			epsilonHJ.setValue(i, 0, this.epsilon);
		}
		
		HookeJeves hj = new HookeJeves(dX, epsilonHJ);
		
		TotalFunction tf = new TotalFunction(f, inequalityConstraints, equalityConstraints, this.t);
		
		do {
			
			xs = new Matrix(x);
			x = new Matrix(hj.findMinimum(x, tf, false));
			tf.increaseT(10);
			
		} while(x.subtract(xs).getNorm() > this.epsilon);
		
		return x;
	}
	
	private class TotalFunction extends Function {
		
		private Function baseFunction;
		private List<Function> inequalityConstraints;
		private List<Function> equalityConstraints;
		private double t;
		
		public TotalFunction(Function f, List<Function> inequalityConstraints, List<Function> equalityConstraints, double t) {
			this.baseFunction = f;
			this.inequalityConstraints = inequalityConstraints;
			this.equalityConstraints = equalityConstraints;
			this.t = t;
		}
		
		public void increaseT(double factor) {
			this.t *= factor;
		}

		@Override
		public double getValueAt(Matrix x) {
			double F = baseFunction.getValueAt(x);
			
			for(Function inequalityConstraint : inequalityConstraints) {
				double g = inequalityConstraint.getValueAt(x);
				
				if (g<0) return Double.POSITIVE_INFINITY;
				else F -= (1/this.t)*Math.log(g);
			}
			
			for(Function equalityConstraint : equalityConstraints) {
				double h = equalityConstraint.getValueAt(x);
				
				F += this.t*Math.pow(h, 2);
			}
		
			return F;
		}
		
	}
	
	private boolean satisfiesConstraints(Matrix x, List<Function> inequalityConstraints) {
		for (Function constraint : inequalityConstraints) {
			if (constraint.getValueAt(x) < 0) return false;
		}
		return true;
	}
	
	private Matrix findInnerPoint(Matrix startingPoint, List<Function> inequalityConstraints) {
		Matrix dX = new Matrix(startingPoint);
		Matrix epsilon = new Matrix(startingPoint);
		
		for (int i=0; i<startingPoint.getNoOfRows(); i++) {
			dX.setValue(i, 0, 1.0);
			epsilon.setValue(i, 0, this.epsilon);
		}
		
		HookeJeves hj = new HookeJeves(dX, epsilon);
		
		StartingPointFunction spf = new StartingPointFunction(inequalityConstraints);
		
		return new Matrix(hj.findMinimum(startingPoint, spf, false));
	}
	
	private class StartingPointFunction extends Function {
		
		private List<Function> inequalityConstraints;
		
		public StartingPointFunction(List<Function> inequalityConstraints) {
			this.inequalityConstraints = inequalityConstraints;
		}

		@Override
		public double getValueAt(Matrix x) {
			double sum = 0;
			
			for (Function inequalityConstraint : inequalityConstraints) {
				double g = inequalityConstraint.getValueAt(x);
				
				if (g<0) sum -= g;
			}
			
			return sum;
		}
	}

}

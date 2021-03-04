package util.optimizations;

import java.util.ArrayList;

import util.functions.Function;
import util.matrices.Matrix;

public class NelderMeadSimplex {
	
	private double alpha, beta, gamma, epsilon;
	
	public NelderMeadSimplex(double alpha, double beta, double gamma, double epsilon) {
		this.alpha = alpha;
		this.beta = beta;
		this.gamma = gamma;
		this.epsilon = epsilon;
	}
	
	public Matrix findMinimum(Matrix startingPoint, Function f, double step, boolean verbose) {
		ArrayList<Matrix> simplex = generateSimplex(startingPoint, step);
		int l, h;
		Matrix xC, xR, xE, xK;
		
		l = findMinimum(simplex, f);
		h = findMaximum(simplex, f);
		xC = calculateCentroid(simplex, h);
		
		do {
			l = findMinimum(simplex, f);
			h = findMaximum(simplex, f);
			xC = calculateCentroid(simplex, h);
			xR = reflection(xC, simplex.get(h));
			
			if (f.getValueAt(xR) < f.getValueAt(simplex.get(l)))
			{
				xE = expansion(xC, xR);
				if (f.getValueAt(xE) < f.getValueAt(simplex.get(l))) simplex.set(h, new Matrix(xE));
				else simplex.set(h, new Matrix(xR));
			} 
			else
			{
				if (loopCondition(simplex, xR, h, f)) {
					if (f.getValueAt(xR) < f.getValueAt(simplex.get(h))) simplex.set(h, new Matrix(xR));
					xK = contraction(xC, simplex.get(h));
					if (f.getValueAt(xK) < f.getValueAt(simplex.get(h))) simplex.set(h, new Matrix(xK));
					else moveToward(simplex, simplex.get(l));
				}
				else simplex.set(h, new Matrix(xR));
			}
		} while(!stopCondition(simplex, xC, f));
		
		if (verbose) {
			System.out.println("Broj jedinstvenih poziva funkcije cilja: "+f.getCallCount());
		}
		
		f.resetFunction();
		return xC;
	}
	
	private ArrayList<Matrix> generateSimplex(Matrix startingPoint, double step) {
		ArrayList<Matrix> simplex = new ArrayList<>();
		simplex.add(startingPoint);
		int n = startingPoint.getNoOfRows();
		
		for (int i=0; i<n; i++) {
			Matrix point = new Matrix(startingPoint);
			point.setValue(i, 0, point.getValue(i, 0)+step);
			simplex.add(point);
		}
		
		return simplex;
	}
	
	private int findMaximum(ArrayList<Matrix> simplex, Function f) {
		int h=0;
		int n = simplex.size();
		
		for (int i=0; i<n; i++) {
			if (f.getValueAt(simplex.get(i)) > f.getValueAt(simplex.get(h))) h = i;
		}
		
		return h;
	}
	
	private int findMinimum(ArrayList<Matrix> simplex, Function f) {
		int l = 0;
		int n = simplex.size();
		
		for (int i=0; i<n; i++) {
			if (f.getValueAt(simplex.get(i)) < f.getValueAt(simplex.get(l))) l = i;
		}
		
		return l;
	}
	
	private Matrix calculateCentroid(ArrayList<Matrix> simplex, int h) {
		Matrix xC = null;
		int n = simplex.size();
		
		for (int i=0; i<n; i++) {
			if (i==h) continue;
			if (xC==null) xC = new Matrix(simplex.get(i));
			else xC = xC.add(simplex.get(i));
		}
		
		xC = xC.multiply(1/(double)(n-1));
		
		return xC;
	}
	
	private Matrix reflection(Matrix xC, Matrix xH) {
		Matrix term1 = new Matrix(xC).multiply(1+this.alpha);
		Matrix term2 = new Matrix(xH).multiply(this.alpha);
		
		return term1.subtract(term2);
	}
	
	private Matrix expansion(Matrix xC, Matrix xR) {
		Matrix term1 = new Matrix(xC).multiply(1-this.gamma);
		Matrix term2 = new Matrix(xR).multiply(this.gamma);
		
		return term1.add(term2);
	}
	
	private Matrix contraction(Matrix xC, Matrix xH) {
		Matrix term1 = new Matrix(xC).multiply(1-this.beta);
		Matrix term2 = new Matrix(xH).multiply(this.beta);
		
		return term1.add(term2);
	}
	
	private void moveToward(ArrayList<Matrix> simplex, Matrix xL) {
		int n = simplex.size();
		
		for (int i=0; i<n; i++) {
			simplex.set(i, simplex.get(i).add(xL).multiply(0.5));
		}
	}
	
	private boolean loopCondition(ArrayList<Matrix> simplex, Matrix xR, int h, Function f) {
		for(int i=0; i<simplex.size(); i++) {
			if (i==h) continue;
 			if (f.getValueAt(xR) > f.getValueAt(simplex.get(i))) continue;
 			else return false;
		}
		return true;
	}
	
	private boolean stopCondition(ArrayList<Matrix> simplex, Matrix xC, Function f) {
		double sum = 0;
		int n = simplex.size();
		
		for (int i=0; i<n; i++) {
			sum += Math.pow(f.getValueAt(simplex.get(i))-f.getValueAt(xC), 2);
		}
		
		sum /= (double)n;
		
		double result = Math.sqrt(sum);
		
		return result <= this.epsilon;
	}
}

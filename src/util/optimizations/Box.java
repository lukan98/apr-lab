package util.optimizations;

import java.util.ArrayList;
import java.util.List;

import util.functions.Function;
import util.matrices.Matrix;

public class Box {
	
	private double epsilon = 10e-6;
	private double alpha = 1.3;
	
	public Matrix findMinimum(Matrix startingPoint, Function f, List<double[]> explicitConstraints, List<Function> implicitConstraints) {
		
		if (!checkImplicitConstraints(startingPoint, implicitConstraints) || !checkExplicitConstraints(startingPoint, explicitConstraints))
			throw new IllegalArgumentException("Početna točka mora zadovoljavati sva ograničenja!");
		
		Matrix xCentroid = new Matrix(startingPoint);
		
		ArrayList<Matrix> points = generatePoints(xCentroid, explicitConstraints, implicitConstraints);
		
		do {
			int h = findMaximum(points, f);
			
			ArrayList<Matrix> points2 = new ArrayList<>(points);
			points2.remove(h);
			int h2 = findMaximum(points2, f);
			
			xCentroid = calculateCentroid(points, h);
			
			Matrix xR = reflection(xCentroid, points.get(h));
			
			for(int i=0; i<xR.getNoOfRows(); i++) {
				if (xR.getValue(i, 0) < explicitConstraints.get(i)[0]) xR.setValue(i, 0, explicitConstraints.get(i)[0]);
				if (xR.getValue(i, 0) > explicitConstraints.get(i)[1]) xR.setValue(i, 0, explicitConstraints.get(i)[1]);
			}
			
			while(!checkImplicitConstraints(xR, implicitConstraints))
				xR = moveTowardCentroid(xR, xCentroid);
			
			if(f.getValueAt(xR) > f.getValueAt(points.get(h2)))
				xR = moveTowardCentroid(xR, xCentroid);
			
			points.set(h, new Matrix(xR));
			
		} while (!checkEndingCondition(xCentroid, points, f));
		
		int l = findMinimum(points, f);
		
		return points.get(l);
	}
	
	
	private boolean checkEndingCondition(Matrix xCentroid, ArrayList<Matrix> points, Function f) {
		double sum = 0;
		
		for(int i=0; i<points.size(); i++) {
			sum += Math.pow(f.getValueAt(points.get(i)) - f.getValueAt(xCentroid), 2);
		}
		
		sum = 0.5*sum;
		sum = Math.sqrt(sum);
		
		return sum<this.epsilon;
		
	}
	
	
	private ArrayList<Matrix> generatePoints(Matrix xCentroid, List<double[]> explicitConstraints, List<Function> implicitConstraints) {
		int n = xCentroid.getNoOfRows();
		ArrayList<Matrix> points = new ArrayList<>();
		points.add(new Matrix(xCentroid));
		
		for(int j=1; j<2*n; j++) {
			Matrix x = new Matrix(n, 1);
			
			for(int i=0; i<n; i++) {
				x.setValue(i, 0, explicitConstraints.get(i)[0] + Math.random()*(explicitConstraints.get(i)[1]-explicitConstraints.get(i)[0]));
			}
			
			while(!checkImplicitConstraints(x, implicitConstraints))
				x = moveTowardCentroid(x, xCentroid);
			
			points.add(x);
			xCentroid = updateCentroid(points);
		}
		
		return points;
	}
	
	private Matrix moveTowardCentroid(Matrix x, Matrix xCentroid) {
		Matrix result = new Matrix(x);
		return result.add(xCentroid).multiply(0.5);
	}
	
	private Matrix reflection(Matrix xC, Matrix xH) {
		Matrix xR;
		Matrix term1, term2;
		
		term1 = xC.multiply(this.alpha + 1);
		term2 = xH.multiply(this.alpha);
		
		xR = term1.subtract(term2);
		
		return xR;
	}
	
	private boolean checkExplicitConstraints(Matrix x, List<double[]> explicitConstraints) {
		for (int i=0; i<x.getNoOfRows(); i++) {
			if (x.getValue(i, 0) < explicitConstraints.get(i)[0] || x.getValue(i, 0) > explicitConstraints.get(i)[1]) return false;
		}
		return true;
	}
	
	private boolean checkImplicitConstraints(Matrix x, List<Function> implicitConstraints) {
		for (Function constraint : implicitConstraints) {
			if (constraint.getValueAt(x) < 0) return false;
		}
		return true;
	}
	
	private Matrix updateCentroid(ArrayList<Matrix> points) {
		Matrix xCentroid = new Matrix(points.get(0));
		
		for (int i=1; i<points.size(); i++) {
			xCentroid = xCentroid.add(points.get(i));
		}
		
		xCentroid = xCentroid.multiply(1/(double)points.size());
		
		return xCentroid;
	}
	
	private int findMaximum(ArrayList<Matrix> points, Function f) {
		int h=0;
		int n = points.size();
		
		for (int i=0; i<n; i++) {
			if (f.getValueAt(points.get(i)) > f.getValueAt(points.get(h))) h = i;
		}
		
		return h;
	}
	
	private int findMinimum(ArrayList<Matrix> points, Function f) {
		int l = 0;
		int n = points.size();
		
		for (int i=0; i<n; i++) {
			if (f.getValueAt(points.get(i)) < f.getValueAt(points.get(l))) l = i;
		}
		
		return l;
	}
	
	private Matrix calculateCentroid(ArrayList<Matrix> points, int h) {
		Matrix xC = null;
		int n = points.size();
		
		for (int i=0; i<n; i++) {
			if (i==h) continue;
			if (xC==null) xC = new Matrix(points.get(i));
			else xC = xC.add(points.get(i));
		}
		
		xC = xC.multiply(1/(double)(n-1));
		
		return xC;
	}

}

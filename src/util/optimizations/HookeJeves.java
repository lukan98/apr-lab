package util.optimizations;

import util.functions.Function;
import util.matrices.Matrix;

public class HookeJeves {

	private Matrix dX;
	private Matrix epsilon;
	
	public HookeJeves(Matrix dX, Matrix epsilon) {
		if (dX.getNoOfRows() != epsilon.getNoOfRows()) throw new RuntimeException("Vektori epsilon i dX moraju imati iste dimenzije!");
		this.dX = new Matrix(dX);
		this.epsilon = new Matrix(epsilon);
	}
	
	public Matrix findMinimum(Matrix x0, Function f, boolean verbose) {
		if (x0.getNoOfRows()!=dX.getNoOfRows()) throw new RuntimeException("Vektor dX ne odgovara dimenzijama domene funkcije! Stvorite novi HJ objekt!");
		Matrix dX = new Matrix(this.dX);
		Matrix xP = new Matrix(x0);
		Matrix xB = new Matrix(x0);
		Matrix xN;
		
		
		do {
			xN = search(xP, dX, f);
			if (f.getValueAt(xN) < f.getValueAt(xB)) {
				xP = xN.multiply(2).subtract(xB);
				xB = new Matrix(xN);
			} else {
				dX = dX.multiply(0.5);
				xP = new Matrix(xB);
			}
		} while(!checkCondition(dX));
		
		if (verbose) {
			System.out.println("Broj jedinstvenih poziva funkcije cilja: "+f.getCallCount());
		}
		
		f.resetFunction();
		return xB;
	}
	
	private Matrix search(Matrix xP, Matrix dX, Function f) {
		Matrix x = new Matrix(xP);
		int n = x.getNoOfRows();
		
		for (int i=0; i<n; i++) {
			double P = f.getValueAt(x);
			x.setValue(i, 0, x.getValue(i, 0)+dX.getValue(i, 0));
			double N = f.getValueAt(x);
			if (N>P) {
				x.setValue(i, 0, x.getValue(i, 0)-2*dX.getValue(i, 0));
				N = f.getValueAt(x);
				if (N>P) {
					x.setValue(i, 0, x.getValue(i, 0)+dX.getValue(i, 0));
				}
			}
		}
		
		return x;
	}
	
	private boolean checkCondition(Matrix dX) {
		int n = dX.getNoOfRows();
		
		for (int i=0; i<n; i++) {
			if (dX.getValue(i, 0) > this.epsilon.getValue(i, 0)) return false;
		}
		
		return true;
	}
}

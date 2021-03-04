package util.functions;


import java.util.HashMap;

import util.matrices.Matrix;

public abstract class Function {
	
	int callCount;
	HashMap<Matrix, Double> memorizedValues;
	
	public abstract double getValueAt(Matrix x);
	
	public int getCallCount() {
		return this.callCount;
	}
	
	public void resetFunction() {
		this.callCount = 0;
		this.memorizedValues = new HashMap<>();
	}
	
}

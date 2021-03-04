package util.functions;

import java.util.HashMap;

import util.matrices.Matrix;

public class Function6 extends Function {
	
	public Function6() {
		super.callCount = 0;
		super.memorizedValues = new HashMap<>();
	}

	@Override
	public double getValueAt(Matrix x) {
		if (super.memorizedValues.containsKey(x)) {
			return super.memorizedValues.get(x);
		} else {
			double sum = 0;
			int n = x.getNoOfRows();
			for (int i=0; i<n; i++) {
				sum += Math.pow(x.getValue(i, 0), 2);
			}
			double termA = Math.pow(Math.sin(Math.sqrt(sum)), 2) - 0.5;
			double termB = Math.pow(1.0+0.001*sum, 2);
			double result = 0.5 + termA/termB;
			
			super.callCount++;
			super.memorizedValues.put(x, result);
			return result;
		}
	}

}

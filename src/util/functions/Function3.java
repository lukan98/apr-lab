package util.functions;

import java.util.HashMap;

import util.matrices.Matrix;

public class Function3 extends Function{
	
	public Function3() {
		super.callCount = 0;
		super.memorizedValues = new HashMap<>();
	}

	@Override
	public double getValueAt(Matrix x) {
		if (super.memorizedValues.containsKey(x)) {
			return super.memorizedValues.get(x);
		}
		else {
			double sum=0;
			int n = x.getNoOfRows();
			for (int i=0; i<n; i++) {
				sum += Math.pow(x.getValue(i, 0)-(i+1), 2);
			}
			super.callCount++;
			super.memorizedValues.put(x, sum);
			return sum;
		}
	}

}

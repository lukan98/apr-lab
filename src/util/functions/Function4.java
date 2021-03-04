package util.functions;

import java.util.HashMap;

import util.matrices.Matrix;

public class Function4 extends Function {
	
	public Function4() {
		super.callCount = 0;
		super.memorizedValues = new HashMap<>();
	}

	@Override
	public double getValueAt(Matrix x) {
		if (super.memorizedValues.containsKey(x)) {
			return super.memorizedValues.get(x);
		} else {
			double result = Math.abs((x.getValue(0, 0) - x.getValue(1, 0)) * (x.getValue(0, 0) + x.getValue(1, 0))) + Math.sqrt(x.getValue(0, 0) * x.getValue(0, 0) + x.getValue(1, 0) * x.getValue(1, 0));
			super.callCount++;
			super.memorizedValues.put(x, result);
			return result;
		}
	}
	
}

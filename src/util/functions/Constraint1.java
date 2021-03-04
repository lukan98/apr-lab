package util.functions;

import util.matrices.Matrix;

public class Constraint1 extends Function {

	@Override
	public double getValueAt(Matrix x) {
		return 3 - x.getValue(0, 0) - x.getValue(1, 0);
	}

}

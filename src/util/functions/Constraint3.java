package util.functions;

import util.matrices.Matrix;

public class Constraint3 extends Function{

	@Override
	public double getValueAt(Matrix x) {
		return x.getValue(1, 0) - 1;
	}

}

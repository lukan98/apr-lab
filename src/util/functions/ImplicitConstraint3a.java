package util.functions;

import util.matrices.Matrix;

public class ImplicitConstraint3a extends Function {

	@Override
	public double getValueAt(Matrix x) {
		return x.getValue(1, 0) - x.getValue(0, 0);
	}

}

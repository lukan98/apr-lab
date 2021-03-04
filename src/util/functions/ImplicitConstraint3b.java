package util.functions;

import util.matrices.Matrix;

public class ImplicitConstraint3b extends Function {

	@Override
	public double getValueAt(Matrix x) {
		return 2-x.getValue(0, 0);
	}

}

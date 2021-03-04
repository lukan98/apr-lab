package util.functions;

import java.util.Arrays;

import util.matrices.Matrix;

public class Function2 extends DerivableFunction{
	

	@Override
	public double getValueAt(Matrix x) {
		if (super.memorizedValues.containsKey(x)) {
			return super.memorizedValues.get(x);
		}
		else {
			double result = Math.pow(x.getValue(0, 0)-4, 2) + 4*Math.pow(x.getValue(1, 0)-2, 2);
			super.callCount++;
			super.memorizedValues.put(x, result);
			return result;
		}
	}

	@Override
	public Matrix getDerivativeAt(Matrix x) {
		if (super.memorizedDerivatives.containsKey(x)) {
			return super.memorizedDerivatives.get(x);
		}
		else {
			Matrix derivative;
			double dx1 = 2*(x.getValue(0, 0) - 4);
			double dx2 = 8*(x.getValue(1, 0) - 2);
			
			derivative = new Matrix(Arrays.asList(dx1, dx2));
			super.derivativeCallCount++;
			super.memorizedDerivatives.put(x, derivative);
			return derivative;
		}
	}

	@Override
	public Matrix getHessianMatrix(Matrix x) {
		Matrix hessian = new Matrix(2, 2);
		
		hessian.setValue(0, 0, 2);
		hessian.setValue(0, 1, 0);
		hessian.setValue(1, 0, 0);
		hessian.setValue(1, 1, 8);
		
		return hessian;
	}
	
}

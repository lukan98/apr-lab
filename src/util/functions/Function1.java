package util.functions;

import java.util.Arrays;

import util.matrices.Matrix;


// Rosenbrockova banana funkcija
public class Function1 extends DerivableFunction{
	
	@Override
	public double getValueAt(Matrix x) {
		if (super.memorizedValues.containsKey(x)) {
			return super.memorizedValues.get(x);
		}
		else {
			double a = x.getValue(1, 0) - Math.pow(x.getValue(0, 0), 2);
			double result = 100*Math.pow(a, 2)+Math.pow(1-x.getValue(0, 0), 2);
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
			double dx1 = 400*Math.pow(x.getValue(0, 0), 3) + (2-400*x.getValue(1, 0))*x.getValue(0, 0) - 2;
			double dx2 = 200*(x.getValue(1, 0) - Math.pow(x.getValue(0, 0), 2));
			
			derivative = new Matrix(Arrays.asList(dx1, dx2));
			super.derivativeCallCount++;
			super.memorizedDerivatives.put(x, derivative);
			return derivative;
		}
	}

	@Override
	public Matrix getHessianMatrix(Matrix x) {
		Matrix hessian = new Matrix(2, 2);
		
		hessian.setValue(0, 0, 2 - 400*x.getValue(1, 0) + 1200*Math.pow(x.getValue(0, 0), 2));
		hessian.setValue(0, 1, -400*x.getValue(0, 0));
		hessian.setValue(1, 0, -400*x.getValue(0, 0));
		hessian.setValue(1, 1, 200);
		
		return hessian;
	}
	
}

package util.functions;

import java.util.HashMap;

import util.matrices.Matrix;

public abstract class DerivableFunction extends Function {
	
	protected int derivativeCallCount;
	protected HashMap<Matrix, Matrix> memorizedDerivatives;
	protected int hessianCalledCount;
	protected HashMap<Matrix, Matrix> memorizedHessians;
	
	public DerivableFunction() {
		super.callCount = 0;
		derivativeCallCount = 0;
		super.memorizedValues = new HashMap<>();
		memorizedDerivatives = new HashMap<>();
	}
	
	public abstract Matrix getDerivativeAt(Matrix x);
	
	@Override
	public void resetFunction() {
		super.resetFunction();
		this.derivativeCallCount = 0;
		this.memorizedDerivatives = new HashMap<>();
	}
	
	public abstract Matrix getHessianMatrix(Matrix x);
	
	public int getDerivativeCallCount() {
		return this.derivativeCallCount;
	}
}

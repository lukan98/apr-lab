package util.integration;

import java.util.ArrayList;

import util.matrices.Matrix;

public class PredictorCorrectorMethod implements Integrator {
	
	private Predictor predictor;
	private Corrector corrector;
	private int noOfIterations;
	private Integer outputStep;
	
	public PredictorCorrectorMethod(Predictor p, Corrector c, int noOfIterations) {
		this.predictor = p;
		this.corrector = c;
		this.noOfIterations = noOfIterations;
		this.outputStep = null;
	}
	
	public PredictorCorrectorMethod(Predictor p, Corrector c, int noOfIterations, int outputStep) {
		this.predictor = p;
		this.corrector = c;
		this.noOfIterations = noOfIterations;
		this.outputStep = outputStep;
	}

	@Override
	public ArrayList<Matrix> integrate(Matrix A, Matrix B, Matrix r, int timeDependency, Matrix initialValues, double start, double end, double step) {
		
		if (B==null)
			B = new Matrix(A.getNoOfRows(), A.getNoOfCols());
		if (r==null)
			r = new Matrix(initialValues.getNoOfRows(), 1);
		
		ArrayList<Matrix> calculatedValues = new ArrayList<>();
		calculatedValues.add(initialValues);

		double t = start;
		int iterationCounter = 0;
		
		while (t < end) {
			
			Matrix currentX = calculatedValues.get(calculatedValues.size()-1);
			if (this.outputStep!=null && iterationCounter%this.outputStep == 0) System.out.print(t+" "+currentX.transpose());
			t += step;
			
			Matrix nextX = predictor.getNext(A, B, r, currentX, timeDependency, t, step);
			
			for (int i=0; i<this.noOfIterations; i++) {
				nextX = corrector.getNext(A, B, r, currentX, nextX, timeDependency, t, step);
			}
			
			calculatedValues.add(nextX);
			iterationCounter++;
		}
		
		return calculatedValues;
	}

}

import java.util.ArrayList;

import util.integration.*;
import util.matrices.Matrix;

public class IntegrationDemo {

	public static void main(String[] args) {
		firstProblem();
//		secondProblem();
//		thirdProblem();
//		fourthProblem();

	}
	
	public static void firstProblem() {
		Integrator i1 = new EulerMethod(100);
		Integrator i2 = new BackwardEulerMethod(100);
		Integrator i3 = new TrapezoidalMethod(100);
		Integrator i4 = new RungeKuttaMethod(100);
		Integrator i5 = new PredictorCorrectorMethod(new EulerMethod(), new BackwardEulerMethod(), 2, 100);
		Integrator i6 = new PredictorCorrectorMethod(new EulerMethod(), new TrapezoidalMethod(), 1, 100);
		
		Matrix A1 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad1/A1.dat");
		Matrix x0_1 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad1/x0_1.dat");
		
		ArrayList<Matrix> results1 = i1.integrate(A1, null, null, 0, x0_1, 0, 10, 0.01);
		ArrayList<Matrix> results2 = i2.integrate(A1, null, null, 0, x0_1, 0, 10, 0.01);
		ArrayList<Matrix> results3 = i3.integrate(A1, null, null, 0, x0_1, 0, 10, 0.01);
		ArrayList<Matrix> results4 = i4.integrate(A1, null, null, 0, x0_1, 0, 10, 0.01);
		ArrayList<Matrix> results5 = i5.integrate(A1, null, null, 0, x0_1, 0, 10, 0.01);
		ArrayList<Matrix> results6 = i6.integrate(A1, null, null, 0, x0_1, 0, 10, 0.01);
		
		checkCumulativeError(results1, x0_1.getValue(0, 0), x0_1.getValue(1, 0), 0, 0.01);
		checkCumulativeError(results2, x0_1.getValue(0, 0), x0_1.getValue(1, 0), 0, 0.01);
		checkCumulativeError(results3, x0_1.getValue(0, 0), x0_1.getValue(1, 0), 0, 0.01);
		checkCumulativeError(results4, x0_1.getValue(0, 0), x0_1.getValue(1, 0), 0, 0.01);
		checkCumulativeError(results5, x0_1.getValue(0, 0), x0_1.getValue(1, 0), 0, 0.01);
		checkCumulativeError(results6, x0_1.getValue(0, 0), x0_1.getValue(1, 0), 0, 0.01);
		
	}
	
	public static void checkCumulativeError(ArrayList<Matrix> results, double x1, double x2, double start, double step) {
		double t = 0;
		double sumX1 = 0;
		double sumX2 = 0;
		
		for (Matrix result : results) {
			double realX1 = x1*Math.cos(t)+x2*Math.sin(t);
			double realX2 = x2*Math.cos(t)-x1*Math.sin(t);
			
			sumX1 += Math.abs(realX1 - result.getValue(0, 0));
			sumX2 += Math.abs(realX2 - result.getValue(1, 0));
			t += 0.01;
		}
		
		System.out.println("Greška za varijablu x1: "+sumX1);
		System.out.println("Greška za varijablu x2: "+sumX2);
	}
	
	public static void secondProblem() {
//		Integrator i = new EulerMethod(1);
//		Integrator i = new BackwardEulerMethod(1);
//		Integrator i = new TrapezoidalMethod(1);
//		Integrator i = new RungeKuttaMethod(1);
//		Integrator i = new PredictorCorrectorMethod(new EulerMethod(), new BackwardEulerMethod(), 2, 1);
		Integrator i = new PredictorCorrectorMethod(new EulerMethod(), new TrapezoidalMethod(), 1, 1);
		
		Matrix A2 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad2/A2.dat");
		Matrix x0_2 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad2/x0_2.dat");
		
		i.integrate(A2, null, null, 0, x0_2, 0, 1, 0.01);

		
	}
	
	public static void thirdProblem() {
//		Integrator i = new EulerMethod(1);
//		Integrator i = new BackwardEulerMethod(1);
//		Integrator i = new TrapezoidalMethod(1);
//		Integrator i = new RungeKuttaMethod(1);
//		Integrator i = new PredictorCorrectorMethod(new EulerMethod(), new BackwardEulerMethod(), 2, 1);
		Integrator i = new PredictorCorrectorMethod(new EulerMethod(), new TrapezoidalMethod(), 1, 1);
		
		Matrix A3 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad3/A3.dat");
		Matrix B3 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad3/B3.dat");
		Matrix r3 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad3/r3.dat");
		Matrix x0_3 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad3/x0_3.dat");
		
		i.integrate(A3, B3, r3, 0, x0_3, 0, 10, 0.01);
	}
	
	public static void fourthProblem() {
//		Integrator i = new EulerMethod(1);
//		Integrator i = new BackwardEulerMethod(1);
//		Integrator i = new TrapezoidalMethod(1);
//		Integrator i = new RungeKuttaMethod(1);
//		Integrator i = new PredictorCorrectorMethod(new EulerMethod(), new BackwardEulerMethod(), 2, 1);
		Integrator i = new PredictorCorrectorMethod(new EulerMethod(), new TrapezoidalMethod(), 1, 1);
		
		Matrix A4 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad4/A4.dat");
		Matrix B4 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad4/B4.dat");
		Matrix r4 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad4/r4.dat");
		Matrix x0_4 = new Matrix("/Users/lukanamacinski/FER-workspace/APR-workspace/lab-5/zad4/x0_4.dat");
		
		i.integrate(A4, B4, r4, 1, x0_4, 0, 1, 0.01);
	}

}

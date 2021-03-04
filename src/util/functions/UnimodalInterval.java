package util.functions;

import java.util.ArrayList;
import java.util.Arrays;

import util.matrices.Matrix;

public class UnimodalInterval {

	public static ArrayList<Double> calculate(double startingPoint, double h, Function f) {
		double left = startingPoint - h;
		double right = startingPoint + h;
		double m = startingPoint;
		int step = 1;
		
		double fm = f.getValueAt(new Matrix(Arrays.asList(m)));
		double fl = f.getValueAt(new Matrix(Arrays.asList(left)));
		double fr = f.getValueAt(new Matrix(Arrays.asList(right)));
		
		
		if(fm < fr && fm < fl) return new ArrayList<Double>(Arrays.asList(left, right));
		else if(fm > fr) {
			do {
				left = m;
				m = right;
				fm = fr;
				right = startingPoint + h * (step*=2);
				fr = f.getValueAt(new Matrix(Arrays.asList(right)));
			} while (fm > fr);
		} else {
			do {
				right = m;
				m = left;
				fm = fl;
				left = startingPoint - h * (step*=2);
				fl = f.getValueAt(new Matrix(Arrays.asList(left)));
			} while (fm > fl);
		}
		return new ArrayList<Double>(Arrays.asList(left, right));
	}
}

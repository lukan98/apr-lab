package util.optimizations;

import java.util.ArrayList;
import java.util.Arrays;

import util.functions.Function;
import util.functions.UnimodalInterval;
import util.matrices.Matrix;

public class GoldenSection {
	
	private double epsilon;
	private final static double k = 0.5*(Math.sqrt(5)-1);
	
	public GoldenSection(double precision) {
		this.epsilon = precision;
	}
	
	public ArrayList<Double> findMinimum(double startingPoint, Function f) {
		ArrayList<Double> interval = UnimodalInterval.calculate(startingPoint, 1, f);
		return findMinimum(interval.get(0), interval.get(1), f);
	}
	
	public ArrayList<Double> findMinimum(double a, double b, Function f) {
		double c = b - k*(b-a);
		double d = a + k*(b-a);
		
		double fc = f.getValueAt(new Matrix(Arrays.asList(c)));
		double fd = f.getValueAt(new Matrix(Arrays.asList(d)));
		
		while((b-a) > epsilon) {
			if (fc < fd) {
				b = d;
				d = c;
				c = b - k * (b - a);
				fd = fc;
				fc = f.getValueAt(new Matrix(Arrays.asList(c)));
			} else {
				a = c;
				c = d;
				d = a + k * (b - a);
				fc = fd;
				fd = f.getValueAt(new Matrix(Arrays.asList(d)));
			}
		}
		return new ArrayList<Double>(Arrays.asList(a, b));
	}
}

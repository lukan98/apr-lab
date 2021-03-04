package util.matrices;

public interface Decomposition {
	
	public static double epsilon = 1e-6;
	
	public Matrix decompose(Matrix m);
	
}

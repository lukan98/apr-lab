package util.matrices;

public class HelperFunctions {
	
	public static double dotProduct(double[] vectorA, double[] vectorB) {
		 double result = 0;
		 if (vectorA.length != vectorB.length) throw new IllegalArgumentException("Vektori moraju biti jednakih dimenzija za skalarni produkt!");
		 else {
			 for(int i=0; i<vectorA.length; i++) {
				 result += vectorA[i]*vectorB[i];
			 }
			 return result;
		 }
	}
	
}

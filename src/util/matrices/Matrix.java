package util.matrices;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.matrices.exceptions.MatrixCompatibilityException;
import util.matrices.exceptions.MatrixDefinitionException;

public class Matrix {
	
	private double values [][];

	public Matrix(int rows, int columns) {
		this.values = new double [rows][columns];
	}
	
	public Matrix(Matrix m) {
		this.values = new double [m.getNoOfRows()][m.getNoOfCols()];
		for (int i=0; i<m.getNoOfRows(); i++) {
			for (int j=0; j<m.getNoOfCols(); j++) {
				this.values[i][j] = m.getValue(i, j);
			}
		}
	}
	
	public Matrix(List<Double> values) {
		this.values = new double [values.size()][1];
		for (int i=0; i<values.size(); i++) {
			this.values[i][0] = values.get(i);
		}
	}
	
	public Matrix(String fileSource) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileSource))) {
			
			ArrayList<ArrayList<Double>> tempValues = new ArrayList<>();
			
			int rows, columns; // size initialization
			rows = columns = 0;
			
			String line;
			while ((line=br.readLine()) != null) {
				rows++;
				if (tempValues.isEmpty()) columns = line.split("\\s+").length;
				else if (line.split("\\s+").length != columns) throw new MatrixDefinitionException("Matrica mora biti zadana pravokutno!");
				tempValues.add(new ArrayList<>());
				for (String number : line.split("\\s+")) {
					tempValues.get(rows-1).add(Double.parseDouble(number));
				}
			}
			
			double[][] values =  tempValues.stream().map(element -> element.stream().mapToDouble(num->num).toArray()).toArray(double[][]::new);
			this.values = values;
			
			br.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("Nije moguće pronaći datoteku matrice!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Matrix unary(int dimension) {
		Matrix unary = new Matrix(dimension, dimension);
		
		for (int i=0; i<dimension; i++) {
			for (int j=0; j<dimension; j++) {
				if(i==j) unary.setValue(i, j, 1);
			}
		}
		
		return unary;
	}
	
	public double[][] getValues() {
		return values;
	}

	public double getValue(int row, int column) {
		return this.values[row][column];
	}
	
	public int getNoOfRows() {
		return this.values.length;
	}
	
	public int getNoOfCols() {
		return this.values[0].length;
	}
	
	public Matrix setValue(int row, int column, double value) {
		this.values[row][column]=value;
		return this;
	}
	
	public double getNorm() {
		if (this.getNoOfCols()!=1) throw new RuntimeException("Norma se računa samo za vektore!");
		
		double norm = 0;
		
		for (int i=0; i<this.getNoOfRows(); i++) {
			norm += Math.pow(this.values[i][0], 2);
		}
		
		norm = Math.sqrt(norm);
		return norm;
	}

	public Matrix add(double scalar) {
		Matrix result = new Matrix(this.getNoOfRows(), this.getNoOfCols());
		
		for (int row=0; row<this.getNoOfRows(); row++) {
			for (int col=0; col<this.getNoOfCols(); col++) {
				result.values[row][col]=this.getValue(row, col)+scalar;
			}
		}
		return result;
	}
	
	public Matrix add(Matrix m) {
		if (!checkAdditionCompatibility(this, m)) throw new MatrixCompatibilityException("Ove se matrice ne mogu zbrojiti zbog neodgovarajućih dimenzija!");
		
		Matrix result = new Matrix(this.getNoOfRows(), this.getNoOfCols());
		
		for (int row=0; row<this.getNoOfRows(); row++) {
			for (int col=0; col<this.getNoOfCols(); col++) {
				result.values[row][col]=this.getValue(row, col)+m.getValue(row, col);
			}
		}
		return result;
	}
	
	public Matrix subtract(double scalar) {
		Matrix result = new Matrix(this.getNoOfRows(), this.getNoOfCols());
		
		for (int row=0; row<this.getNoOfRows(); row++) {
			for (int col=0; col<this.getNoOfCols(); col++) {
				result.values[row][col]=this.getValue(row, col)-scalar;
			}
		}
		return result;
	}

	public Matrix subtract(Matrix m) {
		if (!checkAdditionCompatibility(this, m)) throw new MatrixCompatibilityException("Ove se matrice ne mogu oduzeti zbog neodgovarajućih dimenzija!");
		
		Matrix result = new Matrix(this.getNoOfRows(), this.getNoOfCols());
		
		for (int row=0; row<this.getNoOfRows(); row++) {
			for (int col=0; col<this.getNoOfCols(); col++) {
				result.values[row][col]=this.getValue(row, col)-m.getValue(row, col);
			}
		}
		return result;
	}
	
	public Matrix multiply(double scalar) {
		Matrix result = new Matrix(this);
		
		for (int row=0; row<this.getNoOfRows(); row++) {
			for (int col=0; col<this.getNoOfCols(); col++) {
				result.values[row][col]*=scalar;
			}
		}
		return result;
	}
	
	public Matrix multiply(Matrix m) {
		if (!checkMultiplicationCompatibility(this, m)) throw new MatrixCompatibilityException("Ove se matrice ne mogu množiti zbog neodgovarajućih dimenzija!");
		
		int x = this.getNoOfRows();
		int y = this.getNoOfCols();
		int z = m.getNoOfCols();
		
		Matrix result = new Matrix(x, z);
		
		for (int i=0; i<x; i++) {
			for (int j=0; j<z; j++) {
				double sum = 0;
				for (int k=0; k<y; k++) {
					sum += this.values[i][k]*m.values[k][j];
				}
				result.values[i][j] = sum;
			}
		}
		
		return result;
	}
	
	public Matrix transpose() {
		Matrix result = new Matrix(this.getNoOfCols(), this.getNoOfRows());
		
		for (int i=0; i<this.getNoOfRows(); i++) {
			for (int j=0; j<this.getNoOfCols(); j++) {
				result.values[j][i]=this.values[i][j];
			}
		}
		
		return result;
	}
	
	public void swapRows(int indexRow1, int indexRow2) {
		if (indexRow1<0 || indexRow2<0 || indexRow1>=this.getNoOfRows() || indexRow2>=this.getNoOfRows()) throw new IllegalArgumentException("Matrica nema naznačene redove");
		
		double tempValue;
		
		for (int i=0; i<this.getNoOfCols(); i++) {
			tempValue = this.values[indexRow1][i];
			this.values[indexRow1][i] = this.values[indexRow2][i];
			this.values[indexRow2][i] = tempValue;
		}
		
	}

	public static boolean checkAdditionCompatibility(Matrix a, Matrix b) {
		return a.getNoOfRows() == b.getNoOfRows() && a.getNoOfCols() == b.getNoOfCols();
	}
	
	public static boolean checkMultiplicationCompatibility(Matrix a, Matrix b) {
		return a.getNoOfCols() == b.getNoOfRows();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (double[] row : this.values) {
			for (double value : row) {
				sb.append(value+" ");
			}
			sb.deleteCharAt(sb.lastIndexOf(" "));
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(values);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix other = (Matrix) obj;
		if (!Arrays.deepEquals(values, other.values))
			return false;
		return true;
	}

}

package hdh;

public class OtokePivot extends Thread {
	int p;
	int numberOfEquations;
	double[][] matrix;
	int columns;

	public OtokePivot(int p, int numberOfEquations, double[][] matrix, int columns) {
		super();
		this.p = p;
		this.numberOfEquations = numberOfEquations;
		this.matrix = matrix;
		this.columns = columns;
	}
	
	@Override
	public void run() {
		for (int i = p + 1; i < numberOfEquations; i++){
            double alpha = matrix[i][p] / matrix[p][p];
            new otokePivot2(i, p, columns, matrix, alpha).start();
        }
	}
}

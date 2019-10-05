package hdh;

public class otokePivot2 extends Thread{
	int i;
	int p;
	int columns;
	double[][] matrix;
	double alpha;
	
	public otokePivot2(int i, int p, int columns, double[][] matrix, double alpha) {
		super();
		this.i = i;
		this.p = p;
		this.columns = columns;
		this.matrix = matrix;
		this.alpha = alpha;
	}
	
	@Override
	public void run() {
		for ( int j = p; j < columns; j++){
            matrix[i][j] -= alpha * matrix[p][j];
        }
	}
}

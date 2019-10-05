package hdh;

import java.text.DecimalFormat;
import java.util.Scanner;

public class GaussianElimination {
    private static final int MIN_ROW = 3;
    private static final int MAX_COL = 7;

    private static final int CODE_NO_SOLUTION = -2;
    private static final int CODE_INFINITE = -1;
    private static final int CODE_OK = 0;

    private double[] result;
    private double[][] matrix;
    private final int countVar;
    private static final double EPSILON = 1e-8;

    public GaussianElimination(Scanner inputFile) {
        countVar = Integer.parseInt((inputFile.nextLine()));

        matrix = new double[countVar][countVar + 1];
        for (int row = 0; row < countVar; ++row) {

            for (int col = 0; col <= countVar; ++col) {
                matrix[row][col] = inputFile.nextDouble();
            }
        }

        this.forwardElimination();

        this.resolve();
    }

    public int getCountVar() {
        return countVar;
    }


    // dua ma tran ve dang tam giac,
    private void forwardElimination() {
        for (int index = 0; index < countVar; ++index) {

            int max = index;
            for (int i = index + 1; i < countVar; ++i) {
                if (Math.abs(matrix[i][index]) > Math.abs(matrix[max][index])) {
                    max = i;
                }
            }

            this.swapRow(index, max);

            // neu he so = 0 thi chuyen qua cot tiep theo
            if (Math.abs(matrix[index][index]) <= EPSILON) {
                continue;
            }

            this.pivot(index);

        }
    }

    private void swapRow(int row1, int row2) {
        double[] temp = matrix[row1];
        matrix[row1] = matrix[row2];
        matrix[row2] = temp;
    }

    private void pivot(int p) {
        for (int row = p + 1; row < countVar; ++row) {
            double alpha = matrix[row][p] / matrix[p][p];
            for (int col = p; col <= countVar; ++col) {
                matrix[row][col] -= alpha * matrix[p][col];
            }
        }
    }

    private int replaceWithNumber() {
        result = new double[countVar];
        for (int row = countVar - 1; row >= 0; --row) {
            double sumOther = 0.0;
            for (int col = row + 1; col < countVar; ++col) {
                sumOther += matrix[row][col] * result[col];
            }

            if (Math.abs(matrix[row][row]) > EPSILON) {
                result[row] = (matrix[row][countVar] - sumOther) / matrix[row][row];
            } else if (Math.abs(matrix[row][countVar] - sumOther) > EPSILON) {
                return CODE_NO_SOLUTION;
            } else {
                return CODE_INFINITE;
            }
        }

        return CODE_OK;
    }

    private String generateSubscript(int i) {
        StringBuilder sb = new StringBuilder();
        for (char ch : String.valueOf(i).toCharArray()) {
            sb.append((char) ('\u2080' + (ch - '0')));
        }
        return sb.toString();
    }

    private void resolve() {
        int statusCOde = replaceWithNumber();
        if (statusCOde == CODE_NO_SOLUTION) {
            System.out.println("\nVo nghiem");
            return;
        }
        if (statusCOde == CODE_INFINITE) {
            System.out.println("\nVo so nghiem");
            return;
        }

        DecimalFormat df = new DecimalFormat("#.00");
        int lengthBreak = Math.min(Math.max(countVar / MIN_ROW, MIN_ROW), MAX_COL);
        for (int i = 0; i < countVar; ++i) {
            System.out.print("X" + this.generateSubscript(i) + " = " + df.format(result[i]));
            if ((i + 1) % lengthBreak == 0) {
                System.out.println("\n");
            } else {
                System.out.print("\t");
            }
        }
        System.out.println("\n");
    }
}

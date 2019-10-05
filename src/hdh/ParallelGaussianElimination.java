package hdh;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import test.SequentialGE;

public class ParallelGaussianElimination extends JFrame implements ActionListener {
	JTextField txtfilePath, txtTimeTT, txtTimeSS;
	JButton btnNhap, btnXulySS, btnXuLyTT, btnchoosefile;
	JFileChooser fileChooser;
	JScrollPane scroll, scrollData;
	static JTextArea txtResult;
	static JTextArea txtData;
	JLabel latimeSS, latimeTT;
	JFrame f;
	String path;
	private static double[] x;
	private static double[][] matrix;
	private static int numberOfEquation;
	private static final double EPSILON = 1e-8;

	private static final int MIN_ROW = 3;
	private static final int MAX_COL = 7;

	private static final int CODE_NO_SOLUTION = -2;
	private static final int CODE_INFINITE = -1;
	private static final int CODE_OK = 0;


	public ParallelGaussianElimination() {
		f = new JFrame("Đồ án hệ điều hành");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(550, 480);
		f.setLayout(null);

		txtfilePath = new JTextField("File path");
		txtfilePath.setBounds(5, 5, 400, 30);
		f.add(txtfilePath);

		fileChooser = new JFileChooser();
		btnchoosefile = new JButton("Select File");
		btnchoosefile.setBounds(420, 5, 100, 30);
		btnchoosefile.addActionListener(this);
		f.add(btnchoosefile);

//		btnXulySS = new JButton("Parallel");
//		btnXulySS.setBounds(255, 80, 100, 30);
//		btnXulySS.addActionListener(this);
//		f.add(btnXulySS);

		btnXuLyTT = new JButton("Do");
		btnXuLyTT.setBounds(365, 80, 100, 30);
		btnXuLyTT.addActionListener(this);
		f.add(btnXuLyTT);

		txtResult = new JTextArea("Result");
		txtResult.setEditable(false);

		scroll = new JScrollPane(txtResult);
		scroll.setBounds(5, 80, 200, 350);
		f.getContentPane().add(scroll);

//		latimeSS = new JLabel("Parallel time");
//		latimeSS.setBounds(255, 130, 100, 30);
//		f.add(latimeSS);

		latimeTT = new JLabel("Sequential time");
		latimeTT.setBounds(255, 180, 100, 30);
		f.add(latimeTT);

//		txtTimeSS = new JTextField();
//		txtTimeSS.setHorizontalAlignment(SwingConstants.RIGHT);
//		txtTimeSS.setBounds(365, 130, 155, 30);
//		f.add(txtTimeSS);

		txtTimeTT = new JTextField();
		txtTimeTT.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTimeTT.setBounds(365, 180, 155, 30);
		f.add(txtTimeTT);

		txtData = new JTextArea();
		txtData.setEditable(false);

		scrollData = new JScrollPane(txtData);
		scrollData.setBounds(255, 230, 265, 200);
		f.getContentPane().add(scrollData);
		f.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (btnchoosefile == e.getSource()) {
			int check = fileChooser.showOpenDialog(f);
			if (check == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				path = file.getPath();
				txtfilePath.setText(path);
			} else {
				txtfilePath.setText("Error");
			}
		}
		if (btnXulySS == e.getSource()) {
			if (txtfilePath.getText().equals("File path") || txtfilePath.getText().equals("Error")) {
				JOptionPane.showMessageDialog(null, "Please select a file!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				txtResult.setText("");
				try {
					getFile(path);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				txtResult.setText("");

				long start = System.currentTimeMillis();

				forwardElimination();

				resolve();
				
			}

		}
		if (btnXuLyTT == e.getSource()) {
			if (txtfilePath.getText().equals("File path") || txtfilePath.getText().equals("Error")) {
				JOptionPane.showMessageDialog(null, "Please select a file!", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				
				
				txtResult.setText("");

				long start = System.currentTimeMillis();
				try {
					getFile(path);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				forwardElimination();

				resolve();
				long stop = System.currentTimeMillis() - start;
				txtTimeTT.setText(stop + " millis");
			}
		}

	}

	private static void getFile(String filepath) throws FileNotFoundException {
		File file = new File(filepath);
		Scanner inFile = new Scanner(file);

		numberOfEquation = Integer.parseInt((inFile.nextLine()));
//		txtData.setText("" + countVar + "\n");
		matrix = new double[numberOfEquation][numberOfEquation + 1];
		for (int row = 0; row < numberOfEquation; ++row) {

			for (int col = 0; col <= numberOfEquation; ++col) {
				matrix[row][col] = inFile.nextDouble();
//				txtData.setText(txtData.getText() + matrix[row][col]+"\t");
			}
//			txtData.setText(txtData.getText()+"\n");
		}

	}

	// dua ma tran ve dang tam giac,
	private static void forwardElimination() {
		for (int p = 0; p < numberOfEquation; ++p) {

			int max = p;
			for (int i = p + 1; i < numberOfEquation; ++i) {
				if (Math.abs(matrix[i][p]) > Math.abs(matrix[max][p])) {
					max = i;
				}
			}

			swap(p, max);

			// neu he so = 0 thi chuyen qua cot tiep theo
			if (Math.abs(matrix[p][p]) <= EPSILON) {
				continue;
			}

			pivot(p);

		}
	}

	private static void swap(int row1, int row2) {
		double[] temp = matrix[row1];
		matrix[row1] = matrix[row2];
		matrix[row2] = temp;
	}

	private static void pivot(int p) {
		for (int i = p + 1; i < numberOfEquation; ++i) {
			double alpha = matrix[i][p] / matrix[p][p];
			for (int col = p; col <= numberOfEquation; ++col) {
				matrix[i][col] -= alpha * matrix[p][col];
			}
		}
	}

	private static void resolve() {
		int statusCOde = replaceWithNumber();
		if (statusCOde == CODE_NO_SOLUTION) {
//			System.out.println("\nVo nghiem");
			txtResult.setText("Vô nghiệm");
			return;
		}
		if (statusCOde == CODE_INFINITE) {
//			System.out.println("\nVo so nghiem");
			txtResult.setText("Vô số nghiệm");
			return;
		}

		DecimalFormat df = new DecimalFormat("#.0");
		int lengthBreak = Math.min(Math.max(numberOfEquation / MIN_ROW, MIN_ROW), MAX_COL);
		for (int i = 0; i < numberOfEquation; ++i) {
			txtResult.setText(txtResult.getText() + "X" + generateSubscript(i) + " = " + df.format(x[i]) + "\n");
//			if ((i + 1) % lengthBreak == 0) {
//				System.out.println("\n");
//			} else {
//				System.out.print("\t");
//			}
		}
//		System.out.println("\n");
	}

	private static int replaceWithNumber() {
		x = new double[numberOfEquation];
		for (int row = numberOfEquation - 1; row >= 0; --row) {
			double sumOther = 0.0;
			for (int col = row + 1; col < numberOfEquation; ++col) {
				sumOther += matrix[row][col] * x[col];
			}

			if (Math.abs(matrix[row][row]) > EPSILON) {
				x[row] = (matrix[row][numberOfEquation] - sumOther) / matrix[row][row];
			} else if (Math.abs(matrix[row][numberOfEquation] - sumOther) > EPSILON) {
				return CODE_NO_SOLUTION;
			} else {
				return CODE_INFINITE;
			}
		}

		return CODE_OK;
	}

	private static String generateSubscript(int i) {
		StringBuilder sb = new StringBuilder();
		for (char ch : String.valueOf(i).toCharArray()) {
			sb.append((char) ('\u2080' + (ch - '0')));
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		new ParallelGaussianElimination();

	}

}

package test;


import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.print("Enter the filename: ");
        Scanner scan = new Scanner(System.in);
        String userInput = scan.nextLine();
        try {
            Main.getFile(userInput);
        } catch (FileNotFoundException e) {
            System.out.println("\nFile not found: " + e.getLocalizedMessage());
        }
    }

    private static void getFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner inputFile = new Scanner(file);

        new SequentialGE(inputFile);
    }
}

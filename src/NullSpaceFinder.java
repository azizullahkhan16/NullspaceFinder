import java.util.InputMismatchException;
import java.util.Scanner;

public class NullSpaceFinder {

    public static void GetNullSpace() {
        int rows = 0; // Variable to store the number of rows
        int columns = 0; // Variable to store the number of columns
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the number of rows until a valid input is provided
        while (rows <= 0) {
            try {
                System.out.print("Enter the number of rows: ");
                rows = scanner.nextInt();

                if (rows <= 0) {
                    throw new IllegalArgumentException("Number of rows must be positive.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter an integer value.");
                scanner.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Prompt the user to enter the number of columns until a valid input is provided
        while (columns <= 0) {
            try {
                System.out.print("Enter the number of columns: ");
                columns = scanner.nextInt();

                if (columns <= 0) {
                    throw new IllegalArgumentException("Number of columns must be positive.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter an integer value.");
                scanner.nextLine();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        double[][] matrix = new double[rows][columns]; // Create a 2D array for the matrix

        Print2DArray(matrix); // Print the initial matrix

        boolean isFilled;

        // Loop through each element of the matrix and fill it with user input
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                isFilled = false; // Flag to indicate if the element is filled

                // Prompt the user to fill the current position until a valid input is provided
                while (!isFilled) {
                    try {
                        System.out.print("Fill the (R" + (i + 1) + ", C" + (j + 1) + ") position: ");
                        matrix[i][j] = scanner.nextDouble();
                        isFilled = true;
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a numeric value.");
                        scanner.nextLine();
                    }
                }

                Print2DArray(matrix); // Print the updated matrix
            }
        }

        Print2DArray(RowEchelonForm(matrix));
        System.out.println("_____________________________________");

        Print2DArray(FindBasisVectors(RowEchelonForm(matrix)));

    }


    private static void Print2DArray(double[][] matrix) {
        // Print the column headers
        for (int i = 0; i < matrix[0].length; i++) {
            if (i == 0) {
                System.out.print("       C" + (i + 1));
            } else {
                if (i < 9) {
                    System.out.print("       C" + (i + 1));
                } else {
                    System.out.print("      C" + (i + 1));
                }
            }
        }

        System.out.println();

        // Print the matrix elements
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (j == 0) {
                    if (i < 9) {
                        System.out.print("R" + (i + 1) + "    " + matrix[i][j] + "      ");
                    } else {
                        System.out.print("R" + (i + 1) + "   " + matrix[i][j] + "      ");
                    }
                } else {
                    System.out.print(matrix[i][j] + "      ");
                }
            }
            System.out.println();
        }

        System.out.println();
    }

    private static double[][] RowEchelonForm(double[][] matrix){


        for (int j = 0; j < matrix[0].length-1; j++) {
            for (int i = j+1; i < matrix.length; i++) {
                if (matrix[i][j] != 0){
                    RowOperation(matrix, i, j);
                }
            }
            for (int i = j-1; i >= 0 && matrix[j][j] != 0; i--) {
                if (matrix[i][j] != 0){
                    RowOperation(matrix, i, j);
                }
            }

        }

        return matrix;
    }

    private static int NumOfBasisVectors(double[][] matrix){
        int numBases = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = i+numBases; j < matrix[i].length; j++) {
                if(matrix[i][j] != 0) break;
                else numBases++;
            }
        }

        return numBases;
    }

    private static double[][] FindBasisVectors(double[][] matrix){
        double[][] basisVectors = new double[NumOfBasisVectors(matrix)][matrix[0].length];
        char[] identity = new char[matrix[0].length];
        int numBases = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = i+numBases; j < matrix[i].length; j++) {
                if(matrix[i][j] != 0){
                    identity[j] = 'B';
                    break;
                }else {
                    identity[j] = 'F';
                    numBases++;
                }
            }
        }

        int row = 0;
        int column = 0;

        for (int i = 0; i < matrix[0].length; i++) {
            if (identity[i] == 'F'){
                column = 0;
                for (int j = 0; j < matrix.length; j++) {
                    if (matrix[j][i] != 0) {
                        basisVectors[row][column] = -matrix[j][i]/FindBasicElementInRow(matrix, j);
                        column++;
                    }
                }
                row++;
            }
        }

        int i = 0;
        for (int j = 0; j < basisVectors[0].length; j++) {
            if(identity[j] == 'F'){
                basisVectors[i][j] = 1;
                i++;
            }
        }


        return basisVectors;

    }

    private static double FindBasicElementInRow(double[][] matrix, int row){

        for (int i = 0; i < matrix[row].length; i++) {
            if (matrix[row][i] != 0) return matrix[row][i];
        }

        return 0;
    }

    private static double[][] RowOperation(double[][] matrix, int row, int column){
        double a = matrix[row][column];
        double b = matrix[column][column];

        for (int i = 0; i < matrix[row].length; i++) {
            matrix[row][i] = matrix[row][i] - (a*matrix[column][i])/b;
        }


        return matrix;
    }
}

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

//        double[][] matrix = new double[][]{
//                {2, 2, -4, 3, -9, 1},
//                {1, 1, -2, -3, 0, 2},
//                {3, 3, -6, -3, -6, 2}
//        };

//        double[][] matrix = new double[][]{
//                {1, 1, 0, 0, 1},
//                {0, 0, 1, -2, 0},
//                {4, 2, 0, 0, 3},
//                {1, 1, 1, -2, 1},
//                {2, 2, 0, 0, 2},
//                {1, 1, 2, -4, 1}
//        };

//        double[][] matrix = new double[][]{
//                {3, 0, 3, 3, 3},
//                {-3, 1, -2, -4, -1},
//                {5, 4, 9, 1, 13},
//                {7, 6, 13, 1, 19}
//        };

        Print2DArray(ReducedRowEchelonForm(matrix));

        System.out.println("________________________________");

        Print2DArray(FindBasisVectors(ReducedRowEchelonForm(matrix)));



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

    private static double[][] ReducedRowEchelonForm(double[][] matrix){
        int rowLength = 0;
        if(matrix[0].length < matrix.length) rowLength = matrix[0].length;
        else rowLength = matrix.length;

        for (int j = 0; j < rowLength; j++) {
            if(matrix[j][j] == 0) RowOperationType1(matrix, j, j);
            if(matrix[j][j] != 1 && matrix[j][j] != 0) RowOperationType2(matrix, j, j);
            if(matrix[j][j] == 0){
                int a = FindBasicElementInRow(matrix, j);
                if(matrix[j][a] != 1 && matrix[j][a] != 0) RowOperationType2(matrix, j, a);
                for (int i = j+1; i < matrix.length; i++) {
                    if (matrix[i][a] != 0){
                        RowOperationType3(matrix, i, j, a);
                    }
                }
                for (int i = j-1; i >= 0 && matrix[j][a] != 0; i--) {
                    if (matrix[i][a] != 0){
                        RowOperationType3(matrix, i, j, a);
                    }
                }
            }else {
                for (int i = j + 1; i < matrix.length; i++) {
                    if (matrix[i][j] != 0) {
                        RowOperationType3(matrix, i, j, j);
                    }
                }
                for (int i = j - 1; i >= 0 && matrix[j][j] != 0; i--) {
                    if (matrix[i][j] != 0) {
                        RowOperationType3(matrix, i, j, j);
                    }
                }
            }

        }

        return matrix;
    }

    private static double[][] RowOperationType2(double[][] matrix, int row, int column){
        double a = matrix[row][column];

        for (int i = 0; i < matrix[0].length; i++) {
            matrix[row][i] = matrix[row][i] / a;
        }

        return matrix;
    }

    private static double[][] RowOperationType1(double[][] matrix, int row, int column){
        int exchangeRow = -1;

        for (int i = row+1; i < matrix.length; i++) {
            if(matrix[i][column] != 0){
                exchangeRow = i;
                break;
            }
        }

        if(exchangeRow != -1) {
            double[] tempRow = new double[matrix[0].length];

            for (int i = 0; i < matrix[0].length; i++) {
                tempRow[i] = matrix[row][i];
            }

            for (int i = 0; i < matrix[0].length; i++) {
                matrix[row][i] = matrix[exchangeRow][i];
            }

            for (int i = 0; i < matrix[0].length; i++) {
                matrix[exchangeRow][i] = tempRow[i];
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

    private static char[] MarkFreeAndBasicVariable(char[] identity, double[][] matrix){
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

        return identity;
    }

    private static double[][] FindBasisVectors(double[][] matrix){
        double[][] basisVectors = new double[NumOfBasisVectors(matrix)][matrix[0].length];
        char[] identity = new char[matrix[0].length];

        MarkFreeAndBasicVariable(identity, matrix);

        int row = 0;
        int column;

        for (int i = 0; i < identity.length; i++) {
            if (identity[i] == 'F') {
                column = 0;
                if (basisVectors[0].length < matrix.length) {
                    for (int j = 0; j < basisVectors[0].length; j++) {
                        for (int k = j; k < identity.length; k++) {
                            if(identity[k] != 'B'){
                                column++;
                            }else break;
                        }
                        if(matrix[j][i] != 0) {
                            basisVectors[row][column] = -matrix[j][i];
                            if (basisVectors[row][column] == -0) basisVectors[row][column] = 0;
                            column++;
                        }
                    }
                } else {
                    for (int j = 0; j < matrix.length; j++) {
                        for (int k = j; k < identity.length; k++) {
                            if(identity[k] != 'B'){
                                column++;
                            }else break;
                        }
                        if(matrix[j][i] != 0) {
                            basisVectors[row][column] = -matrix[j][i];
                            if (basisVectors[row][column] == -0) basisVectors[row][column] = 0;
                            column++;
                        }
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

    private static int FindBasicElementInRow(double[][] matrix, int row){

        for (int i = 0; i < matrix[row].length; i++) {
            if (matrix[row][i] != 0) return i;
        }

        return 0;
    }

    private static double[][] RowOperationType3(double[][] matrix, int row1, int row2, int column){
        double a = matrix[row1][column];

        for (int i = 0; i < matrix[row1].length; i++) {
            matrix[row1][i] = matrix[row1][i] - (a*matrix[row2][i]);
        }


        return matrix;
    }
}

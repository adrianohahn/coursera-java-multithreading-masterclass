package sh.hahn.multithreading.module7.lecture3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final int SIZE = 2000;
    private static final int[][] matrixA = new int[SIZE][SIZE];
    private static final int[][] matrixB = new int[SIZE][SIZE];
    private static final int[][] matrixC = new int[SIZE][SIZE];

    public static void main(String[] args) {
        long start, end;

        initializeMatrices();
        start = System.nanoTime();
        multiplyMatricesParallel();
        end = System.nanoTime();
        System.out.printf("Time taken for parallel processing: %.2f seconds%n", (end - start) / 1e9);
        checkResults();

        initializeMatrices();
        start = System.nanoTime();
        multiplyMatricesSerial();
        end = System.nanoTime();
        System.out.printf("Time taken for serial processing: %.2f seconds%n", (end - start) / 1e9);
        checkResults();
    }

    static void initializeMatrices() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrixA[i][j] = 1;
                matrixB[i][j] = 1;
                matrixC[i][j] = 0;
            }
        }
    }

    static void multiplyMatricesSerial() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    matrixC[i][j] = matrixC[i][j] + matrixA[i][k] * matrixB[k][j];
                }
            }
        }
    }

    static void multiplyMatricesParallel() {
        try (ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    executorService.submit(new ParallelMatrixMultiplicationTask(i, j));
                }
            }
        }
    }

    static class ParallelMatrixMultiplicationTask implements Runnable {

        private final int row;
        private final int col;

        public ParallelMatrixMultiplicationTask(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void run() {
            for (int k = 0; k < SIZE; k++) {
                matrixC[row][col] += matrixA[row][k] * matrixB[k][col];
            }
        }
    }

    static void checkResults() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (matrixC[i][j] != SIZE) {
                    System.out.printf("Error at position (%d, %d)\n", i, j);
                    return;
                }
            }
        }
        System.out.println("All good!");
    }

    /**
     * Print the matrix to the console.
     *
     * @param matrix the matrix to print
     * @param title  the title to print before the matrix
     */
    @SuppressWarnings("unused")
    static void printMatrix(int[][] matrix, String title) {
        System.out.printf("%s:%n", title);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.printf("%2d ", matrix[i][j]);
            }
            System.out.println();
        }
    }

}

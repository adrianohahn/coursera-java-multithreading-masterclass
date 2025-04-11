package sh.hahn.multithreading.module7.lecture4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Main {

    private static final int SIZE = 2000;
    private static final int BLOCK_SIZE = SIZE / 2;
    private static final int[][] matrixA = new int[SIZE][SIZE];
    private static final int[][] matrixB = new int[SIZE][SIZE];
    private static final int[][] matrixC = new int[SIZE][SIZE];
    private static final int[][][] partialResults =
            new int[Runtime.getRuntime().availableProcessors()][BLOCK_SIZE][BLOCK_SIZE];

    // 1. How many types do we need to have? -> 2 tasks -> a multiply task, and a sum task
    // 2. How can we identify the blocks that we need to multiply?
    // 3. How can we synchronize the multiplication and sum tasks?
    // 4. How can we identify the place where the block should be written in the final matrix?
    // 5. The block size is equal to N / 2;

    public static void main(String[] args) {
        long start, end;

        initializeMatrices();
        start = System.nanoTime();
        multiplyMatricesSerial();
        end = System.nanoTime();
        System.out.printf("Time taken for serial processing: %.2f seconds%n", (end - start) / 1e9);
        checkResults();

        initializeMatrices();
        start = System.nanoTime();
        multiplyBlockParallel();
        end = System.nanoTime();
        System.out.printf("Time taken for parallel processing: %.2f seconds%n", (end - start) / 1e9);
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

    static void multiplyBlockParallel() {
        try (ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            // Upper-left block
            Semaphore upperLeftSemaphore = new Semaphore(0);
            executorService.submit(new MultiplyTask(0, 0, 0, partialResults[0], upperLeftSemaphore));
            executorService.submit(new MultiplyTask(0, 0, SIZE / 2, partialResults[1], upperLeftSemaphore));
            executorService.submit(new SumTask(0, 0, partialResults[0], partialResults[1], upperLeftSemaphore));

            // Upper-right block
            Semaphore upperRightSemaphore = new Semaphore(0);
            executorService.submit(new MultiplyTask(0, SIZE / 2, 0, partialResults[2], upperRightSemaphore));
            executorService.submit(new MultiplyTask(0, SIZE / 2, SIZE / 2, partialResults[3], upperRightSemaphore));
            executorService.submit(new SumTask(0, SIZE / 2, partialResults[2], partialResults[3], upperRightSemaphore));

            // Lower-left block
            Semaphore lowerLeftSemaphore = new Semaphore(0);
            executorService.submit(new MultiplyTask(SIZE / 2, 0, 0, partialResults[4], lowerLeftSemaphore));
            executorService.submit(new MultiplyTask(SIZE / 2, 0, SIZE / 2, partialResults[5], lowerLeftSemaphore));
            executorService.submit(new SumTask(SIZE / 2, 0, partialResults[4], partialResults[5], lowerLeftSemaphore));

            // Lower-right block
            Semaphore lowerRightSemaphore = new Semaphore(0);
            executorService.submit(new MultiplyTask(0, SIZE / 2, SIZE / 2, partialResults[6], lowerRightSemaphore));
            executorService.submit(new MultiplyTask(0, SIZE / 2, SIZE / 2, partialResults[7], lowerRightSemaphore));
            executorService.submit(new SumTask(SIZE / 2, SIZE / 2, partialResults[6], partialResults[7], lowerRightSemaphore));
        }

    }

    static void multiplyMatricesSerial() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrixC[i][j] = 0;
                for (int k = 0; k < SIZE; k++) {
                    matrixC[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
    }

    static class MultiplyTask implements Runnable {


        private final int lineStart;
        private final int colStart;
        private final int blockStart;
        private final int[][] result;
        private final Semaphore semaphore;

        MultiplyTask(int lineStart, int colStart, int blockStart, int[][] result, Semaphore semaphore) {
            this.lineStart = lineStart;
            this.colStart = colStart;
            this.blockStart = blockStart;
            this.result = result;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            for (int i = lineStart, x = 0; i < lineStart + BLOCK_SIZE; i++, x++) {
                for (int j = colStart, y = 0; j < colStart + BLOCK_SIZE; j++, y++) {
                    result[x][y] = 0;
                    for (int k = blockStart; k < blockStart + BLOCK_SIZE; k++) {
                        result[x][y] += matrixA[i][k] * matrixB[k][j];
                    }
                }
            }
            semaphore.release();
        }
    }

    static class SumTask implements Runnable {

        private final int lineStart;
        private final int colStart;
        private final int[][] firstResult;
        private final int[][] secondResult;
        private final Semaphore semaphore;

        public SumTask(int lineStart, int colStart, int[][] firstResult, int[][] secondResult, Semaphore semaphore) {
            this.lineStart = lineStart;
            this.colStart = colStart;
            this.firstResult = firstResult;
            this.secondResult = secondResult;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                semaphore.acquire(2);
            } catch (InterruptedException e) {
                System.out.printf("SumTask interrupted (%s)\n", e.getMessage());
            }

            for (int i = lineStart, x = 0; i < lineStart + BLOCK_SIZE; i++, x++) {
                for (int j = colStart, y = 0; j < colStart + BLOCK_SIZE; j++, y++) {
                    matrixC[i][j] = firstResult[x][y] + secondResult[x][y];
                }
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

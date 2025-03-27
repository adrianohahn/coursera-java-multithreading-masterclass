package sh.hahn.multithreading.module7.lecture1;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Main {

    private static final int ARRAY_SIZE = 100_000_000;
    private static final int PARALLELISM = 8;


    public static void main(String[] args) {
        long start, end, timeRecursive, timeForkJoin;
        final int[] array = createRandomArray();
        final int[] forkJoinArray = Arrays.copyOf(array, array.length);

        start = System.nanoTime();
        quickSort(array, 0, array.length - 1);
        end = System.nanoTime();
        timeRecursive = end - start;
        System.out.printf("Sorted array in %.3f milliseconds%n", timeRecursive / 1_000_000.0);
//        System.out.printf("Array: %s\n", Arrays.toString(array));

        try (ForkJoinPool forkJoinPool = new ForkJoinPool(PARALLELISM)) {
            start = System.nanoTime();
            forkJoinPool.invoke(new QuickSortTask(forkJoinArray, 0, forkJoinArray.length - 1));
            end = System.nanoTime();
            timeForkJoin = end - start;
            System.out.printf("Sorted fork/join array in %.3f milliseconds%n", timeForkJoin / 1_000_000.0);
//            System.out.printf("Array: %s\n", Arrays.toString(forkJoinArray));
        }

        System.out.printf("Fork/join is %.2f times faster than recursive%n", (double) timeRecursive / timeForkJoin);
    }

    static class QuickSortTask extends RecursiveAction {
        private final int[] array;
        private final int left;
        private final int right;

        public QuickSortTask(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (left < right) {
                int pivotIndex = partition(array, left, right);
                invokeAll(new QuickSortTask(array, left, pivotIndex - 1),
                        new QuickSortTask(array, pivotIndex + 1, right));
            }
        }
    }

    private static int[] createRandomArray() {
        Random random = new Random();
        int[] array = new int[Main.ARRAY_SIZE];
        for (int i = 0; i < Main.ARRAY_SIZE; i++) {
            array[i] = random.nextInt(ARRAY_SIZE);
        }
        return array;
    }

    private static void quickSort(int[] array, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(array, left, right);
            quickSort(array, left, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, right);
        }
    }

    private static int partition(int[] array, int left, int right) {
        int pivot = array[right];
        int swapIndex = left -1;
        for (int i = left; i <= right; i++) {
            if (array[i] <= pivot) {
                swap(array, i, ++swapIndex);
            }
        }
        return swapIndex;
    }

    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}

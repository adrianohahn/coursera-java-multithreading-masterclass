package sh.hahn.multithreading.module6.lecture8;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class Main {

    private static final int[] array = new int[] {1, 2, 3, 4, 5, 6, 7, 8};

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (ForkJoinPool pool = new ForkJoinPool(2)) {
            ForkJoinTask<Void> task = pool.submit(new IncrementTask(0, 8));
            task.get();
            System.out.printf("Array: %s\n", Arrays.toString(array));
        }
    }

    static class IncrementTask extends RecursiveAction {

        private final int left;
        private final int right;

        public IncrementTask(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (right - left < 3) {
                for (int i = left; i < right; i++) {
                    array[i]++;
                }
            } else {
                int middle = (left + right) / 2;
                invokeAll(new IncrementTask(left, middle), new IncrementTask(middle, right));
            }
        }
    }

}

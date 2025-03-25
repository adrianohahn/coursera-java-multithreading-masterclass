package sh.hahn.multithreading.module5.lecture2.cyclic_barrier;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

public class Main {

    private static final int[][] array = {
            {1, 2, 3, 1},
            {2, 1, 2, 1},
            {1, 2, 1, 3},
            {1, 1, 1, 2},
    };

    private static final int[][] outputArray = {
            {1, 2, 3, 1},
            {9, 8, 9, 8},
            {35, 36, 35, 37},
            {144, 144, 144, 145},
    };

    private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(4,
            () -> System.out.println("The barrier was released"));

    public static void main(String[] args) {
        List<Thread> threads = IntStream.range(0, 4).mapToObj(WorkerThread::new)
                .map(Thread::new)
                .peek(Thread::start)
                .toList();

        threads.forEach(t -> {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        System.out.printf("Thread %s was interrupted\n", Thread.currentThread().getName());
                    }
                });
        System.out.printf("Arrays are equal: %s\n", Arrays.deepEquals(array, outputArray));
        System.out.printf("Array:\n%s\n", Arrays.deepToString(array));
    }

    static class WorkerThread implements Runnable {

        private final int columnID;

        public WorkerThread(int columnID) {
            this.columnID = columnID;
        }

        @Override
        public void run() {
            for (int i = 1; i < 4; i++) {
                int sum = 0;
                for (int j = 0; j < 4; j++) {
                    sum += array[i-1][j];
                }
                array[i][columnID] += sum;
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.printf("Thread %s was interrupted\n", Thread.currentThread().getName());
                }
            }
        }
    }

}

package sh.hahn.multithreading.module4.lecture4.locks;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Main {

    private static final Lock lock = new ReentrantLock();
    private static final int ARRAY_SIZE = 10;
    private static final int NUMBER_OF_THREADS = 5;
    private static final int[] array = new int[ARRAY_SIZE];
    private static int sum = 0;

    public static void main(String[] args) {
        Arrays.fill(array, 10);

        IntStream.range(0, NUMBER_OF_THREADS)
                .mapToObj(Main::createWorkerThread)
                .map(Thread::new)
                .peek(Thread::start)
                .forEach(t -> {
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
        });

        System.out.printf("Sum: %d\n", sum);

    }

    private static WorkerThread createWorkerThread(int i) {
        return new WorkerThread(i * ARRAY_SIZE / NUMBER_OF_THREADS,
                (i + 1) * ARRAY_SIZE / NUMBER_OF_THREADS);
    }

    static class WorkerThread implements Runnable {

        private final int left;
        private final int right;

        public WorkerThread(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void run() {
            for (int i = left; i < right; i++) {
                lock.lock();
                sum += array[i];
                lock.unlock();
            }
        }
    }

}

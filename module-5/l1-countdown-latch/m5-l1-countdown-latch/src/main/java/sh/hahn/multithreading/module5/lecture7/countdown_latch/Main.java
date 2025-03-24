package sh.hahn.multithreading.module5.lecture7.countdown_latch;

import java.util.concurrent.CountDownLatch;

public class Main {

    private static final int NUMBER_OF_THREADS = 2;
    private static final int[] array = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
    private static final CountDownLatch countDownLatch = new CountDownLatch(NUMBER_OF_THREADS);
    private static int positionFound;

    public static void main(String[] args) throws InterruptedException {
        int numberToSearch = 5;
        int threadSlice = array.length / NUMBER_OF_THREADS;

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Thread thread = new Thread(new WorkerThread(i * threadSlice, (i + 1) * threadSlice, numberToSearch));
            thread.start();
        }

        countDownLatch.await();
        System.out.printf("Number %d found at position %d%n", numberToSearch, positionFound);

    }

    static class WorkerThread implements Runnable {
        private final int left;
        private final int right;
        private final int numberToSearch;

        public WorkerThread(int left, int right, int numberToSearch) {
            this.left = left;
            this.right = right;
            this.numberToSearch = numberToSearch;
        }


        @Override
        public void run() {
            for (int i = left; i < right; i++) {
                if (array[i] == numberToSearch) {
                    System.out.printf("Thread %s found number %d at position %d%n", Thread.currentThread().getName(), numberToSearch, i);
                    positionFound = i;
                    break;
                }
            }
            countDownLatch.countDown();
        }
    }


}

package sh.hahn.multithreading.module6.lecture5;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
        try (ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                3,
                1,
                TimeUnit.SECONDS,
                workQueue,
                (r, t) -> System.out.println("Task rejected: " + r)
        )) {

            executor.submit(new SleepingTask());
            executor.submit(new SleepingTask());

            System.out.printf("[2] Pool size: %d, Work queue: %d\n", executor.getPoolSize(), workQueue.size());

            executor.submit(new SleepingTask());
            executor.submit(new SleepingTask());

            System.out.printf("[4] Pool size: %d, Work queue: %d\n", executor.getPoolSize(), workQueue.size());

            executor.submit(new SleepingTask());

            System.out.printf("[5] Pool size: %d, Work queue: %d\n", executor.getPoolSize(), workQueue.size());

            executor.submit(new SleepingTask());

            System.out.printf("[6] Pool size: %d, Work queue: %d\n", executor.getPoolSize(), workQueue.size());

            Thread.sleep(5000);
            System.out.printf("[+5s] Pool size: %d, Work queue: %d\n", executor.getPoolSize(), workQueue.size());

            Thread.sleep(5000);
            System.out.printf("[+5s] Pool size: %d, Work queue: %d\n", executor.getPoolSize(), workQueue.size());

        }
    }

    static class SleepingTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Sleeping Task Interrupted");
            }
        }
    }

}

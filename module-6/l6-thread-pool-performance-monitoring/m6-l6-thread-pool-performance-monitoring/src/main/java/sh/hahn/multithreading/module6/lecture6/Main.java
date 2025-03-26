package sh.hahn.multithreading.module6.lecture6;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {

    private static final int NUMBER_OF_THREADS = 10;

    private static final AtomicInteger rejectedTaskCount = new AtomicInteger(0);
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            2,
            3,
            1,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(5),
            (r, e) -> rejectedTaskCount.incrementAndGet()
    );
    private static final AtomicInteger threadCounter = new AtomicInteger(NUMBER_OF_THREADS);

    @SuppressWarnings("BusyWait")
    public static void main(String[] args) throws InterruptedException {
        Thread statisticsThread = new Thread(() -> {
            try {
                while (threadCounter.get() - rejectedTaskCount.get() > 0) {
                    printStatistics();
                    Thread.sleep(2500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        statisticsThread.start();

        try (executor) {
            IntStream.range(0, NUMBER_OF_THREADS)
                    .mapToObj(Worker::new)
                    .forEach(executor::execute);
        }

        statisticsThread.join();

        printStatistics();
    }

    static class Worker implements Runnable {

        private final int id;

        Worker(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Worker %d started\n", id);
                Thread.sleep(5000);
                threadCounter.decrementAndGet();
                System.out.printf("Worker %d finished\n", id);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void printStatistics() {
        System.out.println("===================================");
        System.out.println("Statistics:");
        System.out.println("===================================");
        System.out.printf("Core pool size: %d\n", executor.getCorePoolSize());
        System.out.printf("Largest pool size: %d\n", executor.getLargestPoolSize());
        System.out.printf("Maximum pool size: %d\n", executor.getMaximumPoolSize());
        System.out.println();
        System.out.printf("Pool size: %d\n", executor.getPoolSize());
        System.out.printf("Active threads: %d\n", executor.getActiveCount());
        System.out.printf("Task count: %d\n", executor.getTaskCount());
        System.out.printf("Completed task count: %d\n", executor.getCompletedTaskCount());
        System.out.println();
        System.out.printf("Rejected task count: %d\n", rejectedTaskCount.get());
        System.out.println("===================================");
        System.out.println();
        System.out.println();
    }

}

package sh.hahn.multithreading.module6.lecture2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    @SuppressWarnings({"resource", "ResultOfMethodCallIgnored"})
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3,
                5,
                1,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(3)
        );

        executor.prestartAllCoreThreads();

        executor.execute(() -> System.out.println("Task 1"));
        executor.execute(() -> System.out.println("Task 2"));

        Future<Integer> future = executor.submit(new CallableTask());
        // Do some other work
        future.get();

        System.out.printf("Pool size: %d\n", executor.getPoolSize());

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

    }

    static class CallableTask implements Callable<Integer> {
        @Override
        public Integer call() {
            return 4;
        }
    }

}

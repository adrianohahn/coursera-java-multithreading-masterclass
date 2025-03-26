package sh.hahn.multithreading.module6.lecture4;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        try (ThreadPoolExecutor executor = new CustomThreadPoolExecutor(
                2,
                2,
                1,
                TimeUnit.MINUTES,
                new SynchronousQueue<>()
        )) {
            // Handle exception in task 1 using future.get() and try catch block
            Future<Object> future = executor.submit(() -> {
                // Could be also handled with a try catch block
                throw new RuntimeException("Task 1 exception");
            });
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.printf("Error executing task: %s%n", e.getMessage());
            }

            // Handle exception in task 2 using afterExecute method
            executor.execute(() -> {
                throw new RuntimeException("Task 2 exception");
            });
        }
    }

    // Not really a good way to handle exceptions in a thread pool
    static class CustomThreadPoolExecutor extends ThreadPoolExecutor {

        public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            if (t != null) {
                System.out.printf("Error executing task: %s%n", t.getMessage());
            }
        }
    }



}

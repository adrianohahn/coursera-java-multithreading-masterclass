package sh.hahn.multithreading.module6.lecture9;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        /* Other types of executors can be:
        Executors.newFixedThreadPool();
        Executors.newSingleThreadExecutor();
        Executors.newWorkStealingPool();
        */
        try (ExecutorService executorService = Executors.newCachedThreadPool(new MyThreadFactory("MyThread-"))) {
            for (int i = 0; i < 10; i++) {
                executorService.execute(() -> System.out.printf("Thread: %s%n", Thread.currentThread().getName()));
            }
        }

    }

    static class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(0);
        private final String namePrefix;

        MyThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + threadNumber.incrementAndGet());
        }
    }

}

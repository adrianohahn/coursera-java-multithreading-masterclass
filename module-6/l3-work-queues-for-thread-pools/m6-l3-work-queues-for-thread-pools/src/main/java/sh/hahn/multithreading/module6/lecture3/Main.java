package sh.hahn.multithreading.module6.lecture3;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("resource")
public class Main {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                2,
                1,
                TimeUnit.MINUTES,
                new SynchronousQueue<>()
        );

        System.out.println("Submitting task 1");

        executor.submit(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task 1");
            } catch (InterruptedException e) {
                System.out.println("Task 1 interrupted");
            }
        });

        System.out.println("Submitting task 2");

        executor.submit(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task 2");
            } catch (InterruptedException e) {
                System.out.println("Task 2 interrupted");
            }
        });

        System.out.println("Submitting task 3");

        executor.submit(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Task 3");
            } catch (InterruptedException e) {
                System.out.println("Task 3 interrupted");
            }
        });
    }

}

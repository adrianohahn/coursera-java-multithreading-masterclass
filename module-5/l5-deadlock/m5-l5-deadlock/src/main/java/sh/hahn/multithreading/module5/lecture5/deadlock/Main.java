package sh.hahn.multithreading.module5.lecture5.deadlock;

import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private static final ReentrantLock lock1 = new ReentrantLock();
    private static final ReentrantLock lock2 = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            lock1.lock();
            System.out.println("Thread 1: lock1 locked");

            lock2.lock();
            System.out.println("Thread 1: lock2 locked");

            lock2.unlock();
            System.out.println("Thread 1: lock2 unlocked");

            lock1.unlock();
            System.out.println("Thread 1: lock1 unlocked");
        });

        Thread t2 = new Thread(() -> {
            lock2.lock();
            System.out.println("Thread 2: lock2 locked");

            lock1.lock();
            System.out.println("Thread 2: lock1 locked");

            lock1.unlock();
            System.out.println("Thread 2: lock1 unlocked");

            lock2.unlock();
            System.out.println("Thread 2: lock2 unlocked");
        });

        // Try to set the priority of the threads to avoid the deadlock. It works sometimes.
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);

        t1.start();
        t2.start();
    }

}

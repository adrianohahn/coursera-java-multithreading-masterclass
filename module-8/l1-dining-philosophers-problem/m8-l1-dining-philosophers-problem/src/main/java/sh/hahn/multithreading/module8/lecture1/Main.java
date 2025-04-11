package sh.hahn.multithreading.module8.lecture1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Main {

    static final List<Lock> forks = new ArrayList<>();

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(4);

        IntStream.range(0, 5)
                .peek(i -> forks.add(new ReentrantLock()))
                .mapToObj(i -> new Philosopher(i, semaphore))
                .map(Thread::new)
                .forEach(Thread::start);
    }

    static class Philosopher implements Runnable {

        private final int id;
        private final Semaphore semaphore;

        Philosopher(int id, Semaphore semaphore) {
            this.id = id;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            while (true) {
                think();
                pickUpForks();
                eat();
                putDownForks();
            }
        }

        private void putDownForks() {
            forks.get(id).unlock();
            System.out.printf("Philosopher %d put down the left fork (%d)\n", id, id);
            forks.get((id + 1) % forks.size()).unlock();
            System.out.printf("Philosopher %d put down the right fork (%d)\n", id, (id + 1) % forks.size());
            semaphore.release();
        }

        private void pickUpForks() {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            forks.get(id).lock();
            System.out.printf("Philosopher %d picked up the left fork (%d)\n", id, id);
            forks.get((id + 1) % forks.size()).lock();
            System.out.printf("Philosopher %d picked up the right fork (%d)\n", id, (id + 1) % forks.size());
        }

        private void eat() {
            System.out.printf("Philosopher %d is eating...\n", id);
        }

        private void think() {
            System.out.printf("Philosopher %d is thinking...\n", id);
        }
    }

}

package sh.hahn.multithreading.module8.lecture2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class Main {

    private static final Semaphore writerLock = new Semaphore(1);
    private static final Lock readerLock = new ReentrantLock();
    private static int readers = 0;
    private static int counter = 0;

    public static void main(String[] args) {
        IntStream.range(0, 4)
                .mapToObj(Reader::new)
                .map(Thread::new)
                .forEach(Thread::start);

        IntStream.range(0, 2)
                .mapToObj(Writer::new)
                .map(Thread::new)
                .forEach(Thread::start);

    }

    static class Reader implements Runnable {

        private final int id;

        Reader(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                readerLock.lock();
                readers++;
                if (readers == 1) {
                    try {
                        writerLock.acquire();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                readerLock.unlock();

                System.out.printf("[Reader %d] Value is %d\n", id, counter);

                readerLock.lock();
                readers--;
                if (readers == 0) {
                    writerLock.release();
                }
                readerLock.unlock();
                sleep();
            }
        }
    }

    static class Writer implements Runnable {

        private final int id;

        Writer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    writerLock.acquire();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                counter++;
                System.out.printf("[Writer %d] Value updated to %d\n", id, counter);
                writerLock.release();
                sleep();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

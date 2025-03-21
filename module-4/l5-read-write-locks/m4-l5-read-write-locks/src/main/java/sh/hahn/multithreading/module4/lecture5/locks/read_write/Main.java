package sh.hahn.multithreading.module4.lecture5.locks.read_write;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class Main {

    private static final Random random = new Random();
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();
    private static final List<Integer> list = new ArrayList<>();
    private static final int NUMBER_OF_READERS = 5;

    public static void main(String[] args) {
        new Thread(Main::runWrite).start();
        IntStream.range(0, NUMBER_OF_READERS).forEach(i -> new Thread(Main::runRead).start());
    }

    private static void runWrite() {
        while (true) {
            sleep(10000);

            writeLock.lock();

            int randomInt = random.nextInt(100);
            System.out.printf("Producing data: %d\n", randomInt);
            list.add(randomInt);

            sleep(3000);

            writeLock.unlock();
        }
    }

    private static void runRead() {
        while (true) {
            sleep(4000);

            while (!readLock.tryLock()) {
                System.out.println("Waiting for read lock...");
                sleep(100);
            }

            System.out.printf("List is: %s\n", list);

            readLock.unlock();
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }





}

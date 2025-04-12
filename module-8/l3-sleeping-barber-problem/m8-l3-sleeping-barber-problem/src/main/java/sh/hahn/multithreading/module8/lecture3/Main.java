package sh.hahn.multithreading.module8.lecture3;

import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private static final int MAX_CUSTOMERS = 5;
    private static final String[] CUSTOMER_NAMES = {
            "Alice", "Bob", "Charlie", "David", "Eve",
            "Frank", "Grace", "Heidi", "Ivan", "Judy"
    };
    private static int customerCount = 0;
    private static final Lock customerCountLock = new ReentrantLock();
    private static final Semaphore nextCustomerSemaphore = new Semaphore(0);
    private static final Semaphore barberNextHaircutSemaphore = new Semaphore(0);
    private static final Semaphore customerFinishedSemaphore = new Semaphore(0);
    private static final Semaphore barberFinishedSemaphore = new Semaphore(0);

    private static String currentCustomerName;
    private static final Semaphore customerIdSemaphore = new Semaphore(0);

    public static void main(String[] args) {
        new Thread(new Barber()).start();
        Arrays.stream(CUSTOMER_NAMES)
                .map(Customer::new)
                .map(Thread::new)
                .forEach(Thread::start);
    }

    private static void acquireSemaphore(Semaphore semaphore) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static class Barber implements Runnable {

        @Override
        public void run() {
            while (true) {
                acquireSemaphore(nextCustomerSemaphore);
                barberNextHaircutSemaphore.release();

                acquireSemaphore(customerIdSemaphore);

                System.out.printf("[Barber] Cutting %s's hair%n", currentCustomerName);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.printf("[Barber] Finished cutting %s's hair%n", currentCustomerName);

                barberFinishedSemaphore.release();
                acquireSemaphore(customerFinishedSemaphore);

            }
        }
    }

    static class Customer implements Runnable {

        private final String name;

        Customer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            customerCountLock.lock();

            if (customerCount == MAX_CUSTOMERS) {
                customerCountLock.unlock();
                System.out.printf("[%s] Room is full, customer is leaving%n", name);
                return;
            }

            customerCount++;
            customerCountLock.unlock();

            nextCustomerSemaphore.release();
            acquireSemaphore(barberNextHaircutSemaphore);

            currentCustomerName = name;
            customerIdSemaphore.release();
            System.out.printf("[%s] Getting a haircut%n", name);

            acquireSemaphore(barberFinishedSemaphore);
            customerFinishedSemaphore.release();

            System.out.printf("[%s] Finished haircut%n", name);

            customerCountLock.lock();
            customerCount--;
            customerCountLock.unlock();

        }
    }

}

package sh.hahn.multithreading.module4.lecture6.producer_consumer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();
        Thread producer = new Thread(new Producer(queue));
        Thread consumer = new Thread(new Consumer(queue));

        consumer.start();
        producer.start();
    }

    static class Producer implements Runnable {
        private final Queue<String> queue;

        public Producer(Queue<String> queue) {
            this.queue = queue;
        }

        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            while (true) {
                try {
                    produceData();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        private void produceData() throws InterruptedException {
            lock.lock();
            if (queue.size() == 10) {
                System.out.println("Queue is full, producer is waiting for consumer to consume data");
                condition.await();
            }

            Thread.sleep(1000);

            System.out.printf("Producing element with id: %d%n", queue.size());
            queue.add(String.format("element_%d", queue.size()));

            if (queue.size() == 1) {
                condition.signal();
            }
            lock.unlock();
        }
    }

    static class Consumer implements Runnable {
        private final Queue<String> queue;

        public Consumer(Queue<String> queue) {
            this.queue = queue;
        }

        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            while (true) {
                try {
                    consumeData();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void consumeData() throws InterruptedException {
            lock.lock();
            if (queue.isEmpty()) {
                System.out.println("Queue is empty, consumer is waiting for producer to produce data");
                condition.await();
            }

            Thread.sleep(700);

            String element = queue.remove();
            System.out.printf("Consuming element: %s%n", element);

            if (queue.size() == 9) {
                condition.signal();
            }
            lock.unlock();
        }
    }

}

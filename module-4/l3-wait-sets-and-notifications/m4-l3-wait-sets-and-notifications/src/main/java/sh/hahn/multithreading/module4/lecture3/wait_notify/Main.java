package sh.hahn.multithreading.module4.lecture3.wait_notify;

import java.util.LinkedList;
import java.util.Queue;

public class Main {

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
            synchronized (queue) {
                if (queue.size() == 10) {
                    System.out.println("Queue is full, producer is waiting for consumer to consume data");
                    queue.wait();
                }

                Thread.sleep(1000);

                System.out.printf("Producing element with id: %d%n", queue.size());
                queue.add(String.format("element_%d", queue.size()));

                if (queue.size() == 1) {
                    queue.notify();
                }
            }
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
            synchronized (queue) {
                if (queue.isEmpty()) {
                    System.out.println("Queue is empty, consumer is waiting for producer to produce data");
                    queue.wait();
                }

                Thread.sleep(700);

                String element = queue.remove();
                System.out.printf("Consuming element: %s%n", element);

                if (queue.size() == 9) {
                    queue.notify();
                }
            }
        }
    }

}

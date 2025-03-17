package sh.hahn.multithreading.module3.lecture6.race_conditions;

import java.util.List;
import java.util.stream.IntStream;

public class Main {

    private static final int NUMBER_OF_THREADS = 10;
    private static final int INCREMENTS = 10000; // The higher the number, the more likely the race condition will occur
    private static int globalCounter = 0;

    public static void main(String[] args) {
        List<Thread> threads = IntStream.range(0, NUMBER_OF_THREADS)
                .mapToObj(Main::createIncrementThread)
                .peek(Thread::start)
                .toList();

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.printf("Thread %s was interrupted%n", thread.getName());
            }
        });

        System.out.printf("Global counter should be: %d. Actual value: %d%n",
                NUMBER_OF_THREADS * INCREMENTS,
                globalCounter);
    }

    private static Thread createIncrementThread(int threadNumber) {
        return new Thread(() -> {
            for (int i = 0; i < INCREMENTS; i++) {
                globalCounter++;
            }
        }, "Thread" + threadNumber);
    }

}

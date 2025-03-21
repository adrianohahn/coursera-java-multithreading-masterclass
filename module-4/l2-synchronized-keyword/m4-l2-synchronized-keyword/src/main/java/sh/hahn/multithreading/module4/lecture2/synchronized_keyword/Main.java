package sh.hahn.multithreading.module4.lecture2.synchronized_keyword;

import java.util.List;
import java.util.stream.IntStream;

public class Main {

    private static final int NUMBER_OF_THREADS = 1000;
    private static int globalCounter = 0;
    private static final ThreadGroup threadGroup = new ThreadGroup("IncrementThreads");
    private static final Object lock = new Object();

    public static void main(String[] args) {
        List<Thread> threads = IntStream.range(0, NUMBER_OF_THREADS)
                .mapToObj(Main::createIncrementThread)
                .peek(Thread::start)
                .toList();

        threadGroup.interrupt();

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.printf("Thread %s was interrupted%n", thread.getName());
            }
        });

        System.out.printf("Global counter should be: %d. Actual value: %d%n",
                NUMBER_OF_THREADS,
                globalCounter);
    }

    private static Thread createIncrementThread(int threadNumber) {
        return new Thread(threadGroup, () -> {
            try {
                Thread.sleep(99999);
            } catch (InterruptedException e) {
                // ignore
            }
            synchronized (lock) {
                globalCounter++;
            }
        }, "Thread" + threadNumber);
    }

}

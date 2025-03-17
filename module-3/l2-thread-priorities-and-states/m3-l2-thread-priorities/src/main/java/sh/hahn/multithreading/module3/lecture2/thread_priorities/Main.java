package sh.hahn.multithreading.module3.lecture2.thread_priorities;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        Thread thread1 = createThread("Thread 1", Thread.MIN_PRIORITY);
        Thread thread2 = createThread("Thread 2", Thread.MAX_PRIORITY);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }

    private static Thread createThread(String name, int priority) {
        Thread thread1 = new Thread(Main::runThread);
        thread1.setName(name);
        thread1.setPriority(priority);
        return thread1;
    }

    private static void runThread() {
        Thread currentThread = Thread.currentThread();
        System.out.printf("Thread %s has priority %d\n", currentThread.getName(), currentThread.getPriority());
    }


}

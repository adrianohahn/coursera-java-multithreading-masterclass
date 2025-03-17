package sh.hahn.multithreading.module3.lecture2.thread_states;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> printThreadState(Thread.currentThread(), 1));

        printThreadState(thread, 2);

        thread.start();

        printThreadState(thread, 3);

        thread.join();

        printThreadState(thread, 4);

    }

    private static void  printThreadState(Thread thread, int order) {
        System.out.printf("[%d] Thread state: %s%n", order, thread.getState());
    }

}

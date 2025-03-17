package sh.hahn.multithreading.module3.lecture6.local_variables;

public class Main {

    private static final ThreadLocal<String> threadLocalString = new ThreadLocal<>();
    private static final String MAIN_THREAD_STRING_VALUE = "Main thread";
    private static final String NEW_THREAD_STRING_VALUE = "New thread";
    private static String globalString;

    public static void main(String[] args) throws InterruptedException {
        threadLocalString.set(MAIN_THREAD_STRING_VALUE);
        globalString = MAIN_THREAD_STRING_VALUE;
        printData();

        Thread thread = new Thread(() -> {
            threadLocalString.set(NEW_THREAD_STRING_VALUE);
            globalString = NEW_THREAD_STRING_VALUE;
            printData();
        }, "New-Thread");
        thread.start();

        printData();

        thread.join();

        printData();
    }

    private static void printData() {
        Thread currentThread = Thread.currentThread();
        System.out.printf("Thread %s%n\tglobal string value: %s%n\tlocal string value:  %s%n",
                currentThread.getName(),
                globalString,
                threadLocalString.get());
    }

}

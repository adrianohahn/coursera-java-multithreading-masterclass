package sh.hahn.multithreading.module3.lecture5.example3;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(
                () -> {
                    throw new RuntimeException("Exception from thread");
                },
                "Exception-Thread");

        thread.setUncaughtExceptionHandler((t, e) ->
                System.out.printf("Uncaught exception in thread %s: %s%n",
                        t.getName(),
                        e.getMessage()));

        thread.start();
        thread.join();
    }

}

package sh.hahn.multithreading.module3.lecture5.example2;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(
                () -> {
                    throw new RuntimeException("Exception from thread");
                },
                "Exception-Thread");

        Thread.setDefaultUncaughtExceptionHandler((t, e) ->
                System.out.printf("Uncaught exception in thread %s: %s%n",
                t.getName(),
                e.getMessage()));

        thread.start();
        thread.join();
    }

}

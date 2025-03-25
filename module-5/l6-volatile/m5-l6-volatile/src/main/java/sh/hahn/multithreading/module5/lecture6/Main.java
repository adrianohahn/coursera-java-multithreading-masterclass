package sh.hahn.multithreading.module5.lecture6;

public class Main {

    private static volatile int counter = 0;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            int localCounter = counter;
            while (localCounter < 10) {
                if (localCounter != counter) {
                    System.out.printf("[T1] Local counter changed: %d%n", localCounter);
                    localCounter = counter;
                }
            }
        });

        Thread t2 = new Thread(() -> {
            int localCounter = counter;
            while (localCounter < 10) {
                System.out.printf("[T2] Incrementing counter to %d%n", localCounter + 1);
                counter = ++localCounter;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("[T2] Thread interrupted");
                }
            }
        });

        t1.start();
        t2.start();

    }

}

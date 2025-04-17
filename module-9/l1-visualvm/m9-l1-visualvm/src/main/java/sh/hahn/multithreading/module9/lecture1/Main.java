package sh.hahn.multithreading.module9.lecture1;

public class Main {

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            double var = 1;
            while (true) {
                for (int i = 0; i < 100000000; i++) {
                    var = Math.tan(var);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t.setName("new-thread");
        t.start();

        double var = 1;
        while (true) {
            for (int i = 0; i < 100000000; i++) {
                var = Math.tan(var);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

}

package sh.hahn.multithreading.module3.lecture4.daemon;

public class Main {

    public static void main(String[] args) {
        Thread daemonThread = new Thread(new MyThread(10), "Daemon");
        Thread userThread = new Thread(new MyThread(3), "User");

        daemonThread.setDaemon(true);

        daemonThread.start();
        userThread.start();
        // Uncomment the following line to wait for the daemon thread to finish
//        daemonThread.join();
    }

    static class MyThread implements Runnable {

        private final int numberOfSeconds;

        public MyThread(int numberOfSeconds) {
            this.numberOfSeconds = numberOfSeconds;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < numberOfSeconds; i++) {
                    System.out.printf("%s Thread is sleeping for 1 second. Total sleep in seconds: %d\n",
                            Thread.currentThread().getName(), i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.printf("%s Thread interrupted", Thread.currentThread().getName());
            }
        }
    }
}

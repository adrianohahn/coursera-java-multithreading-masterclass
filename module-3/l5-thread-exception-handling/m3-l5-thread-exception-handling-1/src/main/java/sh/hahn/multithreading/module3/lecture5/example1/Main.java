package sh.hahn.multithreading.module3.lecture5.example1;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new CustomThreadGroup("CustomThreadGroup"),
                () -> {
                    throw new RuntimeException("Exception from thread");
                },
                "Exception-Thread");
        thread.start();
        thread.join();
    }

    static class CustomThreadGroup extends ThreadGroup {
        public CustomThreadGroup(String name) {
            super(name);
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("Uncaught exception in thread " + t.getName() + ": " + e.getMessage());
        }
    }

}

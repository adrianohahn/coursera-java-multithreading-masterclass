package sh.hahn.multithreading.module3.lecture1.example3;

public class Main {

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.printf("Current thread: %s\n", Thread.currentThread().getName());
            }
        };

        Thread myThread = new Thread(runnable);
        myThread.setName("MyThread");
        myThread.start();
    }

}

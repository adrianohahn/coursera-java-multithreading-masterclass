package sh.hahn.multithreading.module3.lecture1.example3;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.printf("[1] Current thread: %s\n", Thread.currentThread().getName());

        Thread myThread = new Thread(
                () -> System.out.printf("[2] Current thread: %s\n", Thread.currentThread().getName())
        );
        myThread.setName("MyThread");
        myThread.start();
        myThread.join();

        System.out.printf("[3] Current thread: %s\n", Thread.currentThread().getName());
    }

}

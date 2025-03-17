package sh.hahn.multithreading.module3.lecture1.example1;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        System.out.printf("Thread name: %s\n", currentThread.getName());

        Thread.sleep(3000);

        System.out.printf("Thread name: %s\n", currentThread.getName());
    }
}
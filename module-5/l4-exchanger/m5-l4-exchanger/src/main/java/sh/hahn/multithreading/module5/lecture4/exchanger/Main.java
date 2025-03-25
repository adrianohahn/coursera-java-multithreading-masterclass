package sh.hahn.multithreading.module5.lecture4.exchanger;

import java.util.concurrent.Exchanger;

public class Main {

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        Thread t = new Thread(() -> {
            try {
                String message = exchanger.exchange("New thread value");
                System.out.printf("Value received in new thread: %s\n", message);
            } catch (InterruptedException e) {
                System.out.printf("Thread %s was interrupted\n", Thread.currentThread().getName());
            }
        });
        t.start();
        try {
            String message = exchanger.exchange("Main thread value");
            System.out.printf("Value received in main thread: %s\n", message);
        } catch (InterruptedException e) {
            System.out.printf("Thread %s was interrupted\n", Thread.currentThread().getName());
        }

    }

}

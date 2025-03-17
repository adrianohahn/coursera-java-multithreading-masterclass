package sh.hahn.multithreading.module3.lecture1.example2;

public class Main {

    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        myThread.start();
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            setName("MyThread");
            System.out.printf("Current thread name: %s\n", Thread.currentThread().getName());
        }
    }

}

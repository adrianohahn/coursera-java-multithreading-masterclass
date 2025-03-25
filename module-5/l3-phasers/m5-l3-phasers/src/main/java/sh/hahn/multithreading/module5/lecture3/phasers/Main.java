package sh.hahn.multithreading.module5.lecture3.phasers;

import java.util.Arrays;
import java.util.concurrent.Phaser;
import java.util.stream.IntStream;

public class Main {

    private static final Phaser phaser = new Phaser(1);

    private static final int[] array = new int[] {1, 2, 3, 4, 5, 6, 7, 8};
    private static int sum = 0;


    public static void main(String[] args) {
        IntStream.range(0, array.length)
                .mapToObj(WorkerThread::new)
                .map(Thread::new)
                .forEach(Thread::start);

        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndAwaitAdvance();

        System.out.printf("Sum: %d\n", sum);
        System.out.printf("Phase count: %d\n", phaser.getPhase());

    }

    static class WorkerThread implements Runnable {

        private final int index;

        public WorkerThread(int index) {
            this.index = index;
            phaser.register();
        }

        @Override
        public void run() {
            array[index] *= 2;
            phaser.arriveAndAwaitAdvance();

            if (index == 0) {
                sum = Arrays.stream(array).sum();
                phaser.arriveAndAwaitAdvance();
            } else {
                phaser.arriveAndDeregister();
            }
        }
    }

}

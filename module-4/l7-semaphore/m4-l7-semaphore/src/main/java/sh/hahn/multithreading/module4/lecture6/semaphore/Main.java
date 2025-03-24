package sh.hahn.multithreading.module4.lecture6.semaphore;

import java.util.concurrent.Semaphore;

public class Main {

    private static final Semaphore semaphore = new Semaphore(2);

    public static void main(String[] args) throws InterruptedException {
        Executor executor = new Executor();

        executor.submit(new Job(4000));
        executor.submit(new Job(5000));
        executor.submit(new Job(3000));
    }

    static class Executor {

        public void submit(Job job) throws InterruptedException {
            semaphore.acquire();
            Thread thread = new Thread(() -> {
                try {
                    System.out.printf("Executing job with work: %d%n", job.getWork());
                    Thread.sleep(job.getWork());
                    semaphore.release();
                    System.out.printf("Job with work: %d is done%n", job.getWork());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }

    }

    static class Job {

        private final int work;


        Job(int work) {
            this.work = work;
        }

        public int getWork() {
            return work;
        }
    }
}

package sh.hahn.multithreading.module6.lecture7;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        try (ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3)) {
            executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true); // avoid stopping delayed tasks
            executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(true); // avoid stopping periodic tasks
            executor.schedule(() -> System.out.println("Task 1"), 1, TimeUnit.SECONDS);
            executor.scheduleAtFixedRate(() -> System.out.println("Task 2"), 5, 1, TimeUnit.SECONDS);
        }

    }

}

package sh.hahn.multithreading.module3.lecture3.thread_groups;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup threadGroup = new ThreadGroup("MyThreadGroup");
        threadGroup.setMaxPriority(7);

        ThreadGroup parentThreadGroup = threadGroup;
        for (String currentThreadGroupName = threadGroup.getName();
             (parentThreadGroup = parentThreadGroup.getParent()) != null;
             currentThreadGroupName = parentThreadGroup.getName())
        {
            System.out.printf("Thread group %s's parent thread group is '%s' with max priority %d\n",
                    currentThreadGroupName,
                    parentThreadGroup.getName(),
                    parentThreadGroup.getMaxPriority());

        }

        Thread thread1 = new Thread(threadGroup, Main::runThread, "Thread 1");
        Thread thread2 = new Thread(threadGroup, Main::runThread, "Thread 2");
        Thread thread3 = new Thread(threadGroup, Main::runThread, "Thread 3");

        thread1.setPriority(Thread.MAX_PRIORITY);

        thread1.start();
        thread2.start();
        thread3.start();

        System.out.println("Sleeping for 3 seconds");
        Thread.sleep(3000);

        threadGroup.interrupt();

        thread1.join();
        thread2.join();
        thread3.join();

    }

    private static void runThread() {
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread currentThread = Thread.currentThread();
                System.out.printf("Thread %s with priority %d was interrupted\n", currentThread.getName(), currentThread.getPriority());
                break;
            }
        }
    }

}

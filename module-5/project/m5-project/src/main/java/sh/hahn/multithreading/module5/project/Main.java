package sh.hahn.multithreading.module5.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class Main {

    private static final int NUM_MAPPERS = 2;
    private static final String INPUT_DATA = "a friend in need is a friend indeed";
    private static final List<Map.Entry<String, Integer>> intermediateResult = Collections.synchronizedList(new ArrayList<>());
    private static final List<List<Map.Entry<String, Integer>>> reducersInput = Collections.synchronizedList(new ArrayList<>());
    private static final CountDownLatch mappersLatch = new CountDownLatch(NUM_MAPPERS);

    public static void main(String[] args) throws InterruptedException {
        List<String> inputList = Arrays.asList(INPUT_DATA.split(" "));
        IntStream.range(0, 2)
                .mapToObj(i -> inputList.subList(i * inputList.size() / 2, (i + 1) * inputList.size() / 2))
                .map(Mapper::new)
                .map(Thread::new)
                .forEach(Thread::start);

        System.out.printf("Intermediate result: %s\n", intermediateResult);

        Thread partitionerThread = new Thread(new Partitioner());
        partitionerThread.start();
        partitionerThread.join();

        System.out.printf("Reducers input: %s\n", reducersInput);

        reducersInput.stream()
                .map(Reducer::new)
                .map(Thread::new)
                .forEach(Thread::start);

    }

    static class Mapper implements Runnable {

        private final List<String> inputList;

        public Mapper(List<String> inputList) {
            this.inputList = inputList;
        }

        @Override
        public void run() {
            inputList.forEach(word -> intermediateResult.add(Map.entry(word, 1)));
            mappersLatch.countDown();
        }
    }

    static class Partitioner implements Runnable {

        @Override
        public void run() {
            try {
                mappersLatch.await();
            } catch (InterruptedException e) {
                System.out.println("Partitioner thread was interrupted");
            }

            List<String> uniqueWords = intermediateResult.stream()
                    .map(Map.Entry::getKey)
                    .distinct()
                    .toList();

            for (String word : uniqueWords) {
                List<Map.Entry<String, Integer>> wordCount = intermediateResult.stream()
                        .filter(entry -> entry.getKey().equals(word))
                        .toList();
                reducersInput.add(wordCount);
            }
        }
    }

    static class Reducer implements Runnable {

        private final List<Map.Entry<String, Integer>> inputList;

        public Reducer(List<Map.Entry<String, Integer>> inputList) {
            this.inputList = inputList;
        }

        @Override
        public void run() {
            String word = inputList.getFirst().getKey();
            int sum = inputList.stream()
                    .map(Map.Entry::getValue)
                    .reduce(0, Integer::sum);

            System.out.printf("Word: %s, Count: %d\n", word, sum);
        }
    }

}

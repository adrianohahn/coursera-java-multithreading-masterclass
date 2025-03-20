package sh.hahn.multithreading.module3.lecture7.parallel_text_processing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.stream.Stream;

/*
 * === REQUIREMENTS ===
 * 1. Periodically scans the ./src/main/resources directory and watches for new files
 * 2. For each file found into this directory, a new thread should take care of its processing
 * 3. Processing = each line of the file will be hashed using a hashing algorithm
 * 4. The program will create new files, marked _output suffix, and they will placed into ./src/main/output
 * 5. Throw an exception if a line is empty
 */
public class Main {

    private static final String MODULE_PATH = "./module-3/l7-parallel-text-file-processing/m3-l7-parallel-text-file-processing";

    public static void main(String[] args) {
        new Thread(Main::runWatcher).start();
    }

    @SuppressWarnings("BusyWait")
    private static void runWatcher() {
        File directory =
                new File(MODULE_PATH + "/src/main/resources");

        while (true) {
            File[] files = directory.listFiles();
            if (files != null) {
                Arrays.stream(files)
                        .parallel()
                        .filter(File::isFile)
                        .map(Main::createThreadProcessor)
                        .map(Thread::new)
                        .peek(Main::addThreadExceptionHandler)
                        .forEach(Thread::start);
            }

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Watcher thread was interrupted", e);
            }
        }
    }

    private static void addThreadExceptionHandler(final Thread thread) {
        thread.setUncaughtExceptionHandler((t, e ) ->
                System.out.printf("An error occurred in thread %s: %s%n", t.getName(), e.getMessage())
        );
    }

    private static Runnable createThreadProcessor(final File file) {
        return () -> {
            System.out.printf("Thread '%s' is processing file %s%n", Thread.currentThread().getName(), file.getName());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(MODULE_PATH + "/src/main/resources/output/" + file.getName() + "_output"))) {
                try (Stream<String> lines = Files.lines(Path.of(file.getAbsolutePath()))) {
                    lines.map(Main::hashLine)
                            .map(line -> line + System.lineSeparator())
                            .forEach(line -> {
                                try {
                                    writer.write(line);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.printf("Thread '%s' processed file '%s'%n", Thread.currentThread().getName(), file.getName());
        };
    }

    private static String hashLine(String input) {
        if (input.isEmpty()) {
            throw new RuntimeException("The line is empty, hashing cannot be done");
        }

        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            final byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

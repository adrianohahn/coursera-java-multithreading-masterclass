package sh.hahn.multithreading.module9.lecture5;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Flowable<String> flow = getSource();
        flow.parallel(4)
                .runOn(Schedulers.computation())
                .map(String::toUpperCase)
                .sequential()
                .subscribe(System.out::println)
        .dispose();
    }

    private static  Flowable<String> getSource() throws IOException {
        String fileName = "module-9/l5-rxjava-data-pipelines/m9-l5-rxjava-data-pipelines/src/main/resources/numbers.txt";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        return Flowable.generate((emitter) -> {
            String value = reader.readLine();
            if (value == null) {
                emitter.onComplete();
            } else {
                emitter.onNext(value);
            }
        });
    }

}

package sh.hahn.multithreading.module9.lecture3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class CustomController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/hello")
    public String hello() throws InterruptedException, ExecutionException {
        CompletableFuture<Boolean> future = customerService.backgroundWork();
        return "Hello Back! with response = " + future.get() + "\n";
    }

}

package br.com.picpay.backend;

import br.com.picpay.backend.config.TestContainersConfiguration;
import org.springframework.boot.SpringApplication;

public class PicpayBackendChallengeApplicationTests {

    public static void main(String[] args) {
        SpringApplication.from(PicpayBackendApplication::main).with(TestContainersConfiguration.class).run(args);
    }

}

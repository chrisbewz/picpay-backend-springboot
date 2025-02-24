package br.com.picpay.backend;

import org.springframework.boot.SpringApplication;

public class TestPicpayDesafioBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(PicpayBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

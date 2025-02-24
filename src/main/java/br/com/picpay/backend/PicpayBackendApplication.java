package br.com.picpay.backend;

import br.com.picpay.backend.data.mongo.repositories.UserRepository;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongock
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
public class PicpayBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicpayBackendApplication.class, args);
    }

}

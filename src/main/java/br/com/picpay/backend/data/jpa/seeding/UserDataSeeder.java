package br.com.picpay.backend.data.jpa.seeding;

import br.com.picpay.backend.data.enums.UserKnownTypes;
import br.com.picpay.backend.data.mongo.documents.UserDocument;
import br.com.picpay.backend.data.mongo.repositories.UserRepository;
import io.mongock.api.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Log4j2
@ChangeUnit(id="users-seeder", order = "1", author = "chrisbewz")
public class UserDataSeeder {

    private static final Random sampler = new Random();

    private static final UserKnownTypes[] userTypes = UserKnownTypes.values();

    @BeforeExecution
    public void beforeExecution(MongoTemplate mongoTemplate) {
        log.info("Creating users collection");
        mongoTemplate.createCollection("users");
    }
    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoTemplate mongoTemplate) {
        log.info("Dropping existing users collection");
        mongoTemplate.dropCollection("users");
    }
    @Execution
    public void execution(UserRepository repository) {
        log.info("Preparing users collection items");

        repository.saveAll(IntStream.range(0, 10)
                .mapToObj(UserDataSeeder::sampleDocument)
                .collect(Collectors.toList()));
    }
    @RollbackExecution
    public void rollbackExecution(UserRepository repository) {
        log.info("######### Deleting existing users collection items");
        repository.deleteAll();
    }

    private static UserDocument sampleDocument(int i) {
        return UserDocument.builder()
                .userName("user" + i)
                .userEmail(String.format("user-%d@gmail.com",i))
                .userType(userTypes[ThreadLocalRandom.current().nextInt(userTypes.length)])
                .currencyAmount(sampler.nextDouble(0,1000))
                .build();
    }
}

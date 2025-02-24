package br.com.picpay.backend.config;

import io.mongock.driver.api.driver.ConnectionDriver;
import io.mongock.driver.mongodb.springdata.v4.SpringDataMongoV4Driver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockApplicationRunner;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfiguration {
//    private final MongoProperties mongoProperties;
//
//    public MongoConfiguration(MongoProperties mongoProperties) {
//        this.mongoProperties = mongoProperties;
//    }
//
//    @Bean
//    public MongoClient mongoClient() {
//        return MongoClients.create(MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString(this.mongoProperties.getUri()))
//                .uuidRepresentation(UuidRepresentation.STANDARD)
//                .build());
//    }
//
//    @Bean
//    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
//        return new MongoTemplate(mongoClient, this.mongoProperties.getDatabase());
//    }

    @Bean
    public MongockInitializingBeanRunner mongockRunner(ConnectionDriver driver, ApplicationContext applicationContext) {
        return MongockSpringboot.builder()
                .setDriver(driver)
                .setSpringContext(applicationContext)
                .addMigrationScanPackage("br.com.picpay.backend.data.jpa.seeding")
                .buildInitializingBeanRunner();
    }

    @Bean
    @Primary
    public ConnectionDriver getConnectionDriver(MongoTemplate mongoTemplate) {
        return SpringDataMongoV4Driver.withDefaultLock(mongoTemplate);
    }
}

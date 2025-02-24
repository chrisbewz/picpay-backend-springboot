package br.com.picpay.backend.config;

import br.com.picpay.backend.exceptions.ApiErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        ApiErrorHandler.class,
        MongoConfiguration.class
})
public class ApiConfiguration {
}

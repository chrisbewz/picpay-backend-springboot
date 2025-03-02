package br.com.picpay.backend.services;

import br.com.picpay.backend.data.dtos.AuthResponse;
import br.com.picpay.backend.data.entities.User;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final static Integer authorizationReadTimeoutSeconds = 10;

    private final WebClient.Builder webClientBuilder;

    @Value("${app.authEndpoint}")
    private String authorizationEndpoint;

    @Configuration
    static class WebFluxConfig {

        @Bean
        @Scope("prototype")
        public HttpClient getHttpClient() {
            return HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, authorizationReadTimeoutSeconds * 1000)
                    .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(authorizationReadTimeoutSeconds)));
        }

        @Bean
        @Scope("prototype")
        public WebClient.Builder webClientBuilder(HttpClient httpClient) {
            return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
        }
    }

    public boolean isAuthorized(User user)
    {
        return checkAuthStatus().data().authorized();
    }

    private AuthResponse checkAuthStatus()
    {
        var client = webClientBuilder
                .baseUrl(authorizationEndpoint)
                .build();

        var uriSpec = client.method(HttpMethod.GET);

        var headerSpec = uriSpec.uri("/authorize");

        var responseSpec = headerSpec.retrieve();

        return responseSpec
                .bodyToMono(AuthResponse.class)
                .block();
    }
}

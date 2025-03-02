package br.com.picpay.backend.services;

import br.com.picpay.backend.config.WebFluxCustomConfig;
import br.com.picpay.backend.data.dtos.AuthResponse;
import br.com.picpay.backend.data.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Import(WebFluxCustomConfig.class)
@RequiredArgsConstructor
public class AuthorizationService {

    private final WebClient.Builder webClientBuilder;

    @Value("${app.authEndpoint}")
    private String authorizationEndpoint;

    public boolean isAuthorized(User user)
    {
        var client = webClientBuilder
            .baseUrl(authorizationEndpoint)
            .build();

        var uriSpec = client.method(HttpMethod.GET);

        var headerSpec = uriSpec.uri("/authorize");

        var responseSpec = headerSpec.retrieve();

        return responseSpec
                .bodyToMono(AuthResponse.class)
                .block()
                .data()
                .authorized();
    }
}

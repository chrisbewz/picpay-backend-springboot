package br.com.picpay.backend.services;

import br.com.picpay.backend.config.WebFluxCustomConfig;
import br.com.picpay.backend.data.dtos.AuthData;
import br.com.picpay.backend.data.dtos.AuthResponse;
import br.com.picpay.backend.data.entities.User;
import br.com.picpay.backend.data.enums.AuthStatus;
import br.com.picpay.backend.exceptions.base.*;
import lombok.RequiredArgsConstructor;
import net.xyzsd.dichotomy.Result;
import net.xyzsd.dichotomy.trying.Try;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;


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

        Mono<AuthResponse> responseSpec = headerSpec.retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> switch (response.statusCode().value()){
                            case 401, 403 -> Mono.error(new NotAuthorizedException());
                            case 500 -> Mono.error(new InternalServerException("An unexpected error occurred while attempting to check user authorization on provided endpoint."));
                            default -> throw new IllegalStateException("Unexpected value: " + response.statusCode().value());
                        })
                .bodyToMono(AuthResponse.class)
                .onErrorMap(throwable -> throwable);

        var fetchTask = Try.wrap(() -> responseSpec.block());

        if(fetchTask.isSuccess()) {
            var content = fetchTask.expect();
            return content.status().equals(AuthStatus.Success);
        }

        return false;
    }
}

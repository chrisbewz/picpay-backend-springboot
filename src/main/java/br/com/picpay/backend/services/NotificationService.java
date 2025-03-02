package br.com.picpay.backend.services;

import br.com.picpay.backend.config.WebFluxCustomConfig;
import br.com.picpay.backend.data.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Import(WebFluxCustomConfig.class)
@RequiredArgsConstructor
public class NotificationService {

    @Value("${app.notificationEndpoint}")
    private String notificationEndpoint;

    private final WebClient.Builder webClientBuilder;

    private final UserService userService;

    public void notifyTransfer(User user)
    {
        var client = webClientBuilder
                .baseUrl(notificationEndpoint)
                .build();

        var uriSpec = client.method(HttpMethod.POST);

        var headerSpec = uriSpec.uri("/notify");

        ResponseEntity<Void> responseSpec = headerSpec
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}

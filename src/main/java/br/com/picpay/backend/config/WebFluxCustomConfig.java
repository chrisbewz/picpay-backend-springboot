package br.com.picpay.backend.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebFluxCustomConfig {

    private static final Integer defaultRequestTimeoutSeconds = 5;
    @Bean
    @Scope("prototype")
    public HttpClient getHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, defaultRequestTimeoutSeconds * 1000)
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(defaultRequestTimeoutSeconds)));
    }

    @Bean
    @Scope("prototype")
    public WebClient.Builder webClientBuilder(HttpClient httpClient) {
        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
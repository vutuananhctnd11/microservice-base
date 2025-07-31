package com.example.order_service.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Slf4j
public class WebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder()
                .filter(((request, next) -> {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    if (auth != null && auth.getCredentials() != null) {
                        String token = auth.getCredentials().toString();
                        ClientRequest newRequest = ClientRequest.from(request)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " +token)
                                .build();
                        return next.exchange(newRequest);
                    } else {
                        log.info("============No token transmitted============");
                        return next.exchange(request);
                    }
                }));
    }
}

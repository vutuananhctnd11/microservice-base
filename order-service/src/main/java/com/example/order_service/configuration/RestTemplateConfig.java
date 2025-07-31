package com.example.order_service.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@Slf4j
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.getCredentials() != null) {
                String token = auth.getCredentials().toString();
                request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            } else {
                log.warn("Không có token trong SecurityContextHolder");
            }
            return execution.execute(request, body);
        }));

        return restTemplate;
    }
}

package hs.aalen.financial_assets_portfolio.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor apiKeyInterceptor() {
        return requestTemplate -> {
            String apiKey = "Team#1-ApiKey-Q1YlLqYsv1";
            requestTemplate.header("Api-Key", apiKey);
        };
    }
}
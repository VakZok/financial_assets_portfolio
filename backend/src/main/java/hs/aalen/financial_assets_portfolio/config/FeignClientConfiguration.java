package hs.aalen.financial_assets_portfolio.config;

import feign.RequestInterceptor;
import feign.Retryer;
import hs.aalen.financial_assets_portfolio.client.NaiveRetryer;
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

    @Bean
    public Retryer retryer() {
        return new NaiveRetryer();
    }

}
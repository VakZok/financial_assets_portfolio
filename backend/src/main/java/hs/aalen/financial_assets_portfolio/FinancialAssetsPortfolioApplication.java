package hs.aalen.financial_assets_portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("hs.aalen.*")
@EnableJpaRepositories("hs.aalen.*")
public class FinancialAssetsPortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialAssetsPortfolioApplication.class, args);
	}

}

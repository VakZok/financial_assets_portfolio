package hs.aalen.financial_assets_portfolio.persistence;


import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import org.springframework.data.repository.ListCrudRepository;


public interface PortfolioItemRepository extends ListCrudRepository<PortfolioItem, Long> {
}



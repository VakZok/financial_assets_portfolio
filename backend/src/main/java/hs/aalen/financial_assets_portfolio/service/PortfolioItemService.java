package hs.aalen.financial_assets_portfolio.service;


import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.persistence.PortfolioItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PortfolioItemService {
    private final PortfolioItemRepository portfolioItemRepository;

    public PortfolioItemService(PortfolioItemRepository portfolioItemRepository) {
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public PortfolioItem getPortfolioItem(Long id)throws NoSuchElementException {
        Optional<PortfolioItem> item = portfolioItemRepository.findById(id);
        if (item.isPresent()){
            return item.get();
        }else {
            throw new NoSuchElementException("Portfolioitem wurde nicht gefunden");
        }
    }

    public PortfolioItem savePortfolioItem(PortfolioItem portfolioItem){
        return portfolioItemRepository.save(portfolioItem);
    }

    public List<PortfolioItem> getPortfolioItemList(){
        return portfolioItemRepository.findAll();
    }
}

package hs.aalen.financial_assets_portfolio.service;


import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import hs.aalen.financial_assets_portfolio.data.PItemPreviewDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.exceptions.ShareAlreadyExistsException;
import hs.aalen.financial_assets_portfolio.persistence.PortfolioItemRepository;
import hs.aalen.financial_assets_portfolio.persistence.ShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PortfolioItemService {
    @Autowired
    private PortfolioItemRepository portfolioItemRepository;
    @Autowired
    private ShareRepository shareRepository;
    @Autowired
    private ShareService shareService = new ShareService(shareRepository);

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

    public void addPortfolioItem(PItemDTO pItemDTO){
        ShareDTO shareDTO = pItemDTO.getShareDTO();
        Share share = new Share(shareDTO);
        if(!(shareService.checkShareExists(share))){
            shareRepository.save(share);
            PortfolioItem pItem = new PortfolioItem(pItemDTO);
            System.out.println(pItem.getId());
            portfolioItemRepository.save(pItem);

        }else {
            PortfolioItem pItem = new PortfolioItem(pItemDTO);
            portfolioItemRepository.save(pItem);
        }
    }

    public List<PortfolioItem> getPortfolioItemList(){
        return portfolioItemRepository.findAll();
    }
}

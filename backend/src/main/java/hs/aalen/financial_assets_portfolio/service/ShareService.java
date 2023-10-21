package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.exceptions.PortfolioItemException;
import hs.aalen.financial_assets_portfolio.exceptions.ShareException;
import hs.aalen.financial_assets_portfolio.persistence.ShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShareService {

    @Autowired
    private ShareRepository shareRepository;

    public ShareService(ShareRepository shareRepository) {
        this.shareRepository = shareRepository;
    }

    public Share saveShare(Share share){
        return shareRepository.save(share);
    }

    public List<Share> getShareList(){
        return shareRepository.findAll();
    }

    public Share getShare(String wkn){
        return shareRepository.findByWkn(wkn);
    }

    public void addShare(ShareDTO shareDTO) {
        Share share = new Share(shareDTO);
        if(!(checkShareExists(share))) {
            shareRepository.save(share);
        } else{
            Share newShare = new Share(shareDTO);
            shareRepository.save(newShare);
        }
    }

    public boolean checkShareExists(Share share){
        Share shareElement = shareRepository.findByWkn(share.getWkn());
        if(shareElement != null){
            return true;
        }else {
            return false;
        }

    }
}

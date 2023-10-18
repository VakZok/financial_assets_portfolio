package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.persistence.ShareRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShareService {
    private final ShareRepository shareRepository;

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
}

package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.domain.Share;
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

    public boolean checkShareExists(Share share){
        System.out.println("xx");
        Share shareElement = shareRepository.findByWkn(share.getWkn());
        if(shareElement != null){
            System.out.println(shareElement.getWkn());
            return true;
        } else{
            return false;
        }

    }
}

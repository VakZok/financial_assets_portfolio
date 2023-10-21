package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.ShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShareService {

    public static final int WKN_LENGTH = 6;

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

    public void addShare(ShareDTO shareDTO) throws FormNotValidException {
        ArrayList<ExceptionDTO> exceptions = validateForm(shareDTO);
        if(exceptions.size() == 0){
            Share share = new Share(shareDTO);
            shareRepository.save(share);
        } else {
            throw new FormNotValidException("Formfehler", exceptions);
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

    public ArrayList<ExceptionDTO> validateForm(ShareDTO shareDTO){
        ArrayList<ExceptionDTO> exceptions = new ArrayList<ExceptionDTO>();
        if (shareDTO.getWkn().length() != WKN_LENGTH ){
            exceptions.add(new ExceptionDTO("wkn", "Die WKN muss exakt 6 Zeichen enthalten."));
        }
        if(shareDTO.getWkn() == null || shareDTO.getWkn().length() == 0){
            exceptions.add(new ExceptionDTO("wkn", "Die WKN darf nicht leer sein."));
        }
        if(shareDTO.getName() == null || shareDTO.getName().length() == 0){
            exceptions.add(new ExceptionDTO("name", "Der Name darf nicht leer sein."));
        }
        if(shareDTO.getCategory() == null || shareDTO.getCategory().length() == 0){
            exceptions.add(new ExceptionDTO("cat", "Die Kategorie darf nicht leer sein."));
        }
        if(shareDTO.getDescription() == null || shareDTO.getDescription().length() == 0){
            exceptions.add(new ExceptionDTO("description", "Die Beschreibung darf nicht leer sein."));
        }
        if(shareDTO.getDescription().length() >= 255){
            exceptions.add(new ExceptionDTO(
                    "description", "Die Beschreibung darf nicht mehr als 255 Zeichen beeinhalten."));
        }
        return exceptions;
    }
}

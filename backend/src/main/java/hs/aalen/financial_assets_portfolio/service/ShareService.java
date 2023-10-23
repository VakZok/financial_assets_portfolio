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
        if(exceptions.isEmpty()){
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
            exceptions.add(new ExceptionDTO("wkn", "Die WKN muss aus 6 Zeichen bestehen."));
        }
        if(shareDTO.getWkn() == null || shareDTO.getWkn().isEmpty()){
            exceptions.add(new ExceptionDTO("wkn", "Bitte füllen Sie die WKN aus"));
        }
        if(shareDTO.getName() == null || shareDTO.getName().isEmpty()){
            exceptions.add(new ExceptionDTO("name", "Bitte tragen Sie einen Namen ein"));
        }
        if(shareDTO.getCategory() == null || shareDTO.getCategory().isEmpty()){
            exceptions.add(new ExceptionDTO("cat", "Bitte wählen Sie eine Kategorie"));
        }
        if(shareDTO.getDescription() == null || shareDTO.getDescription().isEmpty()){
            exceptions.add(new ExceptionDTO("description", "Bitte tragen Sie die Beschreibung ein"));
        }
        if(shareDTO.getDescription().length() >= 255){
            exceptions.add(new ExceptionDTO(
                    "description", "Die Beschreibung darf nicht länger als 255 Zeichen sein"));
        }
        return exceptions;
    }
}

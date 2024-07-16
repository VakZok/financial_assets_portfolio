package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.persistence.ShareRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShareService {
    /** Service class that is used to process
     * share objects.
     * ISIN_LENGTH:       String Length of ISIN
     * STRING_MAX_LENGTH: Max length of strings used in share objects
     *
     */


    public static final int ISIN_LENGTH = 12;
    public static final int STRING_MAX_LENGTH = 255;
    private final ShareRepository shareRepository;


    /* CONSTRUCTOR */

    public ShareService(ShareRepository shareRepository) {
        this.shareRepository = shareRepository;
    }


    /* Method that returns the share list */
    public ArrayList<ShareDTO> getShareList()throws NoSuchElementException{
        List<Share> shareList = shareRepository.findAll();
        if (shareList.isEmpty()){
            throw new NoSuchElementException();
        }
        return new ArrayList<>(shareList.stream().map(ShareDTO::new).toList());
    }

    /* Method that returns the share searched by isin */
    public ShareDTO getShare(String isin) throws NoSuchElementException{
        Share share = shareRepository.findByIsin(isin.toUpperCase());
        if (share == null){
            throw new NoSuchElementException();
        }
        return new ShareDTO(share);
    }

    public void saveShare(ShareDTO shareDTO){
        Share share = new Share(shareDTO);
        this.shareRepository.save(share);
    }

    /* Method that checks if the Portfolio Item already exists */
    public boolean checkShareExists(String isin){
        return this.shareRepository.existsByIsin(isin.toUpperCase());
    }

    /* Method that checks the validity of the input */
    public ArrayList<ExceptionDTO> validateForm(ShareDTO shareDTO){
        ArrayList<ExceptionDTO> exceptions = new ArrayList<>();

        if (checkShareExists(shareDTO.getIsin())){
            exceptions.add(new ExceptionDTO("isin", "ISIN bereits vorhanden"));
        }
        if (shareDTO.getIsin().length() > ISIN_LENGTH ){
            exceptions.add(new ExceptionDTO("isin", "Die ISIN darf maximal aus 12 Zeichen bestehen"));
        }
        if(shareDTO.getIsin() == null || shareDTO.getIsin().isEmpty()){
            exceptions.add(new ExceptionDTO("isin", "Bitte füllen Sie die ISIN aus"));
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
        if(shareDTO.getDescription().length() > STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "description", "Die Beschreibung darf nicht länger als 255 Zeichen sein"));
        }
        if(shareDTO.getName().length() > STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "name", "Der Name darf nicht länger als 255 Zeichen sein"));
        }
        if(shareDTO.getCategory().length() > STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "cat", "Die Kategorie darf nicht länger als 255 Zeichen sein"));
        }
        return exceptions;
    }
}

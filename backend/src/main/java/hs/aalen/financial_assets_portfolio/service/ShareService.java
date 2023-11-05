package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.ShareRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShareService {
    /* Share service class to process
     * the requests received in the controller class
     */

    /* CONSTANTS */
    public static final int WKN_LENGTH = 6;
    public static final int STRING_MAX_LENGTH = 255;

    /* CONNECTED REPOSITORIES */
    private final ShareRepository shareRepository;

    /* PROCESSING METHODS */
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

    /* Method that returns the share searched by the wkn */
    public ShareDTO getShare(String wkn) throws NoSuchElementException{
        Share share = shareRepository.findByWkn(wkn.toUpperCase());
        if (share == null){
            throw new NoSuchElementException();
        }
        return new ShareDTO(share);
    }

    /* Method that adds a new share when the form is correct */
    public void addShare(ShareDTO shareDTO) throws FormNotValidException, DataIntegrityViolationException {
        // check if share exists. If not, validate the form. Raise Exception if share already exists
        // or if form is invalid
        if(!this.checkShareExists(shareDTO.getWkn())){
            ArrayList<ExceptionDTO> exceptions = validateForm(shareDTO);
            if(exceptions.isEmpty()){
                Share share = new Share(shareDTO);
                shareRepository.save(share);
            } else {
                throw new FormNotValidException("Formfehler", exceptions);
            }
        } else {
            throw new DataIntegrityViolationException("Portfolio-Item mit dieser WKN bereits vorhanden");
        }
    }

    /* Method that changes a share when the form is correct */
    public void updateShare(String wkn, ShareDTO shareDTO) throws FormNotValidException{
        ArrayList<ExceptionDTO> exceptions = validateForm(shareDTO);
        if(exceptions.isEmpty()){
            Share share = shareRepository.findByWkn(wkn);
            Share shareNew = new Share(shareDTO, share);
            shareRepository.save(shareNew);
        } else {
            throw new FormNotValidException("Formfehler", exceptions);
        }
    }

    /* Method that checks if the share already exists */
    public boolean checkShareExists(String wkn){
        return shareRepository.existsByWkn(wkn.toUpperCase());
    }

    /* Method that checks the validity of the input */
    public ArrayList<ExceptionDTO> validateForm(ShareDTO shareDTO){

        ArrayList<ExceptionDTO> exceptions = new ArrayList<>();

        if (shareDTO.getWkn().length() > WKN_LENGTH ){
            exceptions.add(new ExceptionDTO("wkn", "Die WKN darf maximal aus 6 Zeichen bestehen."));
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

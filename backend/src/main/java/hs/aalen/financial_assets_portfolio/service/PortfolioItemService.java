package hs.aalen.financial_assets_portfolio.service;


import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.PortfolioItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PortfolioItemService {
    /* Portfolio item service class to process
     *  the requests received in the controller class
     */

    /* CONSTANTS */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final LocalDate MIN_DATE = LocalDate.of(1903,4,22);
    private static final LocalDate MAX_DATE = LocalDate.of(2123,12,31);
    private static final int WKN_LENGTH = 6;
    private static final int STRING_MAX_LENGTH = 255;

    /* CONNECTED REPOSITORIES AND SERVICES */
    private final PortfolioItemRepository portfolioItemRepository;


    /* PROCESSING METHODS */
    public PortfolioItemService(PortfolioItemRepository portfolioItemRepository) {
        this.portfolioItemRepository = portfolioItemRepository;
    }

    /* Method that returns the portfolio item searched by the id */
    public PortfolioItem getPortfolioItem(Long id)throws NoSuchElementException {
        Optional<PortfolioItem> item = portfolioItemRepository.findById(id);
        if (item.isPresent()){
            return item.get();
        }else {
            throw new NoSuchElementException("Portfolioitem wurde nicht gefunden");
        }
    }

    /* Method that adds a new portfolio item when the form is correct */
    public void addPortfolioItem(PItemDTO pItemDTO) throws FormNotValidException {
        ArrayList<ExceptionDTO> exceptions = formIsValid(pItemDTO);
        if(exceptions.isEmpty()){
            PortfolioItem pItem = new PortfolioItem(pItemDTO);
            portfolioItemRepository.save(pItem);
        }else {
            throw new FormNotValidException("Formfehler", exceptions);
        }
    }

    /* Method that returns the portfolio item list */
    public ArrayList<PItemDTO> getPortfolioItemPreviewList(){
        ArrayList<PItemDTO> pItemPrevListDTO = new ArrayList<PItemDTO>();
        for (PortfolioItem pItem : portfolioItemRepository.findAll()) {
            PItemDTO pItemDTO = new PItemDTO();
            pItemDTO.setId(pItem.getId());
            pItemDTO.setWkn(pItem.getWkn());
            pItemDTO.setName(pItem.getName());
            pItemDTO.setPurchasePrice(pItem.getPurchasePrice());
            pItemDTO.setQuantity(pItem.getQuantity());
            pItemPrevListDTO.add(pItemDTO);
        }
        return pItemPrevListDTO;
    }

    /* Method that checks the validity of the input */
    public ArrayList<ExceptionDTO> formIsValid(PItemDTO pItemDTO){
        ArrayList<ExceptionDTO> exceptions = new ArrayList<>();
        if(pItemDTO.getPurchasePrice() == 0){
            exceptions.add(new ExceptionDTO("purchasePrice", "Bitte tragen Sie einen Kaufpreis ein"));
        }
        if(pItemDTO.getQuantity() == 0){
            exceptions.add(new ExceptionDTO("quantity", "Bitte tragen Sie eine Anzahl ein"));
        }
        if(pItemDTO.getPurchaseDate() == null){
            exceptions.add(new ExceptionDTO(
                    "purchaseDate", "Bitte tragen Sie ein Kaufdatum ein"));
        }else if(pItemDTO.getPurchaseDate().isBefore(MIN_DATE)) {
            exceptions.add(new ExceptionDTO(
                    "purchaseDate", "Das Kaufdatum muss nach dem " + MIN_DATE.format(FORMATTER) + " liegen"));
        } else if(pItemDTO.getPurchaseDate().isAfter(MAX_DATE)){
            exceptions.add(new ExceptionDTO(
                    "purchaseDate", "Das Kaufdatum muss vor dem " + MAX_DATE.format(FORMATTER) + " liegen"));
        }
        if (pItemDTO.getWkn().length() != WKN_LENGTH ){
            exceptions.add(new ExceptionDTO("wkn", "Die WKN muss aus 6 Zeichen bestehen."));
        }
        if(pItemDTO.getWkn() == null || pItemDTO.getWkn().isEmpty()){
            exceptions.add(new ExceptionDTO("wkn", "Bitte füllen Sie die WKN aus"));
        }
        if(pItemDTO.getName() == null || pItemDTO.getName().isEmpty()){
            exceptions.add(new ExceptionDTO("name", "Bitte tragen Sie einen Namen ein"));
        }
        if(pItemDTO.getCategory() == null || pItemDTO.getCategory().isEmpty()){
            exceptions.add(new ExceptionDTO("category", "Bitte wählen Sie eine Kategorie"));
        }
        if(pItemDTO.getDescription() == null || pItemDTO.getDescription().isEmpty()){
            exceptions.add(new ExceptionDTO("description", "Bitte tragen Sie die Beschreibung ein"));
        }
        if(pItemDTO.getDescription().length() >= STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "description", "Die Beschreibung darf nicht länger als 255 Zeichen sein"));
        }
        if(pItemDTO.getName().length() >= STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "name", "Der Name darf nicht länger als 255 Zeichen sein"));
        }
        if(pItemDTO.getCategory().length() >= STRING_MAX_LENGTH){
            exceptions.add(new ExceptionDTO(
                    "cat", "Die Kategorie darf nicht länger als 255 Zeichen sein"));
        }
        return exceptions;
    }

}

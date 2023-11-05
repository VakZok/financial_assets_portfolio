package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.PItemAggDTO;
import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.PortfolioItemRepository;
import org.springframework.dao.DataIntegrityViolationException;
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


    /* CONNECTED REPOSITORIES AND SERVICES */
    private final PortfolioItemRepository portfolioItemRepository;
    private final ShareService shareService;


    /* PROCESSING METHODS */
    public PortfolioItemService(PortfolioItemRepository portfolioItemRepository, ShareService shareService) {
        this.portfolioItemRepository = portfolioItemRepository;
        this.shareService = shareService;
    }

    /* Method that returns the portfolio item searched by the id */
    public PItemDTO getPortfolioItem(Long id)throws NoSuchElementException {
        Optional<PortfolioItem> item = portfolioItemRepository.findById(id);
        if (item.isPresent()){
            return new PItemDTO(item.get());
        }else {
            throw new NoSuchElementException("Portfolioitem wurde nicht gefunden");
        }
    }

    public PItemAggDTO getWKNAggPItem(String wkn)throws NoSuchElementException {
        return aggregatePItems(wkn);
    }

    /* Method that adds a new portfolio item when the form is correct */
    public void addPortfolioItem(PItemDTO pItemDTO) throws FormNotValidException, DataIntegrityViolationException {
        ShareDTO shareDTO = pItemDTO.getShareDTO();
        try{
            try{
                ArrayList<ExceptionDTO> pItemExceptionList = validateForm(pItemDTO);
                if (!pItemExceptionList.isEmpty()){
                    throw new FormNotValidException("Formfehler", pItemExceptionList);
                }
            } catch(FormNotValidException e){
                ArrayList<ExceptionDTO> pItemExceptionList = e.getExceptions();
                pItemExceptionList.addAll(this.shareService.validateForm(shareDTO));
                throw new FormNotValidException("Formfehler", pItemExceptionList);
            }
            try {
                shareService.addShare(pItemDTO.getShareDTO());
                PortfolioItem pItem = new PortfolioItem(pItemDTO);
                portfolioItemRepository.save(pItem);
            } catch (FormNotValidException e) {
                ArrayList<ExceptionDTO> shareExceptionList = e.getExceptions();
                throw new FormNotValidException("Formfehler", shareExceptionList);
            }
        } catch(DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }


    /* Method that returns the portfolio item list */
    public ArrayList<PItemDTO> getPortfolioItemList() throws NoSuchElementException{
        List<PortfolioItem> pItemList = portfolioItemRepository.findAll();
        if (pItemList.isEmpty()) {
            throw new NoSuchElementException();
        }
        return new ArrayList<PItemDTO>(pItemList.stream().map(PItemDTO::new).toList());
    }

    public List<PItemAggDTO> getWKNAggPItemsPreview(){
        ArrayList<ShareDTO> shareDTOList = shareService.getShareList();
        return new ArrayList<>(
                shareDTOList.stream().map(share -> aggregatePItems(share.getWkn())).toList());
    }

    /* Method that checks the validity of the input */
    public ArrayList<ExceptionDTO> validateForm(PItemDTO pItemDTO){
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
        return exceptions;
    }

    public PItemAggDTO aggregatePItems(String wkn){
        ShareDTO shareDTO = shareService.getShare(wkn);
        ArrayList<PortfolioItem> pItemList = portfolioItemRepository.findAllByShare_Wkn(wkn);
        ArrayList<PItemDTO> pItemDTOList = new ArrayList<>(pItemList.stream().map(PItemDTO::new).toList());
        double totalSum = pItemList.stream()
                .mapToDouble(pItem -> pItem.getQuantity() * pItem.getPurchasePrice()).sum();
        int totalQuantity = pItemList.stream()
                .mapToInt(PortfolioItem::getQuantity).sum();
        return new PItemAggDTO(shareDTO, totalSum/totalQuantity, totalQuantity, pItemDTOList);
    }

}

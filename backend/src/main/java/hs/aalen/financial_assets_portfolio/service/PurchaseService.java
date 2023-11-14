package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.PortfolioItemDTO;
import hs.aalen.financial_assets_portfolio.data.PurchaseDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.Purchase;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.PurchaseRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PurchaseService {
    /* Portfolio item service class to process
     *  the requests received in the controller class
     */

    /* CONSTANTS */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final LocalDate MIN_DATE = LocalDate.of(1903,4,22);
    private static final LocalDate MAX_DATE = LocalDate.of(2123,12,31);


    /* CONNECTED REPOSITORIES AND SERVICES */
    private final PurchaseRepository purchaseRepository;
    private final ShareService shareService;


    /* PROCESSING METHODS */
    public PurchaseService(PurchaseRepository purchaseRepository, ShareService shareService) {
        this.purchaseRepository = purchaseRepository;
        this.shareService = shareService;
    }

    /* Method that returns the portfolio item searched by the id */
    public PurchaseDTO getPurchase(Long id)throws NoSuchElementException {
        Optional<Purchase> item = purchaseRepository.findById(id);
        if (item.isPresent()){
            return new PurchaseDTO(item.get());
        }else {
            throw new NoSuchElementException("Portfolioitem wurde nicht gefunden");
        }
    }

    public PortfolioItemDTO getPItemByWKN(String wkn)throws NoSuchElementException {
        return aggregatePItem(wkn);
    }

    /* Method that adds a new portfolio item when the form is correct */
    public void addPurchase(PurchaseDTO purchaseDTO) throws FormNotValidException, DataIntegrityViolationException {
        ShareDTO shareDTO = purchaseDTO.getShareDTO();
        try{
            try{
                ArrayList<ExceptionDTO> purchaseExceptionList = validateForm(purchaseDTO);
                if (!purchaseExceptionList.isEmpty()){
                    throw new FormNotValidException("Formfehler", purchaseExceptionList);
                }
            } catch(FormNotValidException e){
                ArrayList<ExceptionDTO> purchaseExceptionList = e.getExceptions();
                purchaseExceptionList.addAll(this.shareService.validateForm(shareDTO));
                throw new FormNotValidException("Formfehler", purchaseExceptionList);
            }
            try {
                shareService.addShare(purchaseDTO.getShareDTO());
                Purchase pItem = new Purchase(purchaseDTO);
                purchaseRepository.save(pItem);
            } catch (FormNotValidException e) {
                ArrayList<ExceptionDTO> shareExceptionList = e.getExceptions();
                throw new FormNotValidException("Formfehler", shareExceptionList);
            }
        } catch(DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }
    }


    /* Method that returns the portfolio item list */
    public ArrayList<PurchaseDTO> getPurchaseList() throws NoSuchElementException{
        List<Purchase> purchaseList = purchaseRepository.findAll();
        if (purchaseList.isEmpty()) {
            throw new NoSuchElementException();
        }
        return new ArrayList<PurchaseDTO>(purchaseList.stream().map(PurchaseDTO::new).toList());
    }

    public List<PortfolioItemDTO> getPItemsPreview(){
        ArrayList<ShareDTO> shareDTOList = shareService.getShareList();
        return new ArrayList<>(
                shareDTOList.stream().map(share -> aggregatePItem(share.getWkn())).toList());
    }

    /* Method that checks the validity of the input */
    public ArrayList<ExceptionDTO> validateForm(PurchaseDTO purchaseDTO){
        ArrayList<ExceptionDTO> exceptions = new ArrayList<>();
        if(purchaseDTO.getPurchasePrice() == 0){
            exceptions.add(new ExceptionDTO("purchasePrice", "Bitte tragen Sie einen Kaufpreis ein"));
        }
        if(purchaseDTO.getQuantity() == 0){
            exceptions.add(new ExceptionDTO("quantity", "Bitte tragen Sie eine Anzahl ein"));
        }
        if(purchaseDTO.getPurchaseDate() == null){
            exceptions.add(new ExceptionDTO(
                    "purchaseDate", "Bitte tragen Sie ein Kaufdatum ein"));
        }else if(purchaseDTO.getPurchaseDate().isBefore(MIN_DATE)) {
            exceptions.add(new ExceptionDTO(
                    "purchaseDate", "Das Kaufdatum muss nach dem " + MIN_DATE.format(FORMATTER) + " liegen"));
        } else if(purchaseDTO.getPurchaseDate().isAfter(MAX_DATE)){
            exceptions.add(new ExceptionDTO(
                    "purchaseDate", "Das Kaufdatum muss vor dem " + MAX_DATE.format(FORMATTER) + " liegen"));
        }
        return exceptions;
    }

    public PortfolioItemDTO aggregatePItem(String wkn){
        ShareDTO shareDTO = shareService.getShare(wkn);
        ArrayList<Purchase> purchaseList = purchaseRepository.findAllByShare_Wkn(wkn);
        ArrayList<PurchaseDTO> purchaseDTOList = new ArrayList<>(purchaseList.stream().map(PurchaseDTO::new).toList());
        double totalSum = purchaseList.stream()
                .mapToDouble(pItem -> pItem.getQuantity() * pItem.getPurchasePrice()).sum();
        int totalQuantity = purchaseList.stream()
                .mapToInt(Purchase::getQuantity).sum();
        return new PortfolioItemDTO(shareDTO, totalSum/totalQuantity, totalQuantity, purchaseDTOList);
    }

}

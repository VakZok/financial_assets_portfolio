package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.PurchaseDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.Purchase;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.PurchaseRepository;
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

    /* Method that returns the purchase searched by the id */
    public PurchaseDTO getPurchase(Long id)throws NoSuchElementException {
        Optional<Purchase> item = this.purchaseRepository.findById(id);
        if (item.isPresent()){
            return new PurchaseDTO(item.get());
        }else {
            throw new NoSuchElementException("Kauf wurde nicht gefunden");
        }
    }

    public ArrayList<PurchaseDTO> getPurchases(String wkn){
        return  new ArrayList<>(purchaseRepository.findAllByShare_Wkn(wkn).stream().map(PurchaseDTO::new).toList());
    }

    /* Method that returns the portfolio item list */
    public ArrayList<PurchaseDTO> getPurchaseList() throws NoSuchElementException{
        List<Purchase> purchaseList = purchaseRepository.findAll();
        if (purchaseList.isEmpty()) {
            throw new NoSuchElementException();
        }
        return new ArrayList<>(purchaseList.stream().map(PurchaseDTO::new).toList());
    }

    public void savePurchase(PurchaseDTO purchaseDTO){
        Purchase purchase = new Purchase(purchaseDTO);
        this.purchaseRepository.save(purchase);
    }

    public void addNewPurchase(String wkn, PurchaseDTO purchaseDTO) throws FormNotValidException{
        ArrayList<ExceptionDTO> exceptionDTOList = this.validateForm(purchaseDTO);
        exceptionDTOList.addAll(this.validateForm(purchaseDTO));
        if(exceptionDTOList.isEmpty()){
            ShareDTO shareDTO = this.shareService.getShare(wkn);
            purchaseDTO.setShareDTO(shareDTO);
            savePurchase(purchaseDTO);
        } else {
            throw new FormNotValidException("Formfehler", exceptionDTOList);
        }
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

}

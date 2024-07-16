package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.client.ShareSwaggerClient;
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
    /** Portfolio item service class to process
     *  the requests received in the controller class
     *  FORMATTER:          Date time formatter used to transfer date to german format
     *  MIN_DATE:           Smallest possible date
     *  MAX_DATE:           Largest possible date
     *  purchaseRepository: Purchase repository object connected with the service
     *  swaggerClient:      Client used to request prices from external API
     */

    /* CONSTANTS */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final LocalDate MIN_DATE = LocalDate.of(1903,4,22);
    private static final LocalDate MAX_DATE = LocalDate.of(2123,12,31);



    private final PurchaseRepository purchaseRepository;
    private final ShareSwaggerClient swaggerClient;

    /* CONSTRUCTOR */

    public PurchaseService(
            PurchaseRepository purchaseRepository,
            ShareSwaggerClient swaggerClient) {

        this.purchaseRepository = purchaseRepository;
        this.swaggerClient = swaggerClient;
    }


    /* METHODS */

    /* Method that returns the purchase searched by the id */
    public PurchaseDTO getPurchase(Long id)throws NoSuchElementException {
        Optional<Purchase> item = this.purchaseRepository.findById(id);
        if (item.isPresent()){
            return new PurchaseDTO(item.get());
        }else {
            throw new NoSuchElementException("Kauf wurde nicht gefunden");
        }
    }

    public ArrayList<PurchaseDTO> getPurchases(String isin){
        return  new ArrayList<>(purchaseRepository.findAllByShare_Isin(isin).stream().map(PurchaseDTO::new).toList());
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

    public void addNewPurchase(String isin, PurchaseDTO purchaseDTO) throws FormNotValidException{
        ArrayList<ExceptionDTO> exceptionDTOList = this.validateForm(purchaseDTO);
        if(exceptionDTOList.isEmpty()){
            ShareDTO shareDTO = new ShareDTO(this.swaggerClient.getShare(isin));
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
        if(purchaseDTO.getQuantity() >= 1000000) {
            exceptions.add(new ExceptionDTO("quantity", "Maximale Anzahl: 1 Mio. Stück."));
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

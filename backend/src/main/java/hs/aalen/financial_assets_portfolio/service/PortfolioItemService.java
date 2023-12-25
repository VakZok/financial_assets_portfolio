package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.client.ShareSwaggerClient;
import hs.aalen.financial_assets_portfolio.data.*;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class PortfolioItemService {


    private PurchaseService purchaseService;
    private ShareService shareService;
    private ShareSwaggerClient shareSwaggerClient;

    @Autowired
    public PortfolioItemService(ShareSwaggerClient shareSwaggerClient) {
        this.shareSwaggerClient = shareSwaggerClient;
    }

    public PortfolioItemDTO getPItemSwagger(String isin) {
        ShareSwaggerDTO shareSwaggerDTO = shareSwaggerClient.getShare(isin);
        PortfolioItemDTO portfolioItemDTO = new PortfolioItemDTO(shareSwaggerDTO);
        return portfolioItemDTO;
    }

    public PortfolioItemService(PurchaseService purchaseService, ShareService shareService) {
        this.purchaseService = purchaseService;
        this.shareService = shareService;
    }

    public PortfolioItemDTO getPItemByISIN(String isin)throws NoSuchElementException {
        if (this.shareService.checkShareExists(isin)) {
            return aggregatePItem(this.shareService.getShare(isin));
        } else {
            throw new NoSuchElementException();
        }
    }

    public ArrayList<PortfolioItemDTO> getPItemsPreview(){
        ArrayList<ShareDTO> shareDTOList = this.shareService.getShareList();
        return new ArrayList<>(shareDTOList.stream().map(this::aggregatePItem).toList());
    }

    public void addNewPItem(PurchaseDTO purchaseDTO) throws FormNotValidException{
        ShareDTO shareDTO = purchaseDTO.getShareDTO();
        ArrayList<ExceptionDTO> exceptionList = new ArrayList<>();
        exceptionList.addAll(shareService.validateForm(shareDTO));
        exceptionList.addAll(purchaseService.validateForm(purchaseDTO));
        if (exceptionList.isEmpty()){
            shareService.saveShare(shareDTO);
            purchaseService.savePurchase(purchaseDTO);
        } else {
            throw new FormNotValidException("Formfehler", exceptionList);
        }
    }


    public PortfolioItemDTO aggregatePItem(ShareDTO shareDTO){
        ArrayList<PurchaseDTO> purchaseDTOList = this.purchaseService.getPurchases(shareDTO.getIsin());

        // create a double stream of price*quantity and sum it up to get total price
        double totalPrice = purchaseDTOList.stream()
                .mapToDouble(pItem -> pItem.getQuantity() * pItem.getPurchasePrice()).sum();

        // create an int stream of quantities and sum them up to get total quantity
        int totalQuantity = purchaseDTOList.stream()
                .mapToInt(PurchaseDTO::getQuantity).sum();

        double avgPrice = totalPrice / totalQuantity;

        return new PortfolioItemDTO(shareDTO, avgPrice, totalPrice, totalQuantity, purchaseDTOList);
    }
}

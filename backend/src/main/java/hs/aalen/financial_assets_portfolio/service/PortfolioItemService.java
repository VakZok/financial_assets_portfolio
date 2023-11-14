package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.PortfolioItemDTO;
import hs.aalen.financial_assets_portfolio.data.PurchaseDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class PortfolioItemService {


    private final PurchaseService purchaseService;
    private final ShareService shareService;

    public PortfolioItemService(PurchaseService purchaseService, ShareService shareService) {
        this.purchaseService = purchaseService;
        this.shareService = shareService;
    }

    public PortfolioItemDTO getPItemByWKN(String wkn)throws NoSuchElementException {
        if (this.shareService.checkShareExists(wkn)) {
            return aggregatePItem(this.shareService.getShare(wkn));
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
        ArrayList<PurchaseDTO> purchaseDTOList = this.purchaseService.getPurchases(shareDTO.getWkn());

        double totalSum = purchaseDTOList.stream()
                .mapToDouble(pItem -> pItem.getQuantity() * pItem.getPurchasePrice()).sum();
        int totalQuantity = purchaseDTOList.stream()
                .mapToInt(PurchaseDTO::getQuantity).sum();
        double avgPrice = totalSum / totalQuantity;

        return new PortfolioItemDTO(shareDTO, avgPrice, totalQuantity, purchaseDTOList);
    }





}

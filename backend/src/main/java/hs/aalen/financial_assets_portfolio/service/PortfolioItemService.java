package hs.aalen.financial_assets_portfolio.service;

import hs.aalen.financial_assets_portfolio.client.ShareSwaggerClient;
import hs.aalen.financial_assets_portfolio.data.*;
import hs.aalen.financial_assets_portfolio.domain.Account;
import hs.aalen.financial_assets_portfolio.domain.Likes;
import hs.aalen.financial_assets_portfolio.domain.LikesId;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.AccountRepository;
import hs.aalen.financial_assets_portfolio.persistence.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class PortfolioItemService {


    private final PurchaseService purchaseService;
    private final ShareService shareService;
    private final ShareSwaggerClient shareSwaggerClient;
    private final LikesRepository likesRepository;
    private final AccountRepository accountRepository;


    public ArrayList<PortfolioItemDTO> getLikedPItems(String username){
        ArrayList<Share> likedShares = new ArrayList<Share> (this.likesRepository.findAllByAccountUsername(username).stream().map(Likes::getShare).toList());
        ArrayList<ShareDTO> shareDTOList = new ArrayList<ShareDTO> (likedShares.stream().map(ShareDTO::new).toList());
        return new ArrayList<>(shareDTOList.stream().map(this::aggregatePItem).toList());
    }

    public void postLikedPItem(String username, String isin){
        LikesId likesId = new LikesId(username,isin);
        Account account = this.accountRepository.findByUsernameIgnoreCase(username);
        Share share = new Share(this.shareService.getShare(isin));
        Likes likes = new Likes(likesId,account,share);
        this.likesRepository.save(likes);
    }

    public void deleteLikedPItems(String username, String isin){
        LikesId likesId = new LikesId(username,isin);
        if(this.likesRepository.existsById(likesId)){
            this.likesRepository.deleteById(likesId);
        }
    }
    public PortfolioItemDTO getPItemSwagger(String isin) {
        ShareSwaggerDTO shareSwaggerDTO = shareSwaggerClient.getShare(isin);
        PortfolioItemDTO portfolioItemDTO = new PortfolioItemDTO(shareSwaggerDTO);
        return portfolioItemDTO;
    }

    public PortfolioItemService(PurchaseService purchaseService,
                                ShareService shareService,
                                ShareSwaggerClient shareSwaggerClient,
                                LikesRepository likesRepository, AccountRepository accountRepository) {
        this.purchaseService = purchaseService;
        this.shareService = shareService;
        this.shareSwaggerClient = shareSwaggerClient;
        this.likesRepository = likesRepository;
        this.accountRepository = accountRepository;
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

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


    public PortfolioItemService(
            PurchaseService purchaseService,
            ShareService shareService,
            ShareSwaggerClient shareSwaggerClient,
            LikesRepository likesRepository, AccountRepository accountRepository) {

        this.purchaseService = purchaseService;
        this.shareService = shareService;
        this.shareSwaggerClient = shareSwaggerClient;
        this.likesRepository = likesRepository;
        this.accountRepository = accountRepository;
    }


    public ArrayList<PortfolioItemDTO> getLikedPItems(String username){
        ArrayList<Share> likedShares = new ArrayList<>(this.likesRepository.findAllByAccountUsername(username).stream().map(Likes::getShare).toList());
        ArrayList<ShareDTO> shareDTOList = new ArrayList<>(likedShares.stream().map(ShareDTO::new).toList());
        return new ArrayList<>(shareDTOList.stream().map(shareDTO -> aggregatePItem(username, shareDTO)).toList());
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
        return new PortfolioItemDTO(shareSwaggerDTO);
    }

    public PortfolioItemDTO getPItemByISIN(String username, String isin)throws NoSuchElementException {
        if (this.shareService.checkShareExists(isin)) {
            return aggregatePItem(username, this.shareService.getShare(isin));
        } else {
            throw new NoSuchElementException();
        }
    }

    public ArrayList<PortfolioItemDTO> getPItemsPreview(String username ){
        ArrayList<ShareDTO> shareDTOList = this.shareService.getShareList();
        return new ArrayList<>(shareDTOList.stream().map(shareDTO -> aggregatePItem(username, shareDTO)).toList());
    }

    public void addNewPItem(PurchaseDTO purchaseDTO) throws FormNotValidException{
        ShareDTO shareDTO = purchaseDTO.getShareDTO();
        ShareDTO shareDTOSwag = new ShareDTO(this.shareSwaggerClient.getShare(shareDTO.getIsin()));
        ArrayList<ExceptionDTO> exceptionList = new ArrayList<>();
        exceptionList.addAll(shareService.validateForm(shareDTOSwag));
        exceptionList.addAll(purchaseService.validateForm(purchaseDTO));
        if (exceptionList.isEmpty()){
            shareService.saveShare(shareDTOSwag);
            purchaseService.savePurchase(purchaseDTO);
        } else {
            throw new FormNotValidException("Formfehler", exceptionList);
        }
    }

    public PortfolioItemDTO aggregatePItem(String username, ShareDTO shareDTO){
        ArrayList<PurchaseDTO> purchaseDTOList = this.purchaseService.getPurchases(shareDTO.getIsin());
        String isin = shareDTO.getIsin();
        LikesId likesId = new LikesId(username,isin);
        boolean isFavorite = this.likesRepository.existsById(likesId);

        // create a double stream of price*quantity and sum it up to get total price
        double totalPrice = purchaseDTOList.stream()
                .mapToDouble(pItem ->
                        pItem.getQuantity() * pItem.getPurchasePrice()).sum();

        // create an int stream of quantities and sum them up to get total quantity
        int totalQuantity = purchaseDTOList.stream()
                .mapToInt(PurchaseDTO::getQuantity).sum();

        double avgPrice = totalPrice / totalQuantity;
        double currentPrice = this.shareSwaggerClient.getShare(isin).getPrice();
        double profitAndLossCum = currentPrice*totalQuantity - totalPrice;
        double profitAndLoss = currentPrice - avgPrice;

        return new PortfolioItemDTO(shareDTO, avgPrice, totalPrice, totalQuantity,
                purchaseDTOList, 0, 0, isFavorite);
    }
}

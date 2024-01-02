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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

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

    public ArrayList<PortfolioItemDTO> getLikedPItems(String username, boolean includePrice){
        ArrayList<ShareDTO> likedShareDTOList = new ArrayList<>(
                this.likesRepository.findAllByAccountUsername(username)
                        .stream().map(liked -> new ShareDTO(liked.getShare())).toList());

        if (includePrice) {
            ArrayList<ShareSwaggerDTO> shareSwaggerDTOList = getShareSwaggerDTOList(likedShareDTOList);
            return new ArrayList<>(shareSwaggerDTOList.stream().map(shareSwaggerDTO -> getPItemWPL(username, shareSwaggerDTO)).toList());
        } else {
            return new ArrayList<>(likedShareDTOList.stream().map(shareDTO -> aggregatePItem(username, shareDTO)).toList());
        }
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
            return getPItemWPL(username, this.shareSwaggerClient.getShare(isin));
        } else {
            throw new NoSuchElementException();
        }
    }

    public ArrayList<PortfolioItemDTO> getPItemsPreview(String username, boolean includePrice ){
        ArrayList<ShareDTO> shareDTOList = this.shareService.getShareList();

        if (includePrice) {
            ArrayList<ShareSwaggerDTO> shareSwaggerDTOList = getShareSwaggerDTOList(shareDTOList);
            return new ArrayList<>(shareSwaggerDTOList.stream().map(shareSwaggerDTO -> getPItemWPL(username, shareSwaggerDTO)).toList());
        } else {
            return new ArrayList<>(shareDTOList.stream().map(shareDTO -> aggregatePItem(username, shareDTO)).toList());
        }

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
        return new PortfolioItemDTO(shareDTO, avgPrice, totalPrice, totalQuantity,
                purchaseDTOList, isFavorite);
    }

    public PortfolioItemDTO getPItemWPL(String username, ShareSwaggerDTO shareSwaggerDTO){
        ShareDTO shareDTO = new ShareDTO(shareSwaggerDTO);
        PortfolioItemDTO pItemDTO = aggregatePItem(username, shareDTO);
        double currentPrice = shareSwaggerDTO.getPrice();

        double profitAndLossCum = currentPrice * pItemDTO.getTotalQuantity() - pItemDTO.getTotalPrice();
        double profitAndLoss = currentPrice - pItemDTO.getAvgPrice();
        pItemDTO.setCurrentPurchasePrice(currentPrice);
        pItemDTO.setProfitAndLoss(profitAndLoss);
        pItemDTO.setProfitAndLossCum(profitAndLossCum);
        return pItemDTO;

    }

    public ArrayList<ShareSwaggerDTO> getShareSwaggerDTOList(ArrayList<ShareDTO> shareDTOList) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(availableProcessors);
        ArrayList <ShareSwaggerDTO> shareSwaggerDTOList = new ArrayList<>();

        List<Future<ShareSwaggerDTO>> futures = new ArrayList<>();

        for (ShareDTO shareDTO : shareDTOList) {
            Future<ShareSwaggerDTO> future = executor.submit(new FeignHttpRequest(shareDTO.getIsin(), shareSwaggerClient));
            futures.add(future);
        }
        executor.shutdown();
        for (Future<ShareSwaggerDTO> future : futures) {
            try {
                shareSwaggerDTOList.add(future.get()); // This will block until the result is available

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return shareSwaggerDTOList;
    }
}

class FeignHttpRequest implements Callable<ShareSwaggerDTO> {
    private final ShareSwaggerClient client;
    private final String isin;
    public FeignHttpRequest(String isin, ShareSwaggerClient client){
        this.client = client;
        this.isin = isin;
    }
    @Override
    public ShareSwaggerDTO call(){
        return this.client.getShare(isin);
    }
}
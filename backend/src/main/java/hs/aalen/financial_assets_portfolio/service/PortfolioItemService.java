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
    /** The service that manges the main business case of handling PortfolioItems.
     * The service contains methods for processing and creating PortfolioItemDTOs
     * which are used to communicate with the frontend.
     * purchaseService:      The database repository containing purchase objects.
     * shareService:         The service that manages share objects.
     * shareSwaggerClient:   The feign client that is used to get share objects from
     *                       an external API
     * likesRepository:      The repository that manages likes objects
     * accountRepository:    The repository that manages user information
     *
     */


    private final PurchaseService purchaseService;
    private final ShareService shareService;
    private final ShareSwaggerClient shareSwaggerClient;
    private final LikesRepository likesRepository;
    private final AccountRepository accountRepository;


    /* CONSTRUCTOR */
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

    /* METHODS */

    public ArrayList<PortfolioItemDTO> getLikedPItems(String username, boolean includePL){
        /* Method returns the list of liked PortfolioItems
        *  username:  The username associated with the liked PortfolioItems
        *  includePL: A flag, that is indicating whether profit and loss should be included.
        *             The flag is used to speed up loading tables in the frontend. Firstly, tables
        *             are loaded without profit and loss, secondly the profit and loss information
        *             is updated. This improves the UX since it decouples the table loading time from
        *             the response time of the external API.
        * */
        ArrayList<ShareDTO> likedShareDTOList = new ArrayList<>(
                this.likesRepository.findAllByAccountUsername(username)
                        .stream().map(liked -> new ShareDTO(liked.getShare())).toList()
        );
        if (includePL) {
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

    public ArrayList<PortfolioItemDTO> getPItemsPreview(String username, boolean includePL ){
        /* Method returns the preview list of owned PortfolioItems.
         *  username:  The username associated with the liked PortfolioItems
         *  includePL: A flag, that is indicating whether profit and loss should be included.
         *             The flag is used to speed up loading tables in the frontend. Firstly, tables
         *             are loaded without profit and loss, secondly the profit and loss information
         *             is updated. This improves the UX since it decouples the table loading time from
         *             the response time of the external API.
         * */
        ArrayList<ShareDTO> shareDTOList = this.shareService.getShareList();
        if (includePL) {
            ArrayList<ShareSwaggerDTO> shareSwaggerDTOList = getShareSwaggerDTOList(shareDTOList);
            return new ArrayList<>(shareSwaggerDTOList.stream().map(shareSwaggerDTO -> getPItemWPL(username, shareSwaggerDTO)).toList());
        } else {
            return new ArrayList<>(shareDTOList.stream().map(shareDTO -> aggregatePItem(username, shareDTO)).toList());
        }

    }

    public void addNewPItem(PurchaseDTO purchaseDTO) throws FormNotValidException{
        ShareDTO shareDTO = purchaseDTO.getShareDTO();
        ArrayList<ExceptionDTO> exceptionList = new ArrayList<>();
        try{
            ShareDTO shareDTOSwag = new ShareDTO(this.shareSwaggerClient.getShare(shareDTO.getIsin()));
            exceptionList.addAll(shareService.validateForm(shareDTOSwag));
            exceptionList.addAll(purchaseService.validateForm(purchaseDTO));
            if (exceptionList.isEmpty()){
                shareService.saveShare(shareDTOSwag);
                purchaseService.savePurchase(purchaseDTO);
            } else {
                throw new FormNotValidException("Formfehler", exceptionList);
            }
        } catch(NullPointerException e) {
            exceptionList.add(new ExceptionDTO("isin", "ISIN nicht bekannt"));
            throw new FormNotValidException("Isin unbekannt", exceptionList );
        }
    }

    public PortfolioItemDTO aggregatePItem(String username, ShareDTO shareDTO){
        /* The method is used to create PortfolioItems from the related purchases.
           It collects the purchases for a shareDTO, checks whether the authenticated
           user liked the share, and aggregates further metrics i.e. averagePrice, totalPrice
           and totalQuantity. It returns a new object of class PortfolioItemDTO.
         */
        ArrayList<PurchaseDTO> purchaseDTOList = this.purchaseService.getPurchases(shareDTO.getIsin());
        String isin = shareDTO.getIsin();
        LikesId likesId = new LikesId(username,isin);
        boolean isFavorite = this.likesRepository.existsById(likesId);
        double totalPrice = purchaseDTOList.stream()
                .mapToDouble(pItem ->
                        pItem.getQuantity() * pItem.getPurchasePrice()).sum();

        int totalQuantity = purchaseDTOList.stream()
                .mapToInt(PurchaseDTO::getQuantity).sum();

        double avgPrice = totalPrice / totalQuantity;
        return new PortfolioItemDTO(shareDTO, avgPrice, totalPrice, totalQuantity,
                purchaseDTOList, isFavorite);
    }

    public PortfolioItemDTO getPItemWPL(String username, ShareSwaggerDTO shareSwaggerDTO){
        /* The method is used to create a PortfolioItemDTO. It uses the method
           "aggregatePitem" to create a PortfolioItemDTO and adds profit/loss information,
           calculated from the requested price of the external API.
         */
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
        /* This method is used to fastly get multiple shares from the external API.
           The method launches multithreaded http requests, where the number of threads is
           dependent on the number of available Processors. For a computer with 8 available processors,
           the loading time can be reduced by a approx. factor of 8.
         */
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
                shareSwaggerDTOList.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return shareSwaggerDTOList;
    }
}

class FeignHttpRequest implements Callable<ShareSwaggerDTO> {
    /** A wrapper class that implements the interface Callable.
        It is used to make the feign http requests multithreadable.
        isin:   The ISIN of the share that should be requested.
        client: The client that should be used for requesting.
     */
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
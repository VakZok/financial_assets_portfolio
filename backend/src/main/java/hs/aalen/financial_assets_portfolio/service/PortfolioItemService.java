package hs.aalen.financial_assets_portfolio.service;


import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.persistence.PortfolioItemRepository;
import hs.aalen.financial_assets_portfolio.persistence.ShareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PortfolioItemService {

    @DateTimeFormat(pattern="dd.MM.yyyy")
    private static final LocalDate MIN_DATE = LocalDate.of(1903,04,22);
    @DateTimeFormat(pattern="dd.MM.yyyy")
    private static final LocalDate MAX_DATE = LocalDate.of(2123,12,31);

    @Autowired
    private PortfolioItemRepository portfolioItemRepository;

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private ShareService shareService;

    public PortfolioItemService(PortfolioItemRepository portfolioItemRepository) {
        this.portfolioItemRepository = portfolioItemRepository;
    }

    public PortfolioItem getPortfolioItem(Long id)throws NoSuchElementException {
        Optional<PortfolioItem> item = portfolioItemRepository.findById(id);
        if (item.isPresent()){
            return item.get();
        }else {
            throw new NoSuchElementException("Portfolioitem wurde nicht gefunden");
        }
    }

    public void addPortfolioItem(PItemDTO pItemDTO) throws FormNotValidException {
        ShareDTO shareDTO = pItemDTO.getShareDTO();
        Share share = new Share(shareDTO);
        if(!(shareService.checkShareExists(share))){
            shareService.addShare(shareDTO);
            ArrayList<ExceptionDTO> exceptions = formIsValid(pItemDTO);
            if(exceptions.size() == 0){
                PortfolioItem pItem = new PortfolioItem(pItemDTO);
                portfolioItemRepository.save(pItem);
            } else{
                throw new FormNotValidException("Formfehler", exceptions);
            }
        }else {
            ArrayList<ExceptionDTO> exceptions = formIsValid(pItemDTO);
            if(exceptions.size() == 0){
                PortfolioItem pItem = new PortfolioItem(pItemDTO);
                portfolioItemRepository.save(pItem);
            } else{
                throw new FormNotValidException("Formfehler", exceptions);
            }
        }
    }

    public List<PortfolioItem> getPortfolioItemList(){
        return portfolioItemRepository.findAll();
    }

    public ArrayList<ExceptionDTO> formIsValid(PItemDTO pItemDTO){
        ArrayList<ExceptionDTO> exceptions = new ArrayList<ExceptionDTO>();
        if(pItemDTO.getPurchasePrice() == 0){
            exceptions.add(new ExceptionDTO("purchasePrice", "Der Kaufpreis darf nicht null sein."));
        }
        if(pItemDTO.getQuantity() == 0){
            exceptions.add(new ExceptionDTO("quantity", "Die Anzahl muss mindestens eins betragen."));
        }
        if(pItemDTO.getPurchaseDate().isBefore(MIN_DATE)){
            exceptions.add(new ExceptionDTO(
                    "purchaseDate", "Das Kaufdatum muss nach dem " + MIN_DATE + " liegen."));
        }
        if(pItemDTO.getPurchaseDate().isAfter(MAX_DATE)){
            exceptions.add(new ExceptionDTO(
                    "purchaseDate", "Das Kaufdatum muss vor dem " + MAX_DATE + " liegen."));
        }
        return exceptions;
    }

}

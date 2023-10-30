package hs.aalen.financial_assets_portfolio.web;

import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.service.PortfolioItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioItemController {

    private final PortfolioItemService portfolioItemService;

    public PortfolioItemController(PortfolioItemService portfolioItemService) {
        this.portfolioItemService = portfolioItemService;
    }

    /* GET REQUESTS */
    @GetMapping("portfolioItems/{id}")
    public ResponseEntity<Object> getPortfolioItem(@PathVariable Long id){
        try{
            PortfolioItem pItem = portfolioItemService.getPortfolioItem(id);
            PItemDTO pItemDTO = new PItemDTO(pItem);
            return new ResponseEntity<>(pItemDTO, HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity<>(new PItemDTO(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/portfolioItems/preview")
    public ResponseEntity<Object> getPortfolioItemList() {
        List<PItemDTO> pItemDTOPrevList = portfolioItemService.getPortfolioItemPreviewList();
        if(pItemDTOPrevList.isEmpty()) {
            pItemDTOPrevList.add(new PItemDTO());
        }
        System.out.println(pItemDTOPrevList);
        return new ResponseEntity<>(pItemDTOPrevList, HttpStatus.OK);
    }

    /* POST REQUESTS */
    @PostMapping("portfolioItems/add")
    public ResponseEntity<Object> addPortfolioItem(@RequestBody PItemDTO pItemDTO){
        try{
            portfolioItemService.addPortfolioItem(pItemDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(FormNotValidException e){
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

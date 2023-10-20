package hs.aalen.financial_assets_portfolio.web;

import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import hs.aalen.financial_assets_portfolio.data.PItemPreviewDTO;
import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.exceptions.PortfolioItemException;
import hs.aalen.financial_assets_portfolio.service.PortfolioItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioItemController {
    @Autowired
    private PortfolioItemService portfolioItemService;

    public PortfolioItemController(PortfolioItemService portfolioItemService) {
        this.portfolioItemService = portfolioItemService;
    }

    @GetMapping("portfolioItems/{id}")
    public ResponseEntity<Object> getPortfolioItem(@PathVariable Long id){
        try{
            PortfolioItem pItem = portfolioItemService.getPortfolioItem(id);
            PItemDTO pItemDTO = new PItemDTO(pItem);
            return new ResponseEntity<>(pItemDTO, HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/portfolioItems/preview", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPortfolioItemList() {
        List<PortfolioItem> pItemList = portfolioItemService.getPortfolioItemList();
        List<PItemPreviewDTO> pItemPreviewDTOList = pItemList.stream().map(PItemPreviewDTO::new).toList();
        return new ResponseEntity<Object>(pItemPreviewDTOList, HttpStatus.OK);
    }

    @PostMapping("portfolioItems/add")
    public ResponseEntity<Object> addPortfolioItem(@RequestBody PItemDTO pItemDTO){
        try{
            portfolioItemService.addPortfolioItem(pItemDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(PortfolioItemException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}

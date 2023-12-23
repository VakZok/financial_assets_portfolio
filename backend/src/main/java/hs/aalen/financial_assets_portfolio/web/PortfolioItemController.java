package hs.aalen.financial_assets_portfolio.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hs.aalen.financial_assets_portfolio.data.PortfolioItemDTO;
import hs.aalen.financial_assets_portfolio.data.PurchaseDTO;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.service.PortfolioItemService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioItemController {

    private final PortfolioItemService pItemService;
    private final HttpHeaders JSON_HEADER = new HttpHeaders();

    public PortfolioItemController(PortfolioItemService pItemService) {
        this.pItemService = pItemService;
        JSON_HEADER.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    /* GET REQUESTS */
    @GetMapping("portfolioItems/{isin}")
    public ResponseEntity<Object> getPItemByIsin(@PathVariable String isin) {
        try {
            PortfolioItemDTO portfolioItemDTO = this.pItemService.getPItemByISIN(isin);
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();

            filterProvider.addFilter("pItemFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "shareDTO", "avgPrice", "totalQuantity", "purchaseDTOList"));

            filterProvider.addFilter("shareFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "name", "isin", "description", "category"));

            filterProvider.addFilter("purchaseFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "purchasePrice", "totalPrice", "quantity", "purchaseDate"));

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(portfolioItemDTO);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/portfolioItems/preview")
    public ResponseEntity<Object> getPItemsPreview() {
        List<PortfolioItemDTO> portfolioItemDTOList = this.pItemService.getPItemsPreview();
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("pItemFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "shareDTO", "avgPrice", "totalPrice", "totalQuantity"));
            filterProvider.addFilter("shareFilter",
                    SimpleBeanPropertyFilter.serializeAll());

            ObjectMapper om = new ObjectMapper();
            String mappedObject = om.writer(filterProvider).writeValueAsString(portfolioItemDTOList);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("portfolioItems/add")
    public ResponseEntity<Object> postPItem(@RequestBody PurchaseDTO purchaseDTO) {
        try {
            this.pItemService.addNewPItem(purchaseDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (FormNotValidException e) {
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.BAD_REQUEST);
        }
    }


}
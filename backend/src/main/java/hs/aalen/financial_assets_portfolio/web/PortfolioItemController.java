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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioItemController {
    /** Controller Class that manages HTTP-Requests regarding portfolio items
     * JSON_HEADER:  The Header that is sent with each response.
     * pItemService: The service that is connected to manage portfolio items.
     * */

    private final HttpHeaders JSON_HEADER = new HttpHeaders();
    private final PortfolioItemService pItemService;

    /* CONSTRUCTOR */

    public PortfolioItemController(PortfolioItemService pItemService) {
        this.pItemService = pItemService;
        JSON_HEADER.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    /* HTTP REQUEST METHODS */

    @GetMapping("swagger/pItem/{isin}")
    public ResponseEntity<Object> getPItemSwaggerByIsin(@PathVariable String isin) {
        try {
            PortfolioItemDTO result = this.pItemService.getPItemSwagger(isin);
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();

            filterProvider.addFilter("pItemFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "shareDTO", "currentPurchasePrice"));

            filterProvider.addFilter("shareFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "name", "isin", "description", "category"));

            filterProvider.addFilter("purchaseFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept());

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(result);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);

        } catch (NoSuchElementException | NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("portfolioItems/{isin}")
    public ResponseEntity<Object> getPItemByIsin(@PathVariable String isin, Authentication authentication) {
        try {
            PortfolioItemDTO portfolioItemDTO = this.pItemService.getPItemByISIN(authentication.getName(), isin);
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();

            filterProvider.addFilter("pItemFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "shareDTO", "avgPrice", "totalQuantity", "purchaseDTOList", "currentPurchasePrice", "profitAndLoss", "profitAndLossCum"));

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
    public ResponseEntity<Object> getPItemsPreview(@RequestParam boolean includePL, Authentication authentication) {
        try {
            List<PortfolioItemDTO> portfolioItemDTOList = this.pItemService.getPItemsPreview(authentication.getName(), includePL);
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("pItemFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "shareDTO", "avgPrice", "totalPrice", "totalQuantity", "profitAndLossCum", "isFavorite"));
            filterProvider.addFilter("shareFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept("isin", "name"));

            ObjectMapper om = new ObjectMapper();
            String mappedObject = om.writer(filterProvider).writeValueAsString(portfolioItemDTOList);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/portfolioItems/liked")
    public ResponseEntity<Object> getPItemsLiked(@RequestParam boolean includePL, Authentication authentication) {
        try {
            List<PortfolioItemDTO> portfolioItemDTOList = this.pItemService.getLikedPItems(authentication.getName(), includePL);
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("pItemFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "shareDTO", "avgPrice", "totalPrice", "totalQuantity", "isFavorite", "profitAndLoss", "profitAndLossCum"));
            filterProvider.addFilter("shareFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept("isin", "name"));

            ObjectMapper om = new ObjectMapper();
            String mappedObject = om.writer(filterProvider).writeValueAsString(portfolioItemDTOList);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("portfolioItems/{isin}/likes")
    public ResponseEntity<Object> postLike(@PathVariable String isin, Authentication authentication) {
        this.pItemService.postLikedPItem(authentication.getName(), isin);
        return new ResponseEntity<>(HttpStatus.CREATED);
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

    @DeleteMapping("portfolioItems/{isin}/likes")
    public ResponseEntity<Object> deleteLike(@PathVariable String isin, Authentication authentication) {
        this.pItemService.deleteLikedPItems(authentication.getName(), isin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
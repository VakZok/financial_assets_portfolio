package hs.aalen.financial_assets_portfolio.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.PortfolioItemDTO;
import hs.aalen.financial_assets_portfolio.data.PurchaseDTO;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.service.PurchaseService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final HttpHeaders JSON_HEADER = new HttpHeaders();

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
        JSON_HEADER.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    /* GET REQUESTS */
    @GetMapping("purchases/{id}")
    public ResponseEntity<Object> getPurchase(@PathVariable Long id) {
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("pItemFilter", SimpleBeanPropertyFilter.serializeAllExcept());
            filterProvider.addFilter("shareFilter", SimpleBeanPropertyFilter.serializeAllExcept());
            filterProvider.addFilter("purchaseFilter", SimpleBeanPropertyFilter.serializeAllExcept());

            PurchaseDTO purchaseDTO = purchaseService.getPurchase(id);

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(purchaseDTO);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/purchases/preview")
    public ResponseEntity<Object> getPurchaseList() {
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("pItemFilter", SimpleBeanPropertyFilter.serializeAllExcept());
            filterProvider.addFilter("shareFilter", SimpleBeanPropertyFilter.serializeAllExcept());
            filterProvider.addFilter("purchaseFilter", SimpleBeanPropertyFilter.serializeAllExcept());

            ArrayList<PurchaseDTO> purchaseDTOList = purchaseService.getPurchaseList();

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(purchaseDTOList);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>( JSON_HEADER, HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /* POST REQUESTS */
    @PostMapping("portfolioItems/{wkn}/purchases/add")
    public ResponseEntity<Object> addNewPurchase(@PathVariable String wkn, @RequestBody PurchaseDTO purchaseDTO) {
        try {
            purchaseService.addNewPurchase(purchaseDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new ExceptionDTO("wkn", e.getMessage()), HttpStatus.CONFLICT);
        } catch (FormNotValidException e) {
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.BAD_REQUEST);
        }
    }
}


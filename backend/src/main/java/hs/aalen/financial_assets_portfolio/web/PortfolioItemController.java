package hs.aalen.financial_assets_portfolio.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.PItemAggDTO;
import hs.aalen.financial_assets_portfolio.data.PItemDTO;
import hs.aalen.financial_assets_portfolio.domain.PortfolioItem;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.service.PortfolioItemService;
import hs.aalen.financial_assets_portfolio.service.ShareService;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioItemController {

    private final PortfolioItemService portfolioItemService;
    private final HttpHeaders JSON_HEADER = new HttpHeaders();

    public PortfolioItemController(PortfolioItemService portfolioItemService) {
        this.portfolioItemService = portfolioItemService;
        JSON_HEADER.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    /* GET REQUESTS */
    @Deprecated
    @GetMapping("portfolioItems/{id}")
    public ResponseEntity<Object> getPortfolioItem(@PathVariable Long id) {
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("pItemAggFilter", SimpleBeanPropertyFilter.serializeAllExcept());
            filterProvider.addFilter("shareFilter", SimpleBeanPropertyFilter.serializeAllExcept());
            filterProvider.addFilter("pItemFilter", SimpleBeanPropertyFilter.serializeAllExcept());

            PItemDTO pItemDTO = portfolioItemService.getPortfolioItem(id);

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(pItemDTO);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("portfolioItems/viewBy/wkn/{wkn}")
    public ResponseEntity<Object> getWKNAggPItem(@PathVariable String wkn) {
        PItemAggDTO pItemAggDTO = portfolioItemService.getWKNAggPItem(wkn);
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("pItemAggFilter", SimpleBeanPropertyFilter.filterOutAllExcept("shareDTO", "avgPrice", "totalQuantity", "pItemDTOList"));
            filterProvider.addFilter("shareFilter", SimpleBeanPropertyFilter.filterOutAllExcept("name", "wkn"));
            filterProvider.addFilter("pItemFilter", SimpleBeanPropertyFilter.filterOutAllExcept("purchasePrice", "totalPrice", "quantity", "purchaseDate"));

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(pItemAggDTO);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);

        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Deprecated
    @GetMapping(value = "/portfolioItems/preview")
    public ResponseEntity<Object> getPortfolioItemList() {
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("pItemAggFilter", SimpleBeanPropertyFilter.serializeAllExcept());
            filterProvider.addFilter("shareFilter", SimpleBeanPropertyFilter.serializeAllExcept());
            filterProvider.addFilter("pItemFilter", SimpleBeanPropertyFilter.serializeAllExcept());

            ArrayList<PItemDTO> pItemDTOList = portfolioItemService.getPortfolioItemList();

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(pItemDTOList);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>( JSON_HEADER, HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/portfolioItems/viewBy/wkn/preview")
    public ResponseEntity<Object> getWKNAggPItemsList() {
        List<PItemAggDTO> pItemAggDTOList = portfolioItemService.getWKNAggPItemsPreview();
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("pItemAggFilter", SimpleBeanPropertyFilter.filterOutAllExcept("shareDTO", "avgPrice", "totalQuantity"));
            filterProvider.addFilter("shareFilter", SimpleBeanPropertyFilter.filterOutAllExcept("name", "wkn"));

            ObjectMapper om = new ObjectMapper();
            String mappedObject = om.writer(filterProvider).writeValueAsString(pItemAggDTOList);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /* POST REQUESTS */
    @PostMapping("portfolioItems/add")
    public ResponseEntity<Object> addPortfolioItem(@RequestBody PItemDTO pItemDTO) {
        try {
            portfolioItemService.addPortfolioItem(pItemDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new ExceptionDTO("wkn", e.getMessage()), HttpStatus.CONFLICT);
        } catch (FormNotValidException e) {
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.BAD_REQUEST);
        }
    }
}


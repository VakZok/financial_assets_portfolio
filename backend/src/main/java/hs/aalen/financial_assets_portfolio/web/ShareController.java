package hs.aalen.financial_assets_portfolio.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.data.ShareDTO;
import hs.aalen.financial_assets_portfolio.domain.Share;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.service.ShareService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class ShareController {
    private final ShareService shareService;
    private final HttpHeaders JSON_HEADER = new HttpHeaders();

    public ShareController(ShareService shareService) {
        this.shareService = shareService;
        JSON_HEADER.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    /* GET REQUESTS */
    @GetMapping("/shares/{wkn}")
    public ResponseEntity<Object> getShare(@PathVariable String wkn) {
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("shareFilter", SimpleBeanPropertyFilter.serializeAllExcept());

            ShareDTO shareDTO = shareService.getShare(wkn);

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(shareDTO);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(JSON_HEADER, HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/shares")
    public ResponseEntity<Object> getShareList() {
        try {
            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("shareFilter", SimpleBeanPropertyFilter.serializeAllExcept());

            ArrayList<ShareDTO> shareDTOList = shareService.getShareList();

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(shareDTOList);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(JSON_HEADER, HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /* POST REQUESTS */
    @PostMapping("/shares/add")
    public ResponseEntity<Object> addShare(@Valid @RequestBody ShareDTO shareDTO){
        try{
            shareService.addShare(shareDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new ExceptionDTO("wkn", e.getMessage()), JSON_HEADER, HttpStatus.CONFLICT);
        } catch (FormNotValidException e){
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /* PUT REQUESTS */
    @PutMapping("/shares/update/{wkn}")
    public ResponseEntity<Object> updateShare(@PathVariable String wkn,
                                              @RequestBody ShareDTO shareDTO){
        try{
            shareService.updateShare(wkn, shareDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FormNotValidException e){
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}

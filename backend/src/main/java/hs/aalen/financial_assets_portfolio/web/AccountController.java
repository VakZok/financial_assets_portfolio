package hs.aalen.financial_assets_portfolio.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hs.aalen.financial_assets_portfolio.data.AccountDTO;
import hs.aalen.financial_assets_portfolio.data.ExceptionDTO;
import hs.aalen.financial_assets_portfolio.exceptions.FormNotValidException;
import hs.aalen.financial_assets_portfolio.service.AccountService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/accounts")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {

    private final AccountService accountService;
    private final HttpHeaders JSON_HEADER = new HttpHeaders();


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
        JSON_HEADER.add(HttpHeaders.CONTENT_TYPE, "application/json");
    }

    @GetMapping("/{username}")
    public ResponseEntity<Object> getAccount(@PathVariable String username) {
        try {

            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("accFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "username", "role", "name"));
            AccountDTO accountDTO = accountService.getAccount(username);

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(accountDTO);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/preview")
    public ResponseEntity<Object> getAccounts() {
        try {

            SimpleFilterProvider filterProvider = new SimpleFilterProvider();
            filterProvider.addFilter("accFilter",
                    SimpleBeanPropertyFilter.filterOutAllExcept(
                            "username", "role", "name"));
            ArrayList<AccountDTO> accountDTOList = accountService.getAccounts();

            ObjectMapper om = new ObjectMapper();
            om.registerModule(new JavaTimeModule());
            String mappedObject = om.writer(filterProvider).writeValueAsString(accountDTOList);
            return new ResponseEntity<>(mappedObject, JSON_HEADER, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/{username}/add")
    public ResponseEntity<Object> addAccount(@PathVariable String username, @RequestBody AccountDTO accountDTO) {
        try {
            accountService.addAccount(username, accountDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new ExceptionDTO("username", e.getMessage()), HttpStatus.CONFLICT);
        } catch (FormNotValidException e) {
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<Object> updateAccount(@PathVariable String username, @RequestBody AccountDTO accountDTO) {
        try {
            accountService.updateAccount(username, accountDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(new ExceptionDTO("username", e.getMessage()), HttpStatus.CONFLICT);
        } catch (FormNotValidException e) {
            return new ResponseEntity<>(e.getExceptions(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{username}")
    public void deleteAccount(@PathVariable String username, Authentication authentication) {
        accountService.deleteAccountByUsername(username, authentication.getName());
    }
}
